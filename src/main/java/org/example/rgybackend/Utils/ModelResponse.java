package org.example.rgybackend.Utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelResponse {
    private String text;
    private Long predicted_class;
    private Double confidence;
}
