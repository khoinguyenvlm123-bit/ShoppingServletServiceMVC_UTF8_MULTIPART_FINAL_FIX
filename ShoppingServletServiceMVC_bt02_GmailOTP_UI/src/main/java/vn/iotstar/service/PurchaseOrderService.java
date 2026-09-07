package vn.iotstar.service;

import vn.iotstar.entity.PurchaseOrder;
import java.util.List;

public interface PurchaseOrderService {
    PurchaseOrder buy(int userId, int productId, int quantity);
    List<PurchaseOrder> findByUser(int userId);
}
