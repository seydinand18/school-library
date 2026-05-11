package sn.seydina.empruntssvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sn.seydina.empruntssvc.model.Emprunt;
import sn.seydina.empruntssvc.service.EmpruntService;

import java.util.List;

@RestController
@RequestMapping("/emprunts")
@RequiredArgsConstructor
public class EmpruntController {

    private final EmpruntService service;

    @Tool(description = "Liste tous les emprunts de la bibliothèque")
    @GetMapping
    public List<Emprunt> findAll() {
        return service.findAll();
    }

    @Tool(description = "Récupère un emprunt par son identifiant")
    @GetMapping("/{id}")
    public Emprunt findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @Tool(description = "Liste tous les emprunts d'un membre par son identifiant")
    @GetMapping("/membre/{membreId}")
    public List<Emprunt> findByMembre(@PathVariable Long membreId) {
        return service.findByMembre(membreId);
    }

    @Tool(description = "Crée un nouvel emprunt pour un membre et un livre donnés")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Emprunt create(@RequestBody Emprunt emprunt) {
        return service.create(emprunt);
    }

    @Tool(description = "Enregistre le retour d'un livre pour un emprunt donné")
    @PatchMapping("/{id}/retour")
    public Emprunt retourner(@PathVariable Long id) {
        return service.retourner(id);
    }
}
