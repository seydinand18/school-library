package sn.seydina.livressvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sn.seydina.livressvc.model.Livre;
import sn.seydina.livressvc.service.LivreService;

import java.util.List;

@RestController
@RequestMapping("/livres")
@RequiredArgsConstructor
public class LivreController {

    private final LivreService service;

    @Tool(description = "Liste tous les livres du catalogue de la bibliothèque")
    @GetMapping
    public List<Livre> findAll() {
        return service.findAll();
    }

    @Tool(description = "Récupère un livre par son identifiant")
    @GetMapping("/{id}")
    public Livre findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @Tool(description = "Ajoute un nouveau livre dans le catalogue")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Livre create(@RequestBody Livre livre) {
        return service.create(livre);
    }

    @Tool(description = "Met à jour les informations d'un livre existant")
    @PutMapping("/{id}")
    public Livre update(@PathVariable Long id, @RequestBody Livre livre) {
        return service.update(id, livre);
    }

    @Tool(description = "Supprime un livre du catalogue par son identifiant")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @Tool(description = "Décrémente la quantité disponible d'un livre lors d'un emprunt")
    @PutMapping("/{id}/emprunter")
    public Livre emprunter(@PathVariable Long id) {
        return service.emprunter(id);
    }

    @Tool(description = "Incrémente la quantité disponible d'un livre lors d'un retour")
    @PutMapping("/{id}/retourner")
    public Livre retourner(@PathVariable Long id) {
        return service.retourner(id);
    }
}
