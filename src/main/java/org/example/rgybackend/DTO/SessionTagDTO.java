package org.example.rgybackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionTagDTO {
    private Long sessionid;
    private String userAid;
    private String userBid;
    private Long timestamp;
    private Long unreadA;
    private Long unreadB;
}
