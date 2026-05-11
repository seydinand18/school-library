package sn.seydina.membressvc.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "emprunts-svc", fallback = EmpruntClientFallback.class)
public interface EmpruntClient {

    @GetMapping("/emprunts/membre/{membreId}")
    List<Object> getEmpruntsByMembre(@PathVariable Long membreId);
}
