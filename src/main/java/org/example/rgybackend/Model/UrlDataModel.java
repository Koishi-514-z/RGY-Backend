package org.example.rgybackend.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlDataModel {
//    private Long urlid;
    private String type;
    private String title;
    private String img;
    private String description;
    private String url;
    private Integer emotagid ;
}
