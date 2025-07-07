package org.example.rgybackend.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.example.rgybackend.Entity.Counseling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CounselingModel {
    private Long counselingid;
    private String psyid;
    private Long timestamp;
    private Long status;
    private TagModel type;

    public final List<TagModel> typeTags = new ArrayList<>(
        Arrays.asList(
            new TagModel(1L, "学业压力"),
            new TagModel(2L, "考试焦虑"),
            new TagModel(3L, "人际关系"),
            new TagModel(4L, "恋爱情感"),
            new TagModel(5L, "宿舍关系"),
            new TagModel(6L, "就业焦虑"),
            new TagModel(7L, "家庭关系"),
            new TagModel(8L, "自我认知"),
            new TagModel(9L, "情绪管理"),
            new TagModel(10L, "适应困难"),
            new TagModel(11L, "社交恐惧"),
            new TagModel(12L, "抑郁情绪"),
            new TagModel(13L, "焦虑情绪"),
            new TagModel(14L, "睡眠问题"),
            new TagModel(15L, "网络成瘾"),
            new TagModel(20L, "生涯规划"),
            new TagModel(22L, "创伤修复"),
            new TagModel(23L, "饮食障碍"),
            new TagModel(24L, "身体形象"),
            new TagModel(25L, "其他")
        )
    );

    public CounselingModel(Counseling counseling) {
        this.counselingid = counseling.getCounselingid();
        this.psyid = counseling.getPsyid();
        this.timestamp = counseling.getTimestamp();
        this.status = counseling.getStatus();
        for(TagModel tag : typeTags) {
            if(tag.getId() == counseling.getType()) {
                this.type = tag;
                break;
            }
        }
    }
}
