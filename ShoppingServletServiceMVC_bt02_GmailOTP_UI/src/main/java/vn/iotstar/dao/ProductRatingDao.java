package vn.iotstar.dao;

import vn.iotstar.entity.ProductRating;
import java.util.List;

public interface ProductRatingDao {
    List<ProductRating> findByProduct(int productId);
    ProductRating findByUserAndProduct(int userId, int productId);
    void saveOrUpdate(ProductRating rating);
    double averageStars(int productId);
    long countByProduct(int productId);
}
