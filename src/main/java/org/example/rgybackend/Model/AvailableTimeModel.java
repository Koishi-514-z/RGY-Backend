package org.example.rgybackend.Model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableTimeModel {
    private String psyid;
    private List<Long> monday;
    private List<Long> tuesday;
    private List<Long> wednesday;
    private List<Long> thursday;
    private List<Long> friday;
    private List<Long> saturday;
    private List<Long> sunday;
}
