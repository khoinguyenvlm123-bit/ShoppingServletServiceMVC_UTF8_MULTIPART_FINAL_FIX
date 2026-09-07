package vn.iotstar.service;

import java.util.List;
import vn.iotstar.entity.Category;

public interface CategoryService {
    void insert(Category category);
    void update(Category category);
    void delete(int cateId) throws Exception;
    Category findById(int cateId);
    Category findByCategoryname(String name);
    List<Category> findAll();
    List<Category> searchByName(String categoryName);
    List<Category> findAll(int page, int pageSize);
    int count();
}
