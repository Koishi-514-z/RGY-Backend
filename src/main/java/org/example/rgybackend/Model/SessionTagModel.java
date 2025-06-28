package org.example.rgybackend.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionTagModel {
    private Long sessionid;
    private SimplifiedProfileModel myself;
    private SimplifiedProfileModel other;
    private Long timestamp;
    private Long unread;
}
