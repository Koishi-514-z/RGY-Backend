package org.example.rgybackend.Repository;

import org.example.rgybackend.Entity.Illegal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IllegalRepository extends JpaRepository<Illegal, Integer> {
    List<Illegal> findAllByStatus(int status);
    List<Illegal> findAllByType(int type);

    Illegal findByIllegalid(int id);
}
