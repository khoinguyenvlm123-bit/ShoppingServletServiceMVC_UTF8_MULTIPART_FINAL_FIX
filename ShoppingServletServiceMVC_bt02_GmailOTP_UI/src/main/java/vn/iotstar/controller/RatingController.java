package vn.iotstar.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.entity.User;
import vn.iotstar.service.ProductRatingService;
import vn.iotstar.service.impl.ProductRatingServiceImpl;
import vn.iotstar.util.Constant;

import java.io.IOException;

@WebServlet("/rating/save")
public class RatingController extends HttpServlet {
    private final ProductRatingService ratingService = new ProductRatingServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(false);
        User user = session == null ? null : (User) session.getAttribute(Constant.SESSION_ACCOUNT);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        int productId;
        try {
            productId = Integer.parseInt(req.getParameter("productId"));
            int stars = Integer.parseInt(req.getParameter("stars"));
            String comment = req.getParameter("comment");
            ratingService.rate(user.getId(), productId, stars, comment);
            session.setAttribute("productMessage", "Cảm ơn bạn đã đánh giá sản phẩm.");
        } catch (Exception e) {
            try { productId = Integer.parseInt(req.getParameter("productId")); } catch (Exception ignored) { productId = 0; }
            session.setAttribute("productError", "Không thể lưu đánh giá: " + e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/product/detail?id=" + productId);
    }
}
