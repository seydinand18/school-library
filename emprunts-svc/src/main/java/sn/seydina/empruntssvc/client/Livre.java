package sn.seydina.empruntssvc.client;

import lombok.Data;

@Data
public class Livre {
    private Long id;
    private String titre;
    private String auteur;
    private String isbn;
    private String genre;
    private String classeScolaire;
    private int quantiteTotale;
    private int quantiteDisponible;
}
