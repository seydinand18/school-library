package sn.seydina.empruntssvc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.seydina.empruntssvc.client.Livre;
import sn.seydina.empruntssvc.client.Membre;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Emprunt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long membreId;
    private Long livreId;

    private LocalDate dateEmprunt;
    private LocalDate dateRetourPrevue;
    private LocalDate dateRetourEffective;

    @Enumerated(EnumType.STRING)
    private Statut statut;

    @Transient
    private Membre membre;

    @Transient
    private Livre livre;

    public enum Statut {
        EN_COURS, RETOURNE, EN_RETARD
    }
}
