package vn.iotstar.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.entity.Product;
import vn.iotstar.service.ProductService;
import vn.iotstar.service.impl.ProductServiceImpl;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/product/list")
public class ProductListController extends HttpServlet {
    private final ProductService service = new ProductServiceImpl();
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!AdminAuth.requireAdmin(req, resp)) return;
        String keyword=req.getParameter("keyword");
        List<Product> list=(keyword==null||keyword.isBlank())?service.findAll():service.searchByName(keyword.trim());
        req.setAttribute("products",list); req.setAttribute("keyword",keyword==null?"":keyword);
        req.getRequestDispatcher("/views/admin/list-product.jsp").forward(req,resp);
    }
}
