package sn.seydina.membressvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sn.seydina.membressvc.client.EmpruntClient;
import sn.seydina.membressvc.model.Membre;
import sn.seydina.membressvc.service.MembreService;

import java.util.List;

@RestController
@RequestMapping("/membres")
@RequiredArgsConstructor
public class MembreController {

    private final MembreService service;
    private final EmpruntClient empruntClient;

    @Tool(description = "Liste tous les membres inscrits à la bibliothèque")
    @GetMapping
    public List<Membre> findAll() {
        return service.findAll();
    }

    @Tool(description = "Récupère un membre par son identifiant")
    @GetMapping("/{id}")
    public Membre findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @Tool(description = "Inscrit un nouveau membre dans la bibliothèque")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Membre create(@RequestBody Membre membre) {
        return service.create(membre);
    }

    @Tool(description = "Met à jour les informations d'un membre existant")
    @PutMapping("/{id}")
    public Membre update(@PathVariable Long id, @RequestBody Membre membre) {
        return service.update(id, membre);
    }

    @Tool(description = "Supprime un membre de la bibliothèque par son identifiant")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @Tool(description = "Liste tous les emprunts en cours et passés d'un membre")
    @GetMapping("/{id}/emprunts")
    public List<Object> getEmprunts(@PathVariable Long id) {
        return empruntClient.getEmpruntsByMembre(id);
    }
}
