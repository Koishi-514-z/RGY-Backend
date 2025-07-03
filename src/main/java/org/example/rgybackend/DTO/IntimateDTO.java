package org.example.rgybackend.DTO;

import org.example.rgybackend.Model.SimplifiedProfileModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntimateDTO {
    Double intimateScre;
    SimplifiedProfileModel intimateProfile;
}
