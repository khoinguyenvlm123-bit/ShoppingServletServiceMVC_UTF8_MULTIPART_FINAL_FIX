package vn.iotstar.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import vn.iotstar.entity.Category;
import vn.iotstar.service.CategoryService;
import vn.iotstar.service.impl.CategoryServiceImpl;
import vn.iotstar.util.UploadConstant;
import vn.iotstar.util.MultipartUtf8;

import java.io.File;
import java.io.IOException;

@WebServlet("/admin/category/add")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024)
public class CategoryAddController extends HttpServlet {
    private final CategoryService categoryService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!AdminAuth.requireAdmin(req, resp)) return;
        req.getRequestDispatcher("/views/admin/add-category.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!AdminAuth.requireAdmin(req, resp)) return;
        req.setCharacterEncoding("UTF-8");

        String name = MultipartUtf8.getParameter(req, "name");
        if (name == null || name.isBlank()) {
            req.setAttribute("error", "Tên danh mục không được để trống.");
            req.getRequestDispatcher("/views/admin/add-category.jsp").forward(req, resp);
            return;
        }

        String icon = savePart(req.getPart("icon"));
        categoryService.insert(new Category(name.trim(), icon));
        resp.sendRedirect(req.getContextPath() + "/admin/category/list");
    }

    private String savePart(Part part) throws IOException {
        if (part == null || part.getSize() == 0 || part.getSubmittedFileName() == null) return null;
        String original = new File(part.getSubmittedFileName()).getName();
        String ext = "";
        int dot = original.lastIndexOf('.');
        if (dot >= 0) ext = original.substring(dot);

        String fileName = System.currentTimeMillis() + ext;
        File dir = new File(UploadConstant.DIR, "category");
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Không tạo được thư mục upload: " + dir.getAbsolutePath());
        }
        part.write(new File(dir, fileName).getAbsolutePath());
        return "category/" + fileName;
    }
}
