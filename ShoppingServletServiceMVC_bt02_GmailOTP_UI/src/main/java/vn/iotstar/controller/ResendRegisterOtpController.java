package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.entity.User;
import vn.iotstar.util.EmailUtil;
import vn.iotstar.util.OtpUtil;

import java.io.IOException;

@WebServlet("/resend-register-otp")
public class ResendRegisterOtpController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute("registerPendingUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/register");
            return;
        }

        long now = System.currentTimeMillis();
        Long last = (Long) s.getAttribute("registerOtpLastSent");
        if (last != null && now - last < OtpUtil.RESEND_COOLDOWN_MILLIS) {
            long wait = (OtpUtil.RESEND_COOLDOWN_MILLIS - (now - last) + 999) / 1000;
            s.setAttribute("otpFlashError", "Vui lòng chờ " + wait + " giây trước khi gửi lại OTP.");
            resp.sendRedirect(req.getContextPath() + "/verify-register-otp");
            return;
        }

        User user = (User) s.getAttribute("registerPendingUser");
        String otp = OtpUtil.generateOtp();
        try {
            EmailUtil.sendOtp(user.getEmail(), otp, "kích hoạt tài khoản");
            s.setAttribute("registerOtp", otp);
            s.setAttribute("registerOtpExpire", now + OtpUtil.OTP_EXPIRE_MILLIS);
            s.setAttribute("registerOtpLastSent", now);
            s.setAttribute("otpFlashSuccess", "Đã gửi một mã OTP mới đến Gmail của bạn.");
        } catch (RuntimeException e) {
            s.setAttribute("otpFlashError", e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/verify-register-otp");
    }
}
