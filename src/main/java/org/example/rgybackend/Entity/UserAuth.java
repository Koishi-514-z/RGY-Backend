package org.example.rgybackend.Entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_auth")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAuth {
    @Id
    @Column(name = "userid")
    private String userid;

    @Basic
    @Column(name = "stuid")
    private String stuid;

    @Basic
    @Column(name = "password")
    private String password;

    @Basic 
    @Column(name = "disabled")
    private boolean disabled;
}
