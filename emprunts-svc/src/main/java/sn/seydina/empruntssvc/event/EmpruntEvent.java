package sn.seydina.empruntssvc.event;

public record EmpruntEvent(
        Long empruntId,
        Long membreId,
        String membreNom,
        String membrePrenom,
        String membreEmail,
        Long livreId,
        String livreTitre,
        String dateEmprunt,
        String dateRetourPrevue
) {}
