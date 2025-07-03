package org.example.rgybackend.Model;

import org.example.rgybackend.Entity.PushContent;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlDataModel {
    private String type;
    private String title;
    private String img;
    private String description;
    private String url;
    private Integer emotagid;

    public UrlDataModel(PushContent pushContent) {
        this.type = pushContent.getType();
        this.title = pushContent.getTitle();
        this.img = pushContent.getImg();
        this.description = pushContent.getDescription();
        this.url = pushContent.getUrl();
        this.emotagid = pushContent.getEmotagid();
    }
}
