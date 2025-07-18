package org.example.rgybackend.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.rgybackend.DTO.ProfileTag;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IllegalModel {
    private int illegalid;
    private Long contentid;
    private ProfileTag user;
    private String title;
    private String content;
    private int type;
    private String reason;
    private Long timestamp;
    private int status;
    private Long blogid;

}
