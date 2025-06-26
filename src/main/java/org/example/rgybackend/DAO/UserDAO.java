package org.example.rgybackend.DAO;

import java.util.List;

import org.example.rgybackend.Model.Profile;
import org.example.rgybackend.Model.SimplifiedProfile;

public interface UserDAO {
    boolean existed(String userid);

    boolean existedByName(String username);

    List<Profile> getAll();

    Profile get(String userid);

    Profile getByName(String username);

    SimplifiedProfile getSimplified(String userid);

    boolean add(Profile profile);

    boolean update(Profile profile);

    boolean remove(String userid);
}
