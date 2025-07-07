package org.example.rgybackend.Repository;

import java.util.List;

import org.example.rgybackend.Entity.Available;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AvailableRepository extends JpaRepository<Available, Long> {
    boolean existsByPsyidAndDateAndHour(String psyid, String date, Long hour);

    List<Available> findByPsyid(String psyid);

    int deleteByPsyid(String psyid);

    @Query("SELECT a.hour FROM Available a WHERE a.psyid = :psyid AND a.date = :date")
    List<Long> findAvailableTimes(String psyid, String date);
}
