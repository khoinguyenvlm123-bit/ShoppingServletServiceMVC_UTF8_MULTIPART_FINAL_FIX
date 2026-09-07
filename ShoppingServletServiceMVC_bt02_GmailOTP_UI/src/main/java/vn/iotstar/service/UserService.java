package vn.iotstar.service;

import vn.iotstar.entity.User;
import java.util.List;

public interface UserService {
    User login(String username,String password);
    User get(String username);
    User findByEmail(String email);
    User findById(int id);
    List<User> findAll();
    List<User> search(String keyword);
    void insert(User user);
    void update(User user);
    void delete(int id);
    boolean register(String username,String password,String email,String fullname,String phone);
    boolean checkExistEmail(String email);
    boolean checkExistUsername(String username);
    boolean checkExistPhone(String phone);
}
