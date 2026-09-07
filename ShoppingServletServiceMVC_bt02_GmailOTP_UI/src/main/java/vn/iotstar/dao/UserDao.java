package vn.iotstar.dao;

import vn.iotstar.entity.User;
import java.util.List;

public interface UserDao {
    User get(String username);
    User findByEmail(String email);
    User findById(int id);
    List<User> findAll();
    List<User> search(String keyword);
    void insert(User user);
    void update(User user);
    void delete(int id);
    boolean checkExistEmail(String email);
    boolean checkExistUsername(String username);
    boolean checkExistPhone(String phone);
}
