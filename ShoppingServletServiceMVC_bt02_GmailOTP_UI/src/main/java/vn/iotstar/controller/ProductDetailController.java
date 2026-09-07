package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.entity.Product;
import vn.iotstar.entity.User;
import vn.iotstar.service.ProductRatingService;
import vn.iotstar.service.ProductService;
import vn.iotstar.service.impl.ProductRatingServiceImpl;
import vn.iotstar.service.impl.ProductServiceImpl;
import vn.iotstar.util.Constant;

import java.io.IOException;

@WebServlet("/product/detail")
public class ProductDetailController extends HttpServlet {
    private final ProductService productService = new ProductServiceImpl();
    private final ProductRatingService ratingService = new ProductRatingServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            Product product = productService.findById(id);
            if (product == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            req.setAttribute("product", product);
            req.setAttribute("ratings", ratingService.findByProduct(id));
            req.setAttribute("averageStars", ratingService.averageStars(id));
            req.setAttribute("ratingCount", ratingService.countByProduct(id));

            HttpSession session = req.getSession(false);
            User account = session == null ? null : (User) session.getAttribute(Constant.SESSION_ACCOUNT);
            if (account != null) {
                req.setAttribute("myRating", ratingService.findByUserAndProduct(account.getId(), id));
            }

            req.getRequestDispatcher("/views/web/product-detail.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
