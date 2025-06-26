package org.example.rgybackend.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionTag {
    private Long sessionid;
    private SimplifiedProfile myself;
    private SimplifiedProfile other;
    private Long timestamp;
}
