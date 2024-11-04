package tn.esprit.spring.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class SkierDTO {
    private Long numSkier;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String city;
    private Long subscriptionId; // Ajout de l'ID de l'abonnement pour éviter d'exposer tout l'objet Subscription
    // Vous pouvez ajouter d'autres champs nécessaires
}