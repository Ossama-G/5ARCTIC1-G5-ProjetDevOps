package tn.esprit.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.dto.SkierDTO;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.services.ISkierServices;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "🏂 Skier Management")
@RestController
@RequestMapping("/skier")
@RequiredArgsConstructor
public class SkierRestController {

    private final ISkierServices skierServices;
    private final ModelMapper modelMapper;

    @Operation(description = "Add Skier")
    @PostMapping("/add")
    public SkierDTO addSkier(@RequestBody SkierDTO skierDTO){
        Skier skier = modelMapper.map(skierDTO, Skier.class);
        return modelMapper.map(skierServices.addSkier(skier), SkierDTO.class);
    }

    @Operation(description = "Add Skier And Assign To Course")
    @PostMapping("/addAndAssign/{numCourse}")
    public SkierDTO addSkierAndAssignToCourse(@RequestBody SkierDTO skierDTO,
                                              @PathVariable("numCourse") Long numCourse){
        Skier skier = modelMapper.map(skierDTO, Skier.class);
        return modelMapper.map(skierServices.addSkierAndAssignToCourse(skier, numCourse), SkierDTO.class);
    }

    @Operation(description = "Assign Skier To Subscription")
    @PutMapping("/assignToSub/{numSkier}/{numSub}")
    public SkierDTO assignToSubscription(@PathVariable("numSkier") Long numSkier,
                                         @PathVariable("numSub") Long numSub){
        return modelMapper.map(skierServices.assignSkierToSubscription(numSkier, numSub), SkierDTO.class);
    }

    @Operation(description = "Assign Skier To Piste")
    @PutMapping("/assignToPiste/{numSkier}/{numPiste}")
    public SkierDTO assignToPiste(@PathVariable("numSkier") Long numSkier,
                                  @PathVariable("numPiste") Long numPiste){
        return modelMapper.map(skierServices.assignSkierToPiste(numSkier, numPiste), SkierDTO.class);
    }

    @Operation(description = "Retrieve Skiers By Subscription Type")
    @GetMapping("/getSkiersBySubscription")
    public List<SkierDTO> retrieveSkiersBySubscriptionType(@RequestParam TypeSubscription typeSubscription) {
        return skierServices.retrieveSkiersBySubscriptionType(typeSubscription)
                .stream()
                .map(skier -> modelMapper.map(skier, SkierDTO.class))
                .collect(Collectors.toList());
    }

    @Operation(description = "Retrieve Skier by Id")
    @GetMapping("/get/{id-skier}")
    public SkierDTO getById(@PathVariable("id-skier") Long numSkier){
        return modelMapper.map(skierServices.retrieveSkier(numSkier), SkierDTO.class);
    }

    @Operation(description = "Delete Skier by Id")
    @DeleteMapping("/delete/{id-skier}")
    public void deleteById(@PathVariable("id-skier") Long numSkier){
        skierServices.removeSkier(numSkier);
    }

    @Operation(description = "Retrieve all Skiers")
    @GetMapping("/all")
    public List<SkierDTO> getAllSkiers(){
        return skierServices.retrieveAllSkiers()
                .stream()
                .map(skier -> modelMapper.map(skier, SkierDTO.class))
                .collect(Collectors.toList());
    }
}
