package vn.iotstar.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.entity.User;
import vn.iotstar.service.PurchaseOrderService;
import vn.iotstar.service.impl.PurchaseOrderServiceImpl;
import vn.iotstar.util.Constant;

import java.io.IOException;

@WebServlet("/order/buy")
public class BuyController extends HttpServlet {
    private final PurchaseOrderService orderService = new PurchaseOrderServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        User user = session == null ? null : (User) session.getAttribute(Constant.SESSION_ACCOUNT);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        int productId = 0;
        try {
            productId = Integer.parseInt(req.getParameter("productId"));
            int quantity = Integer.parseInt(req.getParameter("quantity"));
            orderService.buy(user.getId(), productId, quantity);
            session.setAttribute("productMessage", "Mua hàng thành công. Đơn hàng đã được ghi nhận.");
        } catch (Exception e) {
            session.setAttribute("productError", "Không thể mua hàng: " + e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/product/detail?id=" + productId);
    }
}
