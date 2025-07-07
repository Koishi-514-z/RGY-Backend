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
@Table(name = "available")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Available {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "availableid")
    private Long availableid;

    @Basic
    @Column(name = "psyid")
    private String psyid;

    @Basic
    @Column(name = "date")
    private String date;

    @Basic
    @Column(name = "hour")
    private Long hour;
}
