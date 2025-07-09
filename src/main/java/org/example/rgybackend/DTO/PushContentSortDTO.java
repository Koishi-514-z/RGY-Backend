package org.example.rgybackend.DTO;

import org.example.rgybackend.Model.UrlDataModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PushContentSortDTO {
    private UrlDataModel urlDataModel;
    private double rate;
}
