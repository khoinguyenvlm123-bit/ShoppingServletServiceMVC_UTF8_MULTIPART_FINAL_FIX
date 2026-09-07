package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.util.EmailUtil;
import vn.iotstar.util.OtpUtil;

import java.io.IOException;

@WebServlet("/resend-forgot-otp")
public class ResendForgotOtpController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute("forgotUserId") == null || s.getAttribute("forgotEmail") == null) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }

        long now = System.currentTimeMillis();
        Long last = (Long) s.getAttribute("forgotOtpLastSent");
        if (last != null && now - last < OtpUtil.RESEND_COOLDOWN_MILLIS) {
            long wait = (OtpUtil.RESEND_COOLDOWN_MILLIS - (now - last) + 999) / 1000;
            s.setAttribute("otpFlashError", "Vui lòng chờ " + wait + " giây trước khi gửi lại OTP.");
            resp.sendRedirect(req.getContextPath() + "/verify-forgot-otp");
            return;
        }

        String email = (String) s.getAttribute("forgotEmail");
        String otp = OtpUtil.generateOtp();
        try {
            EmailUtil.sendOtp(email, otp, "xác nhận quên mật khẩu");
            s.setAttribute("forgotOtp", otp);
            s.setAttribute("forgotOtpExpire", now + OtpUtil.OTP_EXPIRE_MILLIS);
            s.setAttribute("forgotOtpLastSent", now);
            s.setAttribute("otpFlashSuccess", "Đã gửi một mã OTP mới đến Gmail của bạn.");
        } catch (RuntimeException e) {
            s.setAttribute("otpFlashError", e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/verify-forgot-otp");
    }
}
