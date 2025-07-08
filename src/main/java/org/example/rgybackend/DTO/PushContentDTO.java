package org.example.rgybackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PushContentDTO {
    private String type;
    private String title;
    private String description;
    private String url;
    private String tags;
    private Long createdAt;
}
