package vn.iotstar.util;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Đọc field text trong multipart/form-data trực tiếp bằng UTF-8.
 * Tránh trường hợp container giải mã multipart bằng charset mặc định làm tiếng Việt thành dấu '?'.
 */
public final class MultipartUtf8 {
    private MultipartUtf8() {}

    public static String getParameter(HttpServletRequest request, String name)
            throws IOException, ServletException {
        Part part = request.getPart(name);
        if (part == null) {
            String value = request.getParameter(name);
            return value == null ? null : value;
        }
        return new String(part.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }
}
