package org.example.rgybackend.Model;

import org.example.rgybackend.Entity.Crisis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrisisModel {
    private Integer crisisid;
    private String userid;
    private SimplifiedProfileModel user;
    private String content;
    private Long timestamp;
    private Long urgencyLevel;
    private Long status;
}
