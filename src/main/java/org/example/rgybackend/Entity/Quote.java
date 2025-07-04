package org.example.rgybackend.Entity;

import org.example.rgybackend.Model.QuoteModel;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quote")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quote {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "quoteid")
    private Long quoteid;

    @Basic
    @Column(name = "text")
    private String text;

    @Basic
    @Column(name = "author")
    private String author;

    @Basic
    @Column(name = "timestamp")
    private Long timestamp;

    public Quote(QuoteModel quoteModel, Long timestamp) {
        this.quoteid = null;
        this.text = quoteModel.getText();
        this.author = quoteModel.getAuthor();
        this.timestamp = timestamp;
    }
}
