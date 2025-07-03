package org.example.rgybackend.Repository;

import org.example.rgybackend.Entity.Counseling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CounselingRepository extends JpaRepository<Counseling, Long> {
    List<Counseling> findByUserid(String userid);

    List<Counseling> findByPsyid(String psyid);

    @Query("SELECT c FROM Counseling c WHERE c.psyid = :psyid AND c.timestamp >= :start AND c.timestamp < :end")
    List<Counseling> scanCounseling(String psyid, Long start, Long end);

    boolean existsByPsyidAndTimestamp(String psyid, Long timestamp);
}
