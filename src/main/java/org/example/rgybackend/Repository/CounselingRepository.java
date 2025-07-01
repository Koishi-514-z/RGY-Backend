package org.example.rgybackend.Repository;

import org.example.rgybackend.Entity.Counseling;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface CounselingRepository extends JpaRepository<Counseling, Long> {
    List<Counseling> findByUserid(String userid);

    List<Counseling> findByPsyid(String psyid);

    
}
