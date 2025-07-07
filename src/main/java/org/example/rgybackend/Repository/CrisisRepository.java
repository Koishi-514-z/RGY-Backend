package org.example.rgybackend.Repository;

import org.example.rgybackend.Entity.Crisis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CrisisRepository extends JpaRepository<Crisis, Integer> {

    List<Crisis> findAllByUserid(String userid);
}
