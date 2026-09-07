package vn.iotstar.service.impl;

import vn.iotstar.dao.CategoryDao;
import vn.iotstar.dao.impl.CategoryDaoImpl;
import vn.iotstar.entity.Category;
import vn.iotstar.service.CategoryService;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    private final CategoryDao categoryDao = new CategoryDaoImpl();

    @Override
    public void insert(Category category) {
        if (findByCategoryname(category.getName()) == null) {
            categoryDao.insert(category);
        }
    }

    @Override
    public void update(Category category) {
        if (findById(category.getId()) != null) {
            categoryDao.update(category);
        }
    }

    @Override
    public void delete(int cateId) throws Exception {
        categoryDao.delete(cateId);
    }

    @Override
    public Category findById(int cateId) {
        return categoryDao.findById(cateId);
    }

    @Override
    public Category findByCategoryname(String name) {
        return categoryDao.findByCategoryname(name);
    }

    @Override
    public List<Category> findAll() {
        return categoryDao.findAll();
    }

    @Override
    public List<Category> searchByName(String categoryName) {
        return categoryDao.searchByName(categoryName);
    }

    @Override
    public List<Category> findAll(int page, int pageSize) {
        return categoryDao.findAll(page, pageSize);
    }

    @Override
    public int count() {
        return categoryDao.count();
    }
}
