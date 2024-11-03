package tn.esprit.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.dto.SubscriptionDTO;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.services.ISubscriptionServices;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Tag(name = "\uD83D\uDC65 Subscription Management")
@RestController
@RequestMapping("/subscription")
@RequiredArgsConstructor
public class SubscriptionRestController {

    private final ISubscriptionServices subscriptionServices;

    @Operation(description = "Add Subscription")
    @PostMapping("/add")
    public SubscriptionDTO addSubscription(@RequestBody SubscriptionDTO subscriptionDTO) {
        Subscription subscription = convertToEntity(subscriptionDTO);
        Subscription savedSubscription = subscriptionServices.addSubscription(subscription);
        return convertToDTO(savedSubscription);
    }

    @Operation(description = "Retrieve Subscription by Id")
    @GetMapping("/get/{id-subscription}")
    public SubscriptionDTO getById(@PathVariable("id-subscription") Long numSubscription) {
        Subscription subscription = subscriptionServices.retrieveSubscriptionById(numSubscription);
        return convertToDTO(subscription);
    }

    @Operation(description = "Retrieve Subscriptions by Type")
    @GetMapping("/all/{typeSub}")
    public Set<SubscriptionDTO> getSubscriptionsByType(@PathVariable("typeSub") TypeSubscription typeSubscription) {
        return subscriptionServices.getSubscriptionByType(typeSubscription).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toSet());
    }

    @Operation(description = "Update Subscription")
    @PutMapping("/update")
    public SubscriptionDTO updateSubscription(@RequestBody SubscriptionDTO subscriptionDTO) {
        Subscription subscription = convertToEntity(subscriptionDTO);
        Subscription updatedSubscription = subscriptionServices.updateSubscription(subscription);
        return convertToDTO(updatedSubscription);
    }

    @Operation(description = "Retrieve Subscriptions created between two dates")
    @GetMapping("/all/{date1}/{date2}")
    public List<SubscriptionDTO> getSubscriptionsByDates(@PathVariable("date1") LocalDate startDate,
                                                         @PathVariable("date2") LocalDate endDate) {
        return subscriptionServices.retrieveSubscriptionsByDates(startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private SubscriptionDTO convertToDTO(Subscription subscription) {
        SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
        subscriptionDTO.setNumSub(subscription.getNumSub());
        subscriptionDTO.setStartDate(subscription.getStartDate());
        subscriptionDTO.setEndDate(subscription.getEndDate());
        subscriptionDTO.setPrice(subscription.getPrice());
        subscriptionDTO.setTypeSub(subscription.getTypeSub());
        return subscriptionDTO;
    }

    private Subscription convertToEntity(SubscriptionDTO subscriptionDTO) {
        Subscription subscription = new Subscription();
        subscription.setNumSub(subscriptionDTO.getNumSub());
        subscription.setStartDate(subscriptionDTO.getStartDate());
        subscription.setEndDate(subscriptionDTO.getEndDate());
        subscription.setPrice(subscriptionDTO.getPrice());
        subscription.setTypeSub(subscriptionDTO.getTypeSub());
        return subscription;
    }
}