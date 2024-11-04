package tn.esprit.spring.dto;

import lombok.Data;
import tn.esprit.spring.entities.Color;

@Data
public class PisteDTO {
    private Long numPiste;
    private String namePiste;
    private Color color;
    private int length;
    private int slope;
}