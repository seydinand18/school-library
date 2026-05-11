package sn.seydina.livressvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sn.seydina.livressvc.model.Livre;
import sn.seydina.livressvc.repository.LivreRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LivreService {

    private final LivreRepository livreRepository;

    public List<Livre> findAll() {
        return livreRepository.findAll();
    }

    public Livre findById(Long id) {
        return livreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livre introuvable : " + id));
    }

    public Livre create(Livre livre) {
        return livreRepository.save(livre);
    }

    public Livre update(Long id, Livre updated) {
        Livre existing = findById(id);
        existing.setTitre(updated.getTitre());
        existing.setAuteur(updated.getAuteur());
        existing.setIsbn(updated.getIsbn());
        existing.setGenre(updated.getGenre());
        existing.setClasseScolaire(updated.getClasseScolaire());
        existing.setQuantiteTotale(updated.getQuantiteTotale());
        existing.setQuantiteDisponible(updated.getQuantiteDisponible());
        return livreRepository.save(existing);
    }

    public void delete(Long id) {
        livreRepository.deleteById(id);
    }

    public Livre emprunter(Long livreId) {
        Livre livre = findById(livreId);
        if (livre.getQuantiteDisponible() <= 0) {
            throw new RuntimeException("Aucun exemplaire disponible pour le livre : " + livreId);
        }
        livre.setQuantiteDisponible(livre.getQuantiteDisponible() - 1);
        return livreRepository.save(livre);
    }

    public Livre retourner(Long livreId) {
        Livre livre = findById(livreId);
        livre.setQuantiteDisponible(livre.getQuantiteDisponible() + 1);
        return livreRepository.save(livre);
    }
}
