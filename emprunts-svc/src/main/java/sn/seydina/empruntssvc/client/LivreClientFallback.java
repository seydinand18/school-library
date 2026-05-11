package sn.seydina.empruntssvc.client;

import org.springframework.stereotype.Component;

@Component
public class LivreClientFallback implements LivreClient {

    @Override
    public Livre getLivre(Long id) {
        Livre fallback = new Livre();
        fallback.setId(id);
        fallback.setTitre("Service indisponible");
        return fallback;
    }

    @Override
    public Livre emprunter(Long id) {
        throw new RuntimeException("livres-svc indisponible");
    }

    @Override
    public Livre retourner(Long id) {
        throw new RuntimeException("livres-svc indisponible");
    }
}
