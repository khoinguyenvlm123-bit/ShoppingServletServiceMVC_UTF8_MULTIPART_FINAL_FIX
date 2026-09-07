package vn.iotstar.service.impl;

import vn.iotstar.dao.PurchaseOrderDao;
import vn.iotstar.dao.impl.PurchaseOrderDaoImpl;
import vn.iotstar.entity.PurchaseOrder;
import vn.iotstar.service.PurchaseOrderService;
import java.util.List;

public class PurchaseOrderServiceImpl implements PurchaseOrderService {
    private final PurchaseOrderDao dao = new PurchaseOrderDaoImpl();
    @Override public PurchaseOrder buy(int userId, int productId, int quantity) { return dao.buy(userId, productId, quantity); }
    @Override public List<PurchaseOrder> findByUser(int userId) { return dao.findByUser(userId); }
}
