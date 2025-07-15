package org.example.rgybackend.Repository;

import org.example.rgybackend.Entity.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface MilestoneRepository extends JpaRepository<Milestone, Long> {
    List<Milestone> findByUserid(String userid);

    boolean existsByUseridAndMilestoneid(String userid, Long milestoneid);
}
