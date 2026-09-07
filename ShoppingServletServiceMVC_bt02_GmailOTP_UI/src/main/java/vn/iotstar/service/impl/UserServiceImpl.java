package vn.iotstar.service.impl;

import vn.iotstar.dao.UserDao;
import vn.iotstar.dao.impl.UserDaoImpl;
import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;
import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserDao userDao=new UserDaoImpl();
    public User login(String u,String p){User x=get(u);return x!=null&&p!=null&&p.equals(x.getPassWord())?x:null;}
    public User get(String u){return userDao.get(u);} public User findByEmail(String e){return userDao.findByEmail(e);} public User findById(int id){return userDao.findById(id);} public List<User> findAll(){return userDao.findAll();} public List<User> search(String k){return userDao.search(k);}
    public void insert(User u){userDao.insert(u);} public void update(User u){userDao.update(u);} public void delete(int id){userDao.delete(id);}
    public boolean register(String username,String password,String email,String fullname,String phone){if(checkExistUsername(username)||checkExistEmail(email)||checkExistPhone(phone))return false;User u=new User(email,username,fullname,password,null,5,phone,new java.sql.Date(System.currentTimeMillis()));userDao.insert(u);return true;}
    public boolean checkExistEmail(String e){return userDao.checkExistEmail(e);} public boolean checkExistUsername(String u){return userDao.checkExistUsername(u);} public boolean checkExistPhone(String p){return userDao.checkExistPhone(p);}
}
