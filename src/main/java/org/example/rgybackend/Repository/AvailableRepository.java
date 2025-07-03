package org.example.rgybackend.Repository;

import java.util.List;

import org.example.rgybackend.Entity.Available;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AvailableRepository extends JpaRepository<Available, Long> {
    List<Available> findByPsyid(String psyid);

    boolean deleteByPsyid(String psyid);

    boolean deleteByPsyidAndTimestamp(String psyid, Long timestamp);

    boolean existsByPsyidAndTimestamp(String psyid, Long timestamp);

    @Query("SELECT a.timestamp FROM Available a WHERE a.psyid = :psyid")
    List<Long> findAvailableTimes(String psyid);
}
