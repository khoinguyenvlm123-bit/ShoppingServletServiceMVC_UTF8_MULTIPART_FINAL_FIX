package vn.iotstar.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import vn.iotstar.connection.JPAConfig;
import vn.iotstar.dao.ProductRatingDao;
import vn.iotstar.entity.ProductRating;

import java.time.LocalDateTime;
import java.util.List;

public class ProductRatingDaoImpl implements ProductRatingDao {
    @Override
    public List<ProductRating> findByProduct(int productId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT r FROM ProductRating r WHERE r.product.id=:pid ORDER BY r.updatedAt DESC, r.createdAt DESC",
                    ProductRating.class)
                    .setParameter("pid", productId)
                    .getResultList();
        } finally { em.close(); }
    }

    @Override
    public ProductRating findByUserAndProduct(int userId, int productId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT r FROM ProductRating r WHERE r.user.id=:uid AND r.product.id=:pid", ProductRating.class)
                    .setParameter("uid", userId)
                    .setParameter("pid", productId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally { em.close(); }
    }

    @Override
    public void saveOrUpdate(ProductRating rating) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            int userId = rating.getUser().getId();
            int productId = rating.getProduct().getId();
            ProductRating existing = em.createQuery(
                    "SELECT r FROM ProductRating r WHERE r.user.id=:uid AND r.product.id=:pid", ProductRating.class)
                    .setParameter("uid", userId)
                    .setParameter("pid", productId)
                    .getResultStream().findFirst().orElse(null);
            if (existing == null) {
                var managedUser = em.find(vn.iotstar.entity.User.class, userId);
                var managedProduct = em.find(vn.iotstar.entity.Product.class, productId);
                if (managedUser == null || managedProduct == null) {
                    throw new IllegalArgumentException("Tài khoản hoặc sản phẩm không tồn tại.");
                }
                rating.setUser(managedUser);
                rating.setProduct(managedProduct);
                em.persist(rating);
            } else {
                existing.setStars(rating.getStars());
                existing.setComment(rating.getComment());
                existing.setUpdatedAt(LocalDateTime.now());
                em.merge(existing);
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally { em.close(); }
    }

    @Override
    public double averageStars(int productId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            Double value = em.createQuery(
                    "SELECT AVG(r.stars) FROM ProductRating r WHERE r.product.id=:pid", Double.class)
                    .setParameter("pid", productId)
                    .getSingleResult();
            return value == null ? 0.0 : value;
        } finally { em.close(); }
    }

    @Override
    public long countByProduct(int productId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT COUNT(r) FROM ProductRating r WHERE r.product.id=:pid", Long.class)
                    .setParameter("pid", productId)
                    .getSingleResult();
        } finally { em.close(); }
    }
}
