package tn.esprit.spring.entities;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

class SubscriptionTest {

    @Test
    void testSubscriptionGettersAndSetters() {
        Subscription subscription = new Subscription();

        // Test numSub
        subscription.setNumSub(1L);
        assertEquals(1L, subscription.getNumSub());

        // Test startDate
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        subscription.setStartDate(startDate);
        assertEquals(startDate, subscription.getStartDate());

        // Test endDate
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        subscription.setEndDate(endDate);
        assertEquals(endDate, subscription.getEndDate());

        // Test price
        subscription.setPrice(99.99f);
        assertEquals(99.99f, subscription.getPrice());

        // Test typeSub
        TypeSubscription typeSub = TypeSubscription.ANNUAL; // Use actual enum value
        subscription.setTypeSub(typeSub);
        assertEquals(typeSub, subscription.getTypeSub());
    }
}