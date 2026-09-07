package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.util.Constant;
import vn.iotstar.util.MultipartUtf8;
import vn.iotstar.util.UploadConstant;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@WebServlet("/profile")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 6 * 1024 * 1024
)
public class ProfileController extends HttpServlet {
    private final UserService userService = new UserServiceImpl();
    private static final Set<String> ALLOWED_EXT = Set.of("jpg", "jpeg", "png", "gif", "webp");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User sessionUser = requireLogin(req, resp);
        if (sessionUser == null) return;

        User freshUser = userService.findById(sessionUser.getId());
        if (freshUser == null) {
            req.getSession().invalidate();
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        req.getSession().setAttribute(Constant.SESSION_ACCOUNT, freshUser);
        req.setAttribute("user", freshUser);
        req.getRequestDispatcher("/views/web/profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        User sessionUser = requireLogin(req, resp);
        if (sessionUser == null) return;

        User user = userService.findById(sessionUser.getId());
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/logout");
            return;
        }

        String fullName = trim(MultipartUtf8.getParameter(req, "fullname"));
        String phone = trim(MultipartUtf8.getParameter(req, "phone"));

        if (fullName.isEmpty()) {
            showError(req, resp, user, "Họ và tên không được để trống.");
            return;
        }
        if (!phone.isEmpty() && !phone.matches("^\\+?[0-9]{9,14}$")) {
            showError(req, resp, user, "Số điện thoại chỉ gồm số (có thể bắt đầu bằng +), từ 9 đến 15 ký tự.");
            return;
        }
        if (!phone.equals(user.getPhone()) && !phone.isEmpty() && userService.checkExistPhone(phone)) {
            showError(req, resp, user, "Số điện thoại này đã được tài khoản khác sử dụng.");
            return;
        }

        String oldAvatar = user.getAvatar();
        String newAvatar = oldAvatar;
        try {
            Part imagePart = req.getPart("images");
            if (imagePart != null && imagePart.getSize() > 0) {
                newAvatar = saveAvatar(imagePart);
            }

            user.setFullName(fullName);
            user.setPhone(phone);
            user.setAvatar(newAvatar);
            userService.update(user);

            if (newAvatar != null && !newAvatar.equals(oldAvatar)) {
                deleteOldAvatar(oldAvatar);
            }

            User updated = userService.findById(user.getId());
            req.getSession().setAttribute(Constant.SESSION_ACCOUNT, updated);
            req.getSession().setAttribute("profileSuccess", "Cập nhật thông tin cá nhân thành công.");
            resp.sendRedirect(req.getContextPath() + "/profile");
        } catch (IllegalArgumentException e) {
            showError(req, resp, user, e.getMessage());
        } catch (IllegalStateException e) {
            showError(req, resp, user, "File ảnh vượt quá giới hạn 5 MB.");
        } catch (Exception e) {
            if (newAvatar != null && !newAvatar.equals(oldAvatar)) deleteOldAvatar(newAvatar);
            showError(req, resp, user, "Không thể cập nhật profile: " + e.getMessage());
        }
    }

    private User requireLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute(Constant.SESSION_ACCOUNT) == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return null;
        }
        return (User) session.getAttribute(Constant.SESSION_ACCOUNT);
    }

    private void showError(HttpServletRequest req, HttpServletResponse resp, User user, String message)
            throws ServletException, IOException {
        req.setAttribute("user", user);
        req.setAttribute("error", message);
        req.getRequestDispatcher("/views/web/profile.jsp").forward(req, resp);
    }

    private String saveAvatar(Part part) throws IOException {
        String submitted = part.getSubmittedFileName();
        String ext = extensionOf(submitted);
        String contentType = part.getContentType() == null ? "" : part.getContentType().toLowerCase(Locale.ROOT);

        if (!ALLOWED_EXT.contains(ext) || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Ảnh đại diện chỉ chấp nhận JPG, JPEG, PNG, GIF hoặc WEBP.");
        }

        File avatarDir = new File(UploadConstant.DIR, "avatars");
        Files.createDirectories(avatarDir.toPath());
        String fileName = UUID.randomUUID() + "." + ext;
        File target = new File(avatarDir, fileName);
        try (var input = part.getInputStream()) {
            Files.copy(input, target.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        return "avatars/" + fileName;
    }

    private void deleteOldAvatar(String avatar) {
        if (avatar == null || avatar.isBlank() || avatar.startsWith("http://") || avatar.startsWith("https://")) return;
        try {
            File base = new File(UploadConstant.DIR).getCanonicalFile();
            File target = new File(base, avatar.replace('/', File.separatorChar)).getCanonicalFile();
            if (target.toPath().startsWith(base.toPath()) && target.isFile()) Files.deleteIfExists(target.toPath());
        } catch (IOException ignored) { }
    }

    private String extensionOf(String fileName) {
        if (fileName == null) return "";
        int dot = fileName.lastIndexOf('.');
        return dot < 0 ? "" : fileName.substring(dot + 1).toLowerCase(Locale.ROOT);
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
