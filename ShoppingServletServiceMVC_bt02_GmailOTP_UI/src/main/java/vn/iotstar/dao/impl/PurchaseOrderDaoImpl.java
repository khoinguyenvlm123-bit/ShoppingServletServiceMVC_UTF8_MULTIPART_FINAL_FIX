package vn.iotstar.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import vn.iotstar.connection.JPAConfig;
import vn.iotstar.dao.PurchaseOrderDao;
import vn.iotstar.entity.Product;
import vn.iotstar.entity.PurchaseOrder;
import vn.iotstar.entity.User;

import java.math.BigDecimal;
import java.util.List;

public class PurchaseOrderDaoImpl implements PurchaseOrderDao {
    @Override
    public PurchaseOrder buy(int userId, int productId, int quantity) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            User user = em.find(User.class, userId);
            Product product = em.find(Product.class, productId, LockModeType.PESSIMISTIC_WRITE);
            if (user == null) throw new IllegalArgumentException("Tài khoản không tồn tại.");
            if (product == null) throw new IllegalArgumentException("Sản phẩm không tồn tại.");
            if (quantity < 1) throw new IllegalArgumentException("Số lượng mua phải lớn hơn 0.");
            if (product.getQuantity() < quantity) {
                throw new IllegalArgumentException("Sản phẩm chỉ còn " + product.getQuantity() + " sản phẩm.");
            }

            product.setQuantity(product.getQuantity() - quantity);
            BigDecimal unit = product.getPrice();
            PurchaseOrder order = new PurchaseOrder();
            order.setUser(user);
            order.setProduct(product);
            order.setQuantity(quantity);
            order.setUnitPrice(unit);
            order.setTotalPrice(unit.multiply(BigDecimal.valueOf(quantity)));
            order.setStatus("DA_DAT");
            em.persist(order);
            em.merge(product);
            tx.commit();
            return order;
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally { em.close(); }
    }

    @Override
    public List<PurchaseOrder> findByUser(int userId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT o FROM PurchaseOrder o WHERE o.user.id=:uid ORDER BY o.createdAt DESC", PurchaseOrder.class)
                    .setParameter("uid", userId)
                    .getResultList();
        } finally { em.close(); }
    }
}
