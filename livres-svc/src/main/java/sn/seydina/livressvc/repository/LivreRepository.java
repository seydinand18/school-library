package sn.seydina.livressvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.seydina.livressvc.model.Livre;

public interface LivreRepository extends JpaRepository<Livre, Long> {
}
