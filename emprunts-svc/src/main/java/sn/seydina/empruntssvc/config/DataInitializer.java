package sn.seydina.empruntssvc.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sn.seydina.empruntssvc.model.Emprunt;
import sn.seydina.empruntssvc.repository.EmpruntRepository;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final EmpruntRepository repository;

    @Override
    public void run(String... args) {
        repository.saveAll(List.of(
                new Emprunt(null, 1L, 2L, LocalDate.now().minusDays(5), LocalDate.now().plusDays(9), null, Emprunt.Statut.EN_COURS, null, null),
                new Emprunt(null, 2L, 3L, LocalDate.now().minusDays(20), LocalDate.now().minusDays(6), null, Emprunt.Statut.EN_RETARD, null, null),
                new Emprunt(null, 3L, 4L, LocalDate.now().minusDays(10), LocalDate.now().plusDays(4), LocalDate.now().minusDays(2), Emprunt.Statut.RETOURNE, null, null)
        ));
    }
}
