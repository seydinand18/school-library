package sn.seydina.membressvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sn.seydina.membressvc.model.Membre;
import sn.seydina.membressvc.repository.MembreRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MembreService {

    private final MembreRepository repository;

    public List<Membre> findAll() {
        return repository.findAll();
    }

    public Membre findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Membre introuvable : " + id));
    }

    public Membre create(Membre membre) {
        membre.setDateInscription(LocalDate.now());
        return repository.save(membre);
    }

    public Membre update(Long id, Membre updated) {
        Membre existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Membre introuvable : " + id));
        existing.setNom(updated.getNom());
        existing.setPrenom(updated.getPrenom());
        existing.setEmail(updated.getEmail());
        existing.setRole(updated.getRole());
        existing.setQuota(updated.getQuota());
        return repository.save(existing);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
