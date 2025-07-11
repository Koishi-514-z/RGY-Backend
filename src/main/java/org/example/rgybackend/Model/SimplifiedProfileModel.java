package org.example.rgybackend.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimplifiedProfileModel {
    private String userid;
    private String username;
    private String avatar;
    private String note;
    private Long jointime;

    public SimplifiedProfileModel(ProfileModel profileModel) {
        this.userid = profileModel.getUserid();
        this.username = profileModel.getUsername();
        this.avatar = profileModel.getAvatar();
        this.note = profileModel.getNote();
        this.jointime = profileModel.getJointime();
    }
}
