package org.example.rgybackend.Model;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IllegalModel {
    private int illegalid;
    private Long contentid;
    private SimplifiedProfileModel user;
    private String title;
    private String content;
    private int type;
    private String reason;
    private Long timestamp;
    private int status;


}
