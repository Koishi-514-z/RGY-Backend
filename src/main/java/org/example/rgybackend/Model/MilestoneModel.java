package org.example.rgybackend.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.example.rgybackend.DTO.MilestoneData;
import org.example.rgybackend.Entity.Milestone;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MilestoneModel {
    private String userid;
    
    private List<MilestoneData> milestones = new ArrayList<>(
        Arrays.asList(
            new MilestoneData(1L, "register", "加入社区", "欢迎来到心理健康互助社区！", null),
            new MilestoneData(2L, "first_post", "首次发帖", "分享了第一条动态，开始你的分享之旅", null),
            new MilestoneData(3L, "first_like", "首次获赞", "你的分享获得了第一个赞！", null),
            new MilestoneData(4L, "positive_week", "积极情绪周", "连续一周保持积极情绪，真棒！", null),
            new MilestoneData(5L, "first_message", "首次私信", "与其他用户建立了私信联系", null),
            new MilestoneData(6L, "counseling", "心理咨询", "预约了第一次专业心理咨询", null),
            new MilestoneData(7L, "active_member", "活跃成员", "成为社区活跃成员，影响力不断提升", null)
        )
    );

    public MilestoneModel(String userid, List<Milestone> milestoneList) {
        this.userid = userid;

        for(MilestoneData milestoneData : milestones) {
            Optional<Milestone> milestoneOptional = milestoneList.stream()
                    .filter(m -> m.getMilestoneid() == milestoneData.getMilestoneid())
                    .findFirst();
            if(milestoneOptional.isPresent()) {
                milestoneData.setTimestamp(milestoneOptional.get().getTimestamp());
            }
        }
    }
}
