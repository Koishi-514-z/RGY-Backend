package org.example.rgybackend.DAO;

import org.example.rgybackend.Entity.PsyProfileExtra;

public interface PsyExtraDAO {
    PsyProfileExtra getPsyProfileExtra(String psyid);

    boolean setPsyProfileExtra(PsyProfileExtra psyProfileExtra);
}
