package org.example.rgybackend.Utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocketMessage {
    private String sender;
    private Long id;
    private String fromuserid;
    private String touserid;
    private Long timestamp;
    private String content;
}
