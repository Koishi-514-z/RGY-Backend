package org.example.rgybackend.DAO;

import org.example.rgybackend.Model.QuoteModel;

public interface QuoteDAO {
    QuoteModel getQuote();

    boolean addQuote(QuoteModel quoteModel, Long timestamp);
}
