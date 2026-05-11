package sn.seydina.notificationsvc.event;

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
