package vn.iotstar.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import vn.iotstar.connection.JPAConfig;
import vn.iotstar.dao.UserDao;
import vn.iotstar.entity.User;

import java.util.List;

public class UserDaoImpl implements UserDao {
    @Override
    public User get(String username) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.userName = :username", User.class)
                    .setParameter("username", username).getSingleResult();
        } catch (NoResultException e) { return null; }
        finally { em.close(); }
    }

    @Override
    public User findByEmail(String email) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:email)", User.class)
                    .setParameter("email", email).getSingleResult();
        } catch (NoResultException e) { return null; }
        finally { em.close(); }
    }

    @Override
    public User findById(int id) {
        EntityManager em = JPAConfig.getEntityManager();
        try { return em.find(User.class, id); }
        finally { em.close(); }
    }

    @Override
    public List<User> findAll() {
        EntityManager em = JPAConfig.getEntityManager();
        try { return em.createNamedQuery("User.findAll", User.class).getResultList(); }
        finally { em.close(); }
    }

    @Override
    public List<User> search(String keyword) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<User> q = em.createQuery(
                "SELECT u FROM User u WHERE LOWER(u.userName) LIKE LOWER(:k) OR LOWER(u.fullName) LIKE LOWER(:k) OR LOWER(u.email) LIKE LOWER(:k) ORDER BY u.id DESC", User.class);
            q.setParameter("k", "%" + keyword + "%");
            return q.getResultList();
        } finally { em.close(); }
    }

    @Override
    public void insert(User user) { executeWrite(em -> em.persist(user)); }

    @Override
    public void update(User user) { executeWrite(em -> em.merge(user)); }

    @Override
    public void delete(int id) {
        executeWrite(em -> { User user = em.find(User.class, id); if (user != null) em.remove(user); });
    }

    @Override public boolean checkExistEmail(String email) { return exists("email", email); }
    @Override public boolean checkExistUsername(String username) { return exists("userName", username); }
    @Override public boolean checkExistPhone(String phone) { return exists("phone", phone); }

    private boolean exists(String field, String value) {
        if (value == null || value.trim().isEmpty()) return false;
        EntityManager em = JPAConfig.getEntityManager();
        try {
            Long n = em.createQuery("SELECT COUNT(u) FROM User u WHERE u." + field + " = :value", Long.class)
                    .setParameter("value", value.trim()).getSingleResult();
            return n > 0;
        } finally { em.close(); }
    }

    private interface JpaWork { void run(EntityManager em); }
    private void executeWrite(JpaWork work) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin(); work.run(em); tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally { em.close(); }
    }
}
