package org.example.rgybackend.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "message")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "messageid")
    private Long messageid;

    @Basic
    @Column(name = "sessionid")
    private Long sessionid;

    @Basic
    @Column(name = "fromuserid")
    private String fromuserid;

    @Basic
    @Column(name = "touserid")
    private String touserid;

    @Basic
    @Column(name = "timestamp")
    private Long timestamp;

    @Basic
    @Column(name = "content")
    private String content;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sessionid", referencedColumnName = "sessionid", insertable=false, updatable=false)
    private Session session;
}
