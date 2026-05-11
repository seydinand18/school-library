package sn.seydina.empruntssvc.client;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Membre {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String role;
    private int quota;
    private LocalDate dateInscription;
}
