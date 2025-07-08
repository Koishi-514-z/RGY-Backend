package org.example.rgybackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeData {
    private Long timeIndex;
    private Long total;
    private Double score;
    private Long positive;
    private Long neutral;
    private Long negative;
}
