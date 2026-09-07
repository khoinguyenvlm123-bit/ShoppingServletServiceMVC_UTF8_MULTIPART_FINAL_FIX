package vn.iotstar.service.impl;

import vn.iotstar.dao.ProductRatingDao;
import vn.iotstar.dao.impl.ProductRatingDaoImpl;
import vn.iotstar.entity.Product;
import vn.iotstar.entity.ProductRating;
import vn.iotstar.entity.User;
import vn.iotstar.service.ProductRatingService;

import java.util.List;

public class ProductRatingServiceImpl implements ProductRatingService {
    private final ProductRatingDao dao = new ProductRatingDaoImpl();

    @Override public List<ProductRating> findByProduct(int productId) { return dao.findByProduct(productId); }
    @Override public ProductRating findByUserAndProduct(int userId, int productId) { return dao.findByUserAndProduct(userId, productId); }

    @Override
    public void rate(int userId, int productId, int stars, String comment) {
        if (stars < 1 || stars > 5) throw new IllegalArgumentException("Số sao phải từ 1 đến 5.");
        ProductRating r = new ProductRating();
        User u = new User(); u.setId(userId);
        Product p = new Product(); p.setId(productId);
        r.setUser(u);
        r.setProduct(p);
        r.setStars(stars);
        r.setComment(comment == null ? "" : comment.trim());
        dao.saveOrUpdate(r);
    }

    @Override public double averageStars(int productId) { return dao.averageStars(productId); }
    @Override public long countByProduct(int productId) { return dao.countByProduct(productId); }
}
