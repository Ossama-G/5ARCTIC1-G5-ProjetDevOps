package tn.esprit.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.dto.SkierDTO;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.services.ISkierServices;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "\uD83C\uDFC2 Skier Management")
@RestController
@RequestMapping("/skier")
@RequiredArgsConstructor
public class SkierRestController {

    private final ISkierServices skierServices;

    @Operation(description = "Add Skier")
    @PostMapping("/add")
    public SkierDTO addSkier(@RequestBody SkierDTO skierDTO){
        Skier skier = convertToEntity(skierDTO);
        Skier savedSkier = skierServices.addSkier(skier);
        return convertToDTO(savedSkier);
    }

    @Operation(description = "Add Skier And Assign To Course")
    @PostMapping("/addAndAssign/{numCourse}")
    public SkierDTO addSkierAndAssignToCourse(@RequestBody SkierDTO skierDTO,
                                              @PathVariable("numCourse") Long numCourse){
        Skier skier = convertToEntity(skierDTO);
        Skier savedSkier = skierServices.addSkierAndAssignToCourse(skier, numCourse);
        return convertToDTO(savedSkier);
    }

    @Operation(description = "Assign Skier To Subscription")
    @PutMapping("/assignToSub/{numSkier}/{numSub}")
    public SkierDTO assignToSubscription(@PathVariable("numSkier") Long numSkier,
                                         @PathVariable("numSub") Long numSub){
        Skier skier = skierServices.assignSkierToSubscription(numSkier, numSub);
        return convertToDTO(skier);
    }

    @Operation(description = "Assign Skier To Piste")
    @PutMapping("/assignToPiste/{numSkier}/{numPiste}")
    public SkierDTO assignToPiste(@PathVariable("numSkier") Long numSkier,
                                  @PathVariable("numPiste") Long numPiste){
        Skier skier = skierServices.assignSkierToPiste(numSkier, numPiste);
        return convertToDTO(skier);
    }

    @Operation(description = "retrieve Skiers By Subscription Type")
    @GetMapping("/getSkiersBySubscription")
    public List<SkierDTO> retrieveSkiersBySubscriptionType(TypeSubscription typeSubscription) {
        return skierServices.retrieveSkiersBySubscriptionType(typeSubscription).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Operation(description = "Retrieve Skier by Id")
    @GetMapping("/get/{id-skier}")
    public SkierDTO getById(@PathVariable("id-skier") Long numSkier){
        Skier skier = skierServices.retrieveSkier(numSkier);
        return convertToDTO(skier);
    }

    @Operation(description = "Delete Skier by Id")
    @DeleteMapping("/delete/{id-skier}")
    public void deleteById(@PathVariable("id-skier") Long numSkier){
        skierServices.removeSkier(numSkier);
    }

    @Operation(description = "Retrieve all Skiers")
    @GetMapping("/all")
    public List<SkierDTO> getAllSkiers(){
        return skierServices.retrieveAllSkiers().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private SkierDTO convertToDTO(Skier skier) {
        SkierDTO skierDTO = new SkierDTO();
        skierDTO.setNumSkier(skier.getNumSkier());
        skierDTO.setFirstName(skier.getFirstName());
        skierDTO.setLastName(skier.getLastName());
        skierDTO.setDateOfBirth(skier.getDateOfBirth());
        skierDTO.setCity(skier.getCity());
        skierDTO.setSubscriptionId(skier.getSubscription() != null ? skier.getSubscription().getNumSub() : null);
        return skierDTO;
    }

    private Skier convertToEntity(SkierDTO skierDTO) {
        Skier skier = new Skier();
        skier.setNumSkier(skierDTO.getNumSkier());
        skier.setFirstName(skierDTO.getFirstName());
        skier.setLastName(skierDTO.getLastName());
        skier.setDateOfBirth(skierDTO.getDateOfBirth());
        skier.setCity(skierDTO.getCity());
        // You may need to fetch the Subscription entity by ID if necessary
        return skier;
    }
}