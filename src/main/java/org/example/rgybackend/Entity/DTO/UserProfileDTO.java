package org.example.rgybackend.Entity.DTO;

import org.example.rgybackend.Entity.Profile;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    @Id
    @Column(name = "userid")
    private String userid;

    @Basic
    @Column(name = "username")
    private String username;

    @Basic
    @Column(name = "email")
    private String email;

    @Basic
    @Column(name = "avatar")
    private String avatar;

    @Basic
    @Column(name = "note")
    private String note;

    public UserProfileDTO(Profile profile) {
        this.userid = profile.getUserid();
        this.username = profile.getUsername();
        this.email = profile.getEmail();
        this.avatar = profile.getAvatar();
        this.note = profile.getNote();
    }
}
