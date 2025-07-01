package org.example.rgybackend.Entity;

import java.util.List;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "session")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Session {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "sessionid")
    private Long sessionid;

    @Basic
    @Column(name = "userAid")
    private String userAid;

    @Basic
    @Column(name = "userBid")
    private String userBid;

    @Basic
    @Column(name = "timestamp")
    private Long timestamp;

    @Basic
    @Column(name = "unread")
    private Long unread;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Message> messages;
}
