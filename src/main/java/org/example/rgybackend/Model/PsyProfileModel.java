package org.example.rgybackend.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.example.rgybackend.Entity.PsyProfileExtra;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PsyProfileModel {
    private String userid;
    private String username;
    private String email;
    private String avatar;
    private Long role;
    private String phone;
    private String title;
    private String license;

    private List<TagModel> specialty;
    private String education;
    private List<TagModel> certifications;
    private Long workingYears;

    private Long totalClients;
    private Double avgScore;
    private Long commentNum;
    private Double successRate;

    private String consultationFee;
    private String location;
    private String workingTime;
    private String responseTime;

    private String introduction;
    private List<String> achievements;

    public final static List<TagModel> specialtyTags = new ArrayList<>(
        Arrays.asList(
            new TagModel(1L, "情感咨询"),
            new TagModel(2L, "焦虑抑郁"),
            new TagModel(3L, "职场压力"),
            new TagModel(4L, "亲子关系"),
            new TagModel(5L, "婚姻家庭"),
            new TagModel(6L, "青少年心理"),
            new TagModel(7L, "创伤治疗"),
            new TagModel(8L, "强迫症"),
            new TagModel(9L, "恐惧症"),
            new TagModel(10L, "人际关系"),
            new TagModel(11L, "职业规划"),
            new TagModel(12L, "学习障碍"),
            new TagModel(13L, "行为问题"),
            new TagModel(14L, "情绪管理"),
            new TagModel(15L, "性心理")
        )
    );

    public final static List<TagModel> certificationTags = new ArrayList<>(
        Arrays.asList(
            new TagModel(1L, "国家二级心理咨询师"),
            new TagModel(2L, "国家三级心理咨询师"),
            new TagModel(3L, "认知行为治疗师"),
            new TagModel(4L, "家庭治疗师"),
            new TagModel(5L, "沙盘游戏治疗师"),
            new TagModel(6L, "艺术治疗师"),
            new TagModel(7L, "催眠治疗师"),
            new TagModel(8L, "精神分析师"),
            new TagModel(9L, "人本主义治疗师"),
            new TagModel(10L, "EMDR治疗师")
        )
    );

    private List<String> fromStringToList(String str) {
        if(str == null || str.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(str.split("|"));
    }

    public PsyProfileModel(PsyProfileExtra entity) {
        this.userid = entity.getUserid();
        this.role = 2L;
        this.phone = entity.getPhone();
        this.title = entity.getTitle();
        this.license = entity.getLicense();
        this.education = entity.getEducation();
        this.workingYears = entity.getWorkingYears();
        this.totalClients = entity.getTotalClients();
        this.commentNum = entity.getCommentNum();
        this.consultationFee = entity.getConsultationFee();
        this.location = entity.getLocation();
        this.workingTime = entity.getWorkingTime();
        this.responseTime = entity.getResponseTime();
        this.introduction = entity.getIntroduction();
        this.achievements = fromStringToList(entity.getAchievements());

        if(entity.getCommentNum() > 0) {
            this.avgScore = entity.getTotalScore() * 1.0 / entity.getCommentNum();
            this.successRate = entity.getSuccessNum() * 1.0 / entity.getCommentNum();
        }

        List<String> specialtyList = fromStringToList(entity.getSpecialty());
        this.specialty = new ArrayList<>();
        for(TagModel tag : specialtyTags) {
            for(String id : specialtyList) {
                if(tag.getId() == Long.parseLong(id)) {
                    this.specialty.add(tag);
                    break;
                }
            }
        }

        List<String> certificationList = fromStringToList(entity.getCertifications());
        this.certifications = new ArrayList<>();
        for(TagModel tag : certificationTags) {
            for(String id : certificationList) {
                if(tag.getId() == Long.parseLong(id)) {
                    this.certifications.add(tag);
                    break;
                }
            }
        }
    }
}
