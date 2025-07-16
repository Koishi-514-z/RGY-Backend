package org.example.rgybackend.DAO;

public interface UserAuthDAO {
    boolean pwdMatch(String userid, String password);

    boolean addAuth(String userid, String stuid, String password);

    boolean updatePassword(String userid, String password);

    boolean isDisabled(String userid);

    boolean setDisabled(String userid, boolean disabled);
}
