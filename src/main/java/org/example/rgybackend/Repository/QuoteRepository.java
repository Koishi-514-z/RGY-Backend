package org.example.rgybackend.Repository;

import org.example.rgybackend.Entity.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuoteRepository extends JpaRepository<Quote, Long> {
    @Query(value = "SELECT * FROM quote ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Quote findRandomQuote();
}
