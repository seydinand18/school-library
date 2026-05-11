package sn.seydina.empruntssvc.client;

import org.springframework.stereotype.Component;

@Component
public class MembreClientFallback implements MembreClient {

    @Override
    public Membre getMembre(Long id) {
        Membre fallback = new Membre();
        fallback.setId(id);
        fallback.setNom("Service indisponible");
        return fallback;
    }
}
