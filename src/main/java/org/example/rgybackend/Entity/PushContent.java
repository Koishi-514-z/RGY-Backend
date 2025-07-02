package org.example.rgybackend.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PushContent {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long pcid;
    private String type;
    private String title;
    private String img;
    private String description;
    private String url;
    private Integer emotagid ;
    private Timestamp createdAt;
}
