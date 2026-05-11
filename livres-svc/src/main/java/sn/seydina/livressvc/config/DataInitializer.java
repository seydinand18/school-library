package sn.seydina.livressvc.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sn.seydina.livressvc.model.Livre;
import sn.seydina.livressvc.repository.LivreRepository;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final LivreRepository livreRepository;

    @Override
    public void run(String... args) {
        livreRepository.save(new Livre(null, "Le Monde s'effondre", "Chinua Achebe", "978-2-07-036822-8", "Roman", "3ème", 3, 2));
        livreRepository.save(new Livre(null, "L'Aventure ambiguë", "Cheikh Hamidou Kane", "978-2-07-036823-5", "Roman", "Terminale", 2, 2));
        livreRepository.save(new Livre(null, "Mathématiques 3ème", "Collectif", "978-2-01-123456-7", "Manuel", "3ème", 5, 4));
        livreRepository.save(new Livre(null, "Histoire-Géographie Tle", "Collectif", "978-2-01-234567-8", "Manuel", "Terminale", 4, 3));
    }
}
