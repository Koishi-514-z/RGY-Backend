package org.example.rgybackend.Model;

import org.example.rgybackend.DTO.SessionTagDTO;

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

    public SessionTagModel(SessionTagDTO sessionTagDTO) {
        this.sessionid = sessionTagDTO.getSessionid();
        this.myself = this.other = null;
        this.timestamp = sessionTagDTO.getTimestamp();
        this.unread = sessionTagDTO.getUnread();
    }
}
