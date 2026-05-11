package sn.seydina.empruntssvc.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "livres-svc", fallback = LivreClientFallback.class)
public interface LivreClient {

    @GetMapping("/livres/{id}")
    Livre getLivre(@PathVariable Long id);

    @PutMapping("/livres/{id}/emprunter")
    Livre emprunter(@PathVariable Long id);

    @PutMapping("/livres/{id}/retourner")
    Livre retourner(@PathVariable Long id);
}
