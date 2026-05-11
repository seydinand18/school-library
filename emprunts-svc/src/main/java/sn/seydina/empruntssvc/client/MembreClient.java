package sn.seydina.empruntssvc.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "membres-svc", fallback = MembreClientFallback.class)
public interface MembreClient {

    @GetMapping("/membres/{id}")
    Membre getMembre(@PathVariable Long id);
}
