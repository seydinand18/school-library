package sn.seydina.membressvc.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sn.seydina.membressvc.model.Membre;
import sn.seydina.membressvc.repository.MembreRepository;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final MembreRepository repository;

    @Override
    public void run(String... args) {
        repository.saveAll(List.of(
                new Membre(null, "Diallo", "Mamadou", "mamadou.diallo@ecole.sn", Membre.Role.ELEVE, 3, LocalDate.of(2024, 9, 1)),
                new Membre(null, "Ndiaye", "Fatou", "fatou.ndiaye@ecole.sn", Membre.Role.ELEVE, 3, LocalDate.of(2024, 9, 1)),
                new Membre(null, "Sow", "Ibrahima", "ibrahima.sow@ecole.sn", Membre.Role.ELEVE, 3, LocalDate.of(2024, 9, 2)),
                new Membre(null, "Ba", "Aissatou", "aissatou.ba@ecole.sn", Membre.Role.ELEVE, 3, LocalDate.of(2024, 9, 2)),
                new Membre(null, "Fall", "Ousmane", "ousmane.fall@ecole.sn", Membre.Role.PROFESSEUR, 5, LocalDate.of(2023, 1, 15)),
                new Membre(null, "Kane", "Mariama", "mariama.kane@ecole.sn", Membre.Role.PROFESSEUR, 5, LocalDate.of(2023, 3, 10))
        ));
    }
}
