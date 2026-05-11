package sn.seydina.empruntssvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.seydina.empruntssvc.model.Emprunt;

import java.util.List;

public interface EmpruntRepository extends JpaRepository<Emprunt, Long> {
    List<Emprunt> findByMembreId(Long membreId);
}
