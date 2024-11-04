package tn.esprit.spring.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import tn.esprit.spring.entities.Color;

class PisteDTOTest {

    @Test
    void testPisteDTOGettersAndSetters() {
        PisteDTO pisteDTO = new PisteDTO();

        pisteDTO.setNumPiste(1L);
        assertEquals(1L, pisteDTO.getNumPiste());

        pisteDTO.setNamePiste("Test Piste");
        assertEquals("Test Piste", pisteDTO.getNamePiste());

        Color color = Color.GREEN;
        pisteDTO.setColor(color);
        assertEquals(color, pisteDTO.getColor());

        pisteDTO.setLength(1000);
        assertEquals(1000, pisteDTO.getLength());

        pisteDTO.setSlope(30);
        assertEquals(30, pisteDTO.getSlope());
    }
}