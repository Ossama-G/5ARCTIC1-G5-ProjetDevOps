package tn.esprit.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.dto.PisteDTO;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.services.IPisteServices;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "🏔️ Piste Management")
@RestController
@RequestMapping("/piste")
@RequiredArgsConstructor
public class PisteRestController {

    private final IPisteServices pisteServices;

    @Operation(description = "Add Piste")
    @PostMapping("/add")
    public PisteDTO addPiste(@RequestBody PisteDTO pisteDTO) {
        Piste piste = convertToEntity(pisteDTO);
        Piste savedPiste = pisteServices.addPiste(piste);
        return convertToDTO(savedPiste);
    }

    @Operation(description = "Retrieve all Pistes")
    @GetMapping("/all")
    public List<PisteDTO> getAllPistes() {
        return pisteServices.retrieveAllPistes()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Operation(description = "Retrieve Piste by Id")
    @GetMapping("/get/{id-piste}")
    public PisteDTO getById(@PathVariable("id-piste") Long numPiste) {
        Piste piste = pisteServices.retrievePiste(numPiste);
        return convertToDTO(piste);
    }

    // Méthodes utilitaires pour la conversion entre Piste et PisteDTO
    private Piste convertToEntity(PisteDTO pisteDTO) {
        Piste piste = new Piste();
        piste.setNumPiste(pisteDTO.getNumPiste());
        piste.setNamePiste(pisteDTO.getNamePiste());
        piste.setColor(pisteDTO.getColor());
        piste.setLength(pisteDTO.getLength());
        piste.setSlope(pisteDTO.getSlope());
        return piste;
    }

    private PisteDTO convertToDTO(Piste piste) {
        PisteDTO pisteDTO = new PisteDTO();
        pisteDTO.setNumPiste(piste.getNumPiste());
        pisteDTO.setNamePiste(piste.getNamePiste());
        pisteDTO.setColor(piste.getColor());
        pisteDTO.setLength(piste.getLength());
        pisteDTO.setSlope(piste.getSlope());
        return pisteDTO;
    }
}