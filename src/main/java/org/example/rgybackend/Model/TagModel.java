package org.example.rgybackend.Model;

import org.example.rgybackend.Entity.Tag;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagModel {
    private Long id;
    private String content;

    public TagModel(Tag tag) {
        this.id = tag.getId();
        this.content = tag.getContent();
    }
}
