package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.entity.User;
import vn.iotstar.service.PurchaseOrderService;
import vn.iotstar.service.impl.PurchaseOrderServiceImpl;
import vn.iotstar.util.Constant;

import java.io.IOException;

@WebServlet("/order/my")
public class MyOrdersController extends HttpServlet {
    private final PurchaseOrderService orderService = new PurchaseOrderServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = session == null ? null : (User) session.getAttribute(Constant.SESSION_ACCOUNT);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        req.setAttribute("orders", orderService.findByUser(user.getId()));
        req.getRequestDispatcher("/views/web/my-orders.jsp").forward(req, resp);
    }
}
