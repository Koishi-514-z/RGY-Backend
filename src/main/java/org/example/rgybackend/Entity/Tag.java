package org.example.rgybackend.Entity;

import org.example.rgybackend.Model.TagModel;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tag")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    @Id
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "content")
    private String content;

    public Tag(TagModel tagModel) {
        this.id = tagModel.getId();
        this.content = tagModel.getContent();
    }
}
