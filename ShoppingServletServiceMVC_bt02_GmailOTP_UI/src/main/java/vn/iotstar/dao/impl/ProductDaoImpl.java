package vn.iotstar.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import vn.iotstar.connection.JPAConfig;
import vn.iotstar.dao.ProductDao;
import vn.iotstar.entity.Product;

import java.util.List;

public class ProductDaoImpl implements ProductDao {
    @Override
    public void insert(Product product) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(product);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally { em.close(); }
    }

    @Override
    public void update(Product product) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(product);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally { em.close(); }
    }

    @Override
    public void delete(int id) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Product product = em.find(Product.class, id);
            if (product != null) em.remove(product);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally { em.close(); }
    }

    @Override
    public Product findById(int id) {
        EntityManager em = JPAConfig.getEntityManager();
        try { return em.find(Product.class, id); }
        finally { em.close(); }
    }

    @Override
    public List<Product> findAll() {
        EntityManager em = JPAConfig.getEntityManager();
        try { return em.createNamedQuery("Product.findAll", Product.class).getResultList(); }
        finally { em.close(); }
    }

    @Override
    public List<Product> searchByName(String keyword) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Product> q = em.createQuery(
                    "SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(:keyword) ORDER BY p.id DESC", Product.class);
            q.setParameter("keyword", "%" + keyword + "%");
            return q.getResultList();
        } finally { em.close(); }
    }
    @Override
    public List<Product> findLatest(int limit) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Product p ORDER BY p.id DESC", Product.class)
                    .setMaxResults(limit).getResultList();
        } finally { em.close(); }
    }

    @Override
    public List<Product> findPage(int page, int pageSize) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Product p ORDER BY p.id DESC", Product.class)
                    .setFirstResult((page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
        } finally { em.close(); }
    }

    @Override
    public long count() {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(p) FROM Product p", Long.class).getSingleResult();
        } finally { em.close(); }
    }

}
