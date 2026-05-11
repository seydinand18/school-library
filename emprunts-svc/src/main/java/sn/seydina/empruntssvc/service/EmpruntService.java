package sn.seydina.empruntssvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import sn.seydina.empruntssvc.client.LivreClient;
import sn.seydina.empruntssvc.client.Membre;
import sn.seydina.empruntssvc.client.MembreClient;
import sn.seydina.empruntssvc.config.RabbitMQConfig;
import sn.seydina.empruntssvc.event.EmpruntEvent;
import sn.seydina.empruntssvc.model.Emprunt;
import sn.seydina.empruntssvc.repository.EmpruntRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpruntService {

    private final EmpruntRepository repository;
    private final MembreClient membreClient;
    private final LivreClient livreClient;
    private final RabbitTemplate rabbitTemplate;

    public List<Emprunt> findAll() {
        return repository.findAll().stream().map(this::enrich).toList();
    }
    public Emprunt findById(Long id) {
        return enrich(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Emprunt introuvable : " + id)));
    }


    public List<Emprunt> findByMembre(Long membreId) {
        return repository.findByMembreId(membreId).stream().map(this::enrich).toList();
    }

    private Emprunt enrich(Emprunt emprunt) {
        emprunt.setMembre(membreClient.getMembre(emprunt.getMembreId()));
        emprunt.setLivre(livreClient.getLivre(emprunt.getLivreId()));
        return emprunt;
    }

    public Emprunt create(Emprunt emprunt) {
        Membre membre = membreClient.getMembre(emprunt.getMembreId());
        livreClient.emprunter(emprunt.getLivreId());
        emprunt.setDateEmprunt(LocalDate.now());
        emprunt.setDateRetourPrevue(LocalDate.now().plusDays(14));
        emprunt.setStatut(Emprunt.Statut.EN_COURS);
        Emprunt saved = repository.save(emprunt);

        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_EMPRUNT_CREE, new EmpruntEvent(
                saved.getId(),
                membre.getId(),
                membre.getNom(),
                membre.getPrenom(),
                membre.getEmail(),
                saved.getLivreId(),
                saved.getLivre() != null ? saved.getLivre().getTitre() : "Livre #" + saved.getLivreId(),
                saved.getDateEmprunt().toString(),
                saved.getDateRetourPrevue().toString()
        ));

        return saved;
    }

    public Emprunt retourner(Long id) {
        Emprunt emprunt = findById(id);
        livreClient.retourner(emprunt.getLivreId());
        emprunt.setDateRetourEffective(LocalDate.now());
        emprunt.setStatut(Emprunt.Statut.RETOURNE);
        return repository.save(emprunt);
    }
}
