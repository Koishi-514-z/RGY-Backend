package org.example.rgybackend.Entity;

import java.util.ArrayList;
import java.util.List;

import org.example.rgybackend.Model.TagModel;
import org.example.rgybackend.Model.UrlDataModel;

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
@Table(name = "urldata")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PushContent {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "dataid")
    private Long dataid;

    @Basic
    @Column(name = "type")
    private String type;

    @Basic
    @Column(name = "title")
    private String title;

    @Basic
    @Column(name = "img")
    private String img;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "url")
    private String url;
    
    @Basic
    @Column(name = "tags")
    private String tags;

    @Basic
    @Column(name = "createdAt")
    private Long createdAt;

    private String toStringFromTags(List<TagModel> tags) {
        if(tags == null || tags.isEmpty()) {
            return "";
        }
        List<String> strList = new ArrayList<>();
        for(TagModel tagModel : tags) {
            strList.add(String.valueOf(tagModel.getId()));
        }
        return String.join("|", strList);
    }

    public PushContent(UrlDataModel urlDataModel) {
        this.dataid = null;
        this.type = urlDataModel.getType();
        this.title = urlDataModel.getTitle();
        this.img = urlDataModel.getImg();
        this.description = urlDataModel.getDescription();
        this.url = urlDataModel.getUrl();
        this.createdAt = urlDataModel.getCreatedAt();
        this.tags = toStringFromTags(urlDataModel.getTags());
    }
}
