package org.example.rgybackend.Entity;

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
}
