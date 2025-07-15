package org.example.rgybackend.Service;

import java.util.List;

import org.example.rgybackend.DTO.PsyCommentData;
import org.example.rgybackend.Model.AvailableTimeModel;
import org.example.rgybackend.Model.CounselingModel;
import org.example.rgybackend.Model.TagModel;

public interface CounselingService {
    List<CounselingModel> getCounseling(String psyid);

    List<CounselingModel> getUserCounseling(String userid);

    List<CounselingModel> getDateCounseling(String psyid, Long timestamp);

    boolean addCounseling(CounselingModel counselingModel, String userid);

    boolean addComment(PsyCommentData psyCommentData);

    boolean setStatus(Long counselingid, Long status, String userid);

    boolean removeCounseling(Long counselingid);

    AvailableTimeModel getAvailableTime(String psyid);

    List<Long> getDateAvailables(String psyid, Long timestamp);

    boolean setAvailableTimes(AvailableTimeModel availableTimeModel);

    List<TagModel> getTypeTags();

    boolean placeCallBackRequest(String userid);
}
