package org.example.rgybackend.Repository;

import org.example.rgybackend.Entity.PushContent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PushContentRepository extends JpaRepository<PushContent, Long> {
    Page<PushContent> findAllByEmotagidOrderByCreatedAtDesc(Integer emotagid, Pageable pageable);

    Page<PushContent> findAllOrderByCreatedAtDesc(Pageable pageable);

    Long countByEmotagid(Integer emotagid);
}
