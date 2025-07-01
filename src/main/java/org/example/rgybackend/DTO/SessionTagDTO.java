package org.example.rgybackend.DTO;

import org.example.rgybackend.Model.SessionTagModel;

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
    private Long unread;

    public SessionTagDTO(SessionTagModel sessionTagModel) {
        this.sessionid = sessionTagModel.getSessionid();
        this.userAid = sessionTagModel.getMyself().getUserid();
        this.userBid = sessionTagModel.getOther().getUserid();
        this.timestamp = sessionTagModel.getTimestamp();
        this.unread = sessionTagModel.getUnread();
    }
}
