package org.example.rgybackend.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CounselingModel {
    private Long counselingid;
    private String userid;
    private String psyid;
    private Long timestamp;
}
