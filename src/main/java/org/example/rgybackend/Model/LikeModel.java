package org.example.rgybackend.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeModel {
    private Long likeid;
    private String fromuserid;
    private String touserid;
    private Long blogid;
    private Long timestamp;
}
