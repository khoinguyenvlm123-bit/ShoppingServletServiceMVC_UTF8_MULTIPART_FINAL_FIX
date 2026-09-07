package vn.iotstar.service.impl;

import vn.iotstar.dao.ProductDao;
import vn.iotstar.dao.impl.ProductDaoImpl;
import vn.iotstar.entity.Product;
import vn.iotstar.service.ProductService;
import java.util.List;

public class ProductServiceImpl implements ProductService {
    private final ProductDao dao = new ProductDaoImpl();
    public void insert(Product product) { dao.insert(product); }
    public void update(Product product) { dao.update(product); }
    public void delete(int id) { dao.delete(id); }
    public Product findById(int id) { return dao.findById(id); }
    public List<Product> findAll() { return dao.findAll(); }
    public List<Product> searchByName(String keyword) { return dao.searchByName(keyword); }
    public List<Product> findLatest(int limit) { return dao.findLatest(limit); }
    public List<Product> findPage(int page, int pageSize) { return dao.findPage(page, pageSize); }
    public long count() { return dao.count(); }
}
