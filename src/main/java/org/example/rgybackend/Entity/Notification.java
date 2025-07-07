package org.example.rgybackend.Entity;


import jakarta.persistence.*;
import lombok.*;
import org.example.rgybackend.Model.BlogModel;

@Entity
@Table(name = "notification")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @Column(name = "notificationid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer notificationid;

    @Basic
    @Column(name = "title")
    private String title;

    @Basic
    @Column(name = "content")
    private String content;

    @Basic
    @Column(name = "timestamp")
    private Long timestamp;

    @Basic
    @Column(name = "adminid")
    private String adminid;

    @Basic
    @Column(name = "priority")
    private Integer priority;



}
