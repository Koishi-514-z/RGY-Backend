package org.example.rgybackend.Model;

import org.example.rgybackend.Entity.UserProfile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileModel {
    private String userid;
    private String username;
    private String email;
    private String avatar;
    private String note;
    private Long role;
    private Long jointime;

    public ProfileModel(UserProfile userProfile) {
        this.userid = userProfile.getUserid();
        this.username = userProfile.getUsername();
        this.email = userProfile.getEmail();
        this.avatar = userProfile.getAvatar();
        this.note = userProfile.getNote();
        this.role = userProfile.getRole();
        this.jointime = userProfile.getJointime();
    }
}
