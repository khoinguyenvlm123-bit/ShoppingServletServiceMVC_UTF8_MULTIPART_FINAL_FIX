package vn.iotstar.service;

import vn.iotstar.entity.ProductRating;
import java.util.List;

public interface ProductRatingService {
    List<ProductRating> findByProduct(int productId);
    ProductRating findByUserAndProduct(int userId, int productId);
    void rate(int userId, int productId, int stars, String comment);
    double averageStars(int productId);
    long countByProduct(int productId);
}
