package vn.iotstar.dao;

import vn.iotstar.entity.Product;
import java.util.List;

public interface ProductDao {
    void insert(Product product);
    void update(Product product);
    void delete(int id);
    Product findById(int id);
    List<Product> findAll();
    List<Product> searchByName(String keyword);
    List<Product> findLatest(int limit);
    List<Product> findPage(int page, int pageSize);
    long count();
}
