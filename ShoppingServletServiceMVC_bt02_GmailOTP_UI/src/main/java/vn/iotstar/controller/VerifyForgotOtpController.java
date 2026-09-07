package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.util.OtpUtil;

import java.io.IOException;

@WebServlet("/verify-forgot-otp")
public class VerifyForgotOtpController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute("forgotUserId") == null) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }
        showPage(req, resp, s);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute("forgotUserId") == null) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }

        String otp = (String) s.getAttribute("forgotOtp");
        Long expire = (Long) s.getAttribute("forgotOtpExpire");
        String input = req.getParameter("otp");

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

        s.setAttribute("forgotVerified", Boolean.TRUE);
        resp.sendRedirect(req.getContextPath() + "/reset-password");
    }

    private void showPage(HttpServletRequest req, HttpServletResponse resp, HttpSession s) throws ServletException, IOException {
        String email = (String) s.getAttribute("forgotEmail");
        req.setAttribute("maskedEmail", OtpUtil.maskEmail(email));
        req.setAttribute("otpExpire", s.getAttribute("forgotOtpExpire"));
        Long lastSent = (Long) s.getAttribute("forgotOtpLastSent");
        long resendWait = lastSent == null ? 0 : Math.max(0, (OtpUtil.RESEND_COOLDOWN_MILLIS - (System.currentTimeMillis() - lastSent) + 999) / 1000);
        req.setAttribute("resendWait", resendWait);
        req.getRequestDispatcher("/view/verify-forgot-otp.jsp").forward(req, resp);
    }
}
