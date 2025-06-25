package org.example.rgybackend.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private Long messageid;
    private Long role;
    private Long timestamp;
    private String content;
}
