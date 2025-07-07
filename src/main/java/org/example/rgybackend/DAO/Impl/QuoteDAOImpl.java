package org.example.rgybackend.DAO.Impl;

import org.example.rgybackend.DAO.QuoteDAO;
import org.example.rgybackend.Entity.Quote;
import org.example.rgybackend.Model.QuoteModel;
import org.example.rgybackend.Repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class QuoteDAOImpl implements QuoteDAO {
    @Autowired
    private QuoteRepository quoteRepository;

    @Override
    public QuoteModel getQuote() {
        if(quoteRepository.count() == 0) {
            return new QuoteModel(null, null);
        }
        Quote quote = quoteRepository.findRandomQuote();
        if (quote == null) {
            return new QuoteModel(null, null);
        }
        return new QuoteModel(quote);
    }

    @Override
    public boolean addQuote(QuoteModel quoteModel, Long timestamp) {
        Quote quote = new Quote(quoteModel, timestamp);
        quoteRepository.save(quote);
        return true;
    }
}
