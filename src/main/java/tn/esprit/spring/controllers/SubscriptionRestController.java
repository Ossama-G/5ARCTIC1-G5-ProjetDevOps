package tn.esprit.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    @Operation(description = "Add Subscription")
    @PostMapping("/add")
    public SubscriptionDTO addSubscription(@RequestBody SubscriptionDTO subscriptionDTO) {
        Subscription subscription = modelMapper.map(subscriptionDTO, Subscription.class);
        Subscription savedSubscription = subscriptionServices.addSubscription(subscription);
        return modelMapper.map(savedSubscription, SubscriptionDTO.class);
    }

    @Operation(description = "Retrieve Subscription by Id")
    @GetMapping("/get/{id-subscription}")
    public SubscriptionDTO getById(@PathVariable("id-subscription") Long numSubscription) {
        Subscription subscription = subscriptionServices.retrieveSubscriptionById(numSubscription);
        return modelMapper.map(subscription, SubscriptionDTO.class);
    }

    @Operation(description = "Retrieve Subscriptions by Type")
    @GetMapping("/all/{typeSub}")
    public Set<SubscriptionDTO> getSubscriptionsByType(@PathVariable("typeSub") TypeSubscription typeSubscription) {
        Set<Subscription> subscriptions = subscriptionServices.getSubscriptionByType(typeSubscription);
        return subscriptions.stream()
                .map(subscription -> modelMapper.map(subscription, SubscriptionDTO.class))
                .collect(Collectors.toSet());
    }

    @Operation(description = "Update Subscription")
    @PutMapping("/update")
    public SubscriptionDTO updateSubscription(@RequestBody SubscriptionDTO subscriptionDTO) {
        Subscription subscription = modelMapper.map(subscriptionDTO, Subscription.class);
        Subscription updatedSubscription = subscriptionServices.updateSubscription(subscription);
        return modelMapper.map(updatedSubscription, SubscriptionDTO.class);
    }

    @Operation(description = "Retrieve Subscriptions created between two dates")
    @GetMapping("/all/{date1}/{date2}")
    public List<SubscriptionDTO> getSubscriptionsByDates(@PathVariable("date1") LocalDate startDate,
                                                         @PathVariable("date2") LocalDate endDate) {
        List<Subscription> subscriptions = subscriptionServices.retrieveSubscriptionsByDates(startDate, endDate);
        return subscriptions.stream()
                .map(subscription -> modelMapper.map(subscription, SubscriptionDTO.class))
                .collect(Collectors.toList());
    }
}
