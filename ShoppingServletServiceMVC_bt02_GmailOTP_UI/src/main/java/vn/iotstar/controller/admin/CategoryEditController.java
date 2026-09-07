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

@WebServlet("/admin/category/edit")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024)
public class CategoryEditController extends HttpServlet {
    private final CategoryService categoryService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!AdminAuth.requireAdmin(req, resp)) return;

        int id = Integer.parseInt(req.getParameter("id"));
        Category category = categoryService.findById(id);
        if (category == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy Category.");
            return;
        }
        req.setAttribute("category", category);
        req.getRequestDispatcher("/views/admin/edit-category.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!AdminAuth.requireAdmin(req, resp)) return;
        req.setCharacterEncoding("UTF-8");

        int id = Integer.parseInt(MultipartUtf8.getParameter(req, "id"));
        String name = MultipartUtf8.getParameter(req, "name");
        Category old = categoryService.findById(id);
        if (old == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy Category.");
            return;
        }
        if (name == null || name.isBlank()) {
            req.setAttribute("error", "Tên danh mục không được để trống.");
            req.setAttribute("category", old);
            req.getRequestDispatcher("/views/admin/edit-category.jsp").forward(req, resp);
            return;
        }

        String icon = old.getIcon();
        Part part = req.getPart("icon");
        if (part != null && part.getSize() > 0) {
            icon = savePart(part);
            deleteOldIcon(old.getIcon());
        }

        categoryService.update(new Category(id, name.trim(), icon));
        resp.sendRedirect(req.getContextPath() + "/admin/category/list");
    }

    private String savePart(Part part) throws IOException {
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

    private void deleteOldIcon(String oldIcon) {
        if (oldIcon == null || oldIcon.isBlank()) return;
        File file = new File(UploadConstant.DIR, oldIcon.replace('/', File.separatorChar));
        if (file.exists()) file.delete();
    }
}
