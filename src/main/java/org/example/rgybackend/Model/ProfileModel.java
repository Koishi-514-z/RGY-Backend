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

    public ProfileModel(UserProfile userProfileDTO) {
        this.userid = userProfileDTO.getUserid();
        this.username = userProfileDTO.getUsername();
        this.email = userProfileDTO.getEmail();
        this.avatar = userProfileDTO.getAvatar();
        this.note = userProfileDTO.getNote();
        this.role = userProfileDTO.getRole();
    }
}
