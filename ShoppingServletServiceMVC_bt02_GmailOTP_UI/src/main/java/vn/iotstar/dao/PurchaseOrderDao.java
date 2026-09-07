package vn.iotstar.dao;

import vn.iotstar.entity.PurchaseOrder;
import java.util.List;

public interface PurchaseOrderDao {
    PurchaseOrder buy(int userId, int productId, int quantity);
    List<PurchaseOrder> findByUser(int userId);
}
