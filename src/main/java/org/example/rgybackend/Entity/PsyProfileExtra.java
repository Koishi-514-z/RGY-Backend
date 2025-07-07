package org.example.rgybackend.Entity;

import java.util.ArrayList;
import java.util.List;

import org.example.rgybackend.Model.PsyProfileModel;
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
@Table(name = "psyextra")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PsyProfileExtra {
    @Id
    @Column(name = "userid")
    private String userid;

    @Basic
    @Column(name = "phone")
    private String phone;

    @Basic
    @Column(name = "title")
    private String title;

    @Basic
    @Column(name = "license")
    private String license;

    @Basic
    @Column(name = "specialty")
    private String specialty;

    @Basic
    @Column(name = "education")
    private String education;

    @Basic
    @Column(name = "certifications")
    private String certifications;

    @Basic
    @Column(name = "workingYears")
    private Long workingYears;

    @Basic
    @Column(name = "totalClients")
    private Long totalClients;

    @Basic
    @Column(name = "totalScore")
    private Long totalScore;

    @Basic
    @Column(name = "successNum")
    private Long successNum;

    @Basic
    @Column(name = "commentNum")
    private Long commentNum;

    @Basic
    @Column(name = "consultationFee")
    private String consultationFee;

    @Basic
    @Column(name = "location")
    private String location;

    @Basic
    @Column(name = "workingTime")
    private String workingTime;

    @Basic
    @Column(name = "responseTime")
    private String responseTime;

    @Basic
    @Column(name = "introduction")
    private String introduction;

    @Basic
    @Column(name = "achievements")
    private String achievements;

    private String toStringFromList(List<String> list) {
        if(list == null || list.isEmpty()) {
            return "";
        }
        return String.join("|", list);
    }

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

    public PsyProfileExtra(PsyProfileModel model) {
        this.userid = model.getUserid();
        this.phone = model.getPhone();
        this.title = model.getTitle();
        this.license = model.getLicense();
        this.specialty = toStringFromTags(model.getSpecialty());
        this.education = model.getEducation();
        this.certifications = toStringFromTags(model.getCertifications());
        this.workingYears = model.getWorkingYears();
        this.consultationFee = model.getConsultationFee();
        this.location = model.getLocation();
        this.workingTime = model.getWorkingTime();
        this.responseTime = model.getResponseTime();
        this.introduction = model.getIntroduction();
        this.achievements = toStringFromList(model.getAchievements());
    }

    public PsyProfileExtra(String userid) {
        this.userid = userid;
        this.phone = "";
        this.title = "心理咨询师";
        this.license = "";
        this.specialty = "";
        this.education = "";
        this.certifications = "";
        this.workingYears = 0L;
        this.totalClients = 0L;
        this.totalScore = 0L;
        this.successNum = 0L;
        this.commentNum = 0L;
        this.consultationFee = "";
        this.location = "";
        this.workingTime = "";
        this.responseTime = "";
        this.introduction = "";
        this.achievements = "";
    }

}
