package org.example.rgybackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MilestoneData {
    private Long milestoneid;
    private String type;
    private String title;
    private String description;
    private Long timestamp;
}
