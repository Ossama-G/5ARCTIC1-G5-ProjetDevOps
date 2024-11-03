package tn.esprit.spring.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import tn.esprit.spring.entities.TypeSubscription;

class SubscriptionDTOTest {

    @Test
    void testSubscriptionDTOGettersAndSetters() {
        SubscriptionDTO subscriptionDTO = new SubscriptionDTO();

        subscriptionDTO.setNumSub(1L);
        assertEquals(1L, subscriptionDTO.getNumSub());

        LocalDate startDate = LocalDate.of(2023, 1, 1);
        subscriptionDTO.setStartDate(startDate);
        assertEquals(startDate, subscriptionDTO.getStartDate());

        LocalDate endDate = LocalDate.of(2023, 12, 31);
        subscriptionDTO.setEndDate(endDate);
        assertEquals(endDate, subscriptionDTO.getEndDate());

        subscriptionDTO.setPrice(99.99f);
        assertEquals(99.99f, subscriptionDTO.getPrice());

        TypeSubscription typeSub = TypeSubscription.ANNUAL;
        subscriptionDTO.setTypeSub(typeSub);
        assertEquals(typeSub, subscriptionDTO.getTypeSub());
    }
}