package org.example.rgybackend.Model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Session {
    private Long sessionid;
    private SimplifiedProfile myself;
    private SimplifiedProfile other;
    private Long timestamp;
    private List<Message> messages;
}
