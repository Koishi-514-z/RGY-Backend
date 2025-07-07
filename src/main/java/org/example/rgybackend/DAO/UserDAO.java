package org.example.rgybackend.DAO;

import java.util.List;

import org.example.rgybackend.Model.ProfileModel;
import org.example.rgybackend.Model.SimplifiedProfileModel;

public interface UserDAO {
    boolean existed(String userid);

    boolean existedByName(String username);

    List<ProfileModel> getAll();

    List<ProfileModel> getAllPsys();

    ProfileModel get(String userid);

    ProfileModel getByName(String username);

    SimplifiedProfileModel getSimplified(String userid);

    boolean add(ProfileModel profile);

    boolean update(ProfileModel profile);

    boolean remove(String userid);

    Long getRole(String userid);
}
