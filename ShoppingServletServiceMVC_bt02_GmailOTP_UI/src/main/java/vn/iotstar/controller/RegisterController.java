package vn.iotstar.controller;

import java.io.IOException;
import java.sql.Date;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.util.Constant;
import vn.iotstar.util.EmailUtil;
import vn.iotstar.util.OtpUtil;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/register")
public class RegisterController extends HttpServlet {
    private final UserService service = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute(Constant.SESSION_ACCOUNT) != null) {
            resp.sendRedirect(req.getContextPath() + "/waiting");
            return;
        }
        req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String username = trim(req.getParameter("username"));
        String password = req.getParameter("password");
        String repassword = req.getParameter("repassword");
        String email = trim(req.getParameter("email"));
        String fullname = trim(req.getParameter("fullname"));
        String phone = trim(req.getParameter("phone"));

        if (username.isEmpty() || password == null || password.isEmpty() || email.isEmpty() || fullname.isEmpty() || phone.isEmpty()) {
            forwardError(req, resp, "Vui lòng nhập đầy đủ thông tin!"); return;
        }
        if (!password.equals(repassword)) { forwardError(req, resp, "Mật khẩu nhập lại không khớp!"); return; }
        if (service.checkExistEmail(email)) { forwardError(req, resp, "Email đã tồn tại!"); return; }
        if (service.checkExistUsername(username)) { forwardError(req, resp, "Tài khoản đã tồn tại!"); return; }
        if (service.checkExistPhone(phone)) { forwardError(req, resp, "Số điện thoại đã tồn tại!"); return; }

        User pending = new User(email, username, fullname, password, null, 5, phone, new Date(System.currentTimeMillis()));
        String otp = OtpUtil.generateOtp();
        try {
            EmailUtil.sendOtp(email, otp, "kích hoạt tài khoản");
            HttpSession session = req.getSession(true);
            session.setAttribute("registerPendingUser", pending);
            session.setAttribute("registerOtp", otp);
            long now = System.currentTimeMillis();
            session.setAttribute("registerOtpExpire", now + OtpUtil.OTP_EXPIRE_MILLIS);
            session.setAttribute("registerOtpLastSent", now);
            resp.sendRedirect(req.getContextPath() + "/verify-register-otp");
        } catch (RuntimeException e) {
            forwardError(req, resp, e.getMessage());
        }
    }

    private void forwardError(HttpServletRequest req, HttpServletResponse resp, String message) throws ServletException, IOException {
        req.setAttribute("alert", message);
        req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
    }
    private String trim(String value) { return value == null ? "" : value.trim(); }
}
