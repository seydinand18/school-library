package sn.seydina.membressvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.seydina.membressvc.model.Membre;

public interface MembreRepository extends JpaRepository<Membre, Long> {
}
