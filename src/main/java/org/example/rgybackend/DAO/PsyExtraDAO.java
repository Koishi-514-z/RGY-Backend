package org.example.rgybackend.DAO;

import org.example.rgybackend.DTO.PsyCommentData;
import org.example.rgybackend.Entity.PsyProfileExtra;

public interface PsyExtraDAO {
    PsyProfileExtra getPsyProfileExtra(String psyid);

    boolean setPsyProfileExtra(PsyProfileExtra psyProfileExtra);

    boolean increaseClients(String psyid);

    boolean addComments(PsyCommentData psyCommentData);
}
