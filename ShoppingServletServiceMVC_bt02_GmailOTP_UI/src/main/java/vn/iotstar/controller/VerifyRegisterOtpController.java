package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.util.OtpUtil;

import java.io.IOException;

@WebServlet("/verify-register-otp")
public class VerifyRegisterOtpController extends HttpServlet {
    private final UserService service = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute("registerPendingUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/register");
            return;
        }
        showPage(req, resp, s);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute("registerPendingUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/register");
            return;
        }

        String input = req.getParameter("otp");
        String otp = (String) s.getAttribute("registerOtp");
        Long expire = (Long) s.getAttribute("registerOtpExpire");

        if (expire == null || System.currentTimeMillis() > expire) {
            req.setAttribute("alert", "Mã OTP đã hết hạn. Hãy bấm Gửi lại mã để nhận OTP mới.");
            showPage(req, resp, s);
            return;
        }
        if (input == null || !input.trim().equals(otp)) {
            req.setAttribute("alert", "Mã OTP không đúng. Vui lòng kiểm tra lại Gmail.");
            showPage(req, resp, s);
            return;
        }

        User u = (User) s.getAttribute("registerPendingUser");
        if (service.checkExistEmail(u.getEmail()) || service.checkExistUsername(u.getUserName()) || service.checkExistPhone(u.getPhone())) {
            req.setAttribute("alert", "Thông tin tài khoản đã được sử dụng. Vui lòng đăng ký lại.");
            showPage(req, resp, s);
            return;
        }

        service.insert(u);
        s.removeAttribute("registerPendingUser");
        s.removeAttribute("registerOtp");
        s.removeAttribute("registerOtpExpire");
        s.removeAttribute("registerOtpLastSent");
        resp.sendRedirect(req.getContextPath() + "/login?activated=1");
    }

    private void showPage(HttpServletRequest req, HttpServletResponse resp, HttpSession s) throws ServletException, IOException {
        User u = (User) s.getAttribute("registerPendingUser");
        req.setAttribute("maskedEmail", OtpUtil.maskEmail(u.getEmail()));
        req.setAttribute("otpExpire", s.getAttribute("registerOtpExpire"));
        Long lastSent = (Long) s.getAttribute("registerOtpLastSent");
        long resendWait = lastSent == null ? 0 : Math.max(0, (OtpUtil.RESEND_COOLDOWN_MILLIS - (System.currentTimeMillis() - lastSent) + 999) / 1000);
        req.setAttribute("resendWait", resendWait);
        req.getRequestDispatcher("/view/verify-register-otp.jsp").forward(req, resp);
    }
}
