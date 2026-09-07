package vn.iotstar.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.entity.Category;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.impl.CategoryServiceImpl;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/category/list")
public class CategoryListController extends HttpServlet {
    private final CategoryService categoryService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!AdminAuth.requireAdmin(req, resp)) return;

        String keyword = req.getParameter("keyword");
        List<Category> cateList = (keyword == null || keyword.isBlank())
                ? categoryService.findAll()
                : categoryService.searchByName(keyword.trim());

        req.setAttribute("cateList", cateList);
        req.setAttribute("keyword", keyword == null ? "" : keyword);
        req.getRequestDispatcher("/views/admin/list-category.jsp").forward(req, resp);
    }
}
