package org.example.rgybackend.DTO;

import org.example.rgybackend.Model.TagModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoodData {
    private TagModel tag;
    private Long total;
    private Double percent;
}
