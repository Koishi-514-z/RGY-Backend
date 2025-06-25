package org.example.rgybackend.DAO;

public interface UserAuthDAO {
    boolean pwdMatch(String userid, String password);

    boolean addAuth(String userid, String stuid, String password);

    boolean updatePassword(String userid, String password);

    boolean removeAuth(String userid);
}
