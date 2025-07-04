package org.example.rgybackend.Model;

import org.example.rgybackend.Entity.Quote;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuoteModel {
    private String text;
    private String author;

    public QuoteModel(Quote quote) {
        this.text = quote.getText();
        this.author = quote.getAuthor();
    }
}
