package tn.esprit.spring.entities;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import tn.esprit.spring.entities.Color;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.entities.Skier;

import java.util.HashSet;
import java.util.Set;

class PisteTest {

    @Test
    void testPisteGettersAndSetters() {
        Piste piste = new Piste();

        // Test numPiste
        piste.setNumPiste(1L);
        assertEquals(1L, piste.getNumPiste());

        // Test namePiste
        piste.setNamePiste("Test Piste");
        assertEquals("Test Piste", piste.getNamePiste());

        // Test color
        Color color = Color.GREEN; // Use actual enum value
        piste.setColor(color);
        assertEquals(color, piste.getColor());

        // Test length
        piste.setLength(1000);
        assertEquals(1000, piste.getLength());

        // Test slope
        piste.setSlope(30);
        assertEquals(30, piste.getSlope());

        // Test skiers
        Set<Skier> skiers = new HashSet<>();
        piste.setSkiers(skiers);
        assertEquals(skiers, piste.getSkiers());
    }
}