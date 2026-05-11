package sn.seydina.membressvc.client;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class EmpruntClientFallback implements EmpruntClient {

    @Override
    public List<Object> getEmpruntsByMembre(Long membreId) {
        return Collections.emptyList();
    }
}
