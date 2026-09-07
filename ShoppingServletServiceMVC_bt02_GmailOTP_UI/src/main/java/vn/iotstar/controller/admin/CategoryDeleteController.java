package vn.iotstar.controller.admin;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.entity.Category;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.impl.CategoryServiceImpl;
import vn.iotstar.util.UploadConstant;

import java.io.File;
import java.io.IOException;

@WebServlet("/admin/category/delete")
public class CategoryDeleteController extends HttpServlet {
    private final CategoryService categoryService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!AdminAuth.requireAdmin(req, resp)) return;

        int id = Integer.parseInt(req.getParameter("id"));
        Category category = categoryService.findById(id);
        if (category != null) {
            try {
                categoryService.delete(id);
            } catch (Exception e) {
                throw new IOException("Không xóa được Category có id = " + id, e);
            }
            if (category.getIcon() != null && !category.getIcon().isBlank()) {
                File file = new File(UploadConstant.DIR, category.getIcon().replace('/', File.separatorChar));
                if (file.exists()) file.delete();
            }
        }
        resp.sendRedirect(req.getContextPath() + "/admin/category/list");
    }
}
