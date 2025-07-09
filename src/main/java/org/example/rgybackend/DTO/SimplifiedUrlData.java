package org.example.rgybackend.DTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.example.rgybackend.Model.TagModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimplifiedUrlData {
    private String type;
    private String title;
    private String description;
    private String url;
    private List<TagModel> tags;
    private Long createdAt;

    public final List<TagModel> typeTags = new ArrayList<>(
        Arrays.asList(
            new TagModel(1L, "正能量"),
            new TagModel(2L, "励志鸡汤"),
            new TagModel(3L, "情绪调节"),
            new TagModel(4L, "压力缓解"),
            new TagModel(5L, "睡眠改善"),
            new TagModel(6L, "运动健身"),
            new TagModel(7L, "冥想放松"),
            new TagModel(8L, "音乐疗愈"),
            new TagModel(10L, "美食治愈"),
            new TagModel(11L, "旅行散心"),
            new TagModel(12L, "艺术创作"),
            new TagModel(13L, "社交互动"),
            new TagModel(14L, "学习成长"),
            new TagModel(16L, "亲情友情"),
            new TagModel(17L, "爱情感悟"),
            new TagModel(18L, "自我关爱"),
            new TagModel(19L, "心理科普"),
            new TagModel(20L, "生活技巧"),
            new TagModel(23L, "趣味娱乐"),
            new TagModel(24L, "温暖故事"),
            new TagModel(25L, "其他")
        )
    );

    private List<String> fromStringToList(String str) {
        if(str == null || str.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(str.split("\\|"));
    }

    public SimplifiedUrlData(PushContentDTO pushContent) {
        this.type = pushContent.getType();
        this.title = pushContent.getTitle();
        this.description = pushContent.getDescription();
        this.url = pushContent.getUrl();
        this.createdAt = pushContent.getCreatedAt();
        this.tags = new ArrayList<>();
    
        List<String> list = fromStringToList(pushContent.getTags());
        for(TagModel tagModel : typeTags) {
            for(String tagid : list) {
                if(tagModel.getId() == Long.parseLong(tagid)) {
                    this.tags.add(tagModel);
                    break;
                }
            }
        }
    }
}
