package org.example.rgybackend.Entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "urldata")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PushContent {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "dataid")
    private Long dataid;

    @Basic
    @Column(name = "type")
    private String type;

    @Basic
    @Column(name = "title")
    private String title;

    @Basic
    @Column(name = "img")
    private String img;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "url")
    private String url;
    
    @Basic
    @Column(name = "emotagid")
    private Integer emotagid;

    @Basic
    @Column(name = "createdAt")
    private Long createdAt;
}
