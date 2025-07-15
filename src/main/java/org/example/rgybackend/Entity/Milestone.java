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
@Table(name = "milestone")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Milestone {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "userid")
    private String userid;

    @Basic
    @Column(name = "milestoneid")
    private Long milestoneid;

    @Basic
    @Column(name = "timestamp")
    private Long timestamp;
}
