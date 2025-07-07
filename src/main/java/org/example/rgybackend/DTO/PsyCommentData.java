package org.example.rgybackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PsyCommentData {
    private String userid;
    private String psyid;
    private boolean success;
    private Long score;
}
