package org.example.rgybackend.Entity;

import org.example.rgybackend.Entity.DTO.UserProfileDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    private String userid;
    private String username;
    private String email;
    private String avatar;
    private String note;

    public Profile(UserProfileDTO userProfileDTO) {
        this.userid = userProfileDTO.getUserid();
        this.username = userProfileDTO.getUsername();
        this.email = userProfileDTO.getEmail();
        this.avatar = userProfileDTO.getAvatar();
        this.note = userProfileDTO.getNote();
    }
}
