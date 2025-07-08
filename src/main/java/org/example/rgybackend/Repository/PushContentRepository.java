package org.example.rgybackend.Repository;

import java.util.List;

import org.example.rgybackend.DTO.PushContentDTO;
import org.example.rgybackend.Entity.PushContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PushContentRepository extends JpaRepository<PushContent, Long> {
    @Query("SELECT new org.example.rgybackend.DTO.PushContentDTO(p.type, p.title, p.description, p.url, p.tags, p.createdAt) FROM PushContent p")
    List<PushContentDTO> findSimplifiedDatas();
}
