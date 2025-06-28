package org.example.rgybackend.Entity;

import org.example.rgybackend.Model.ProfileModel;

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
public class UserProfile {
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

    @Basic
    @Column(name = "role")
    private Long role;

    public UserProfile(ProfileModel profile) {
        this.userid = profile.getUserid();
        this.username = profile.getUsername();
        this.email = profile.getEmail();
        this.avatar = profile.getAvatar();
        this.note = profile.getNote();
        this.role = profile.getRole();
    }
}
