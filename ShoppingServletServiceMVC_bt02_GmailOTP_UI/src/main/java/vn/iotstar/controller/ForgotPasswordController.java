package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.util.EmailUtil;
import vn.iotstar.util.OtpUtil;
import java.io.IOException;

@WebServlet("/forgot-password")
public class ForgotPasswordController extends HttpServlet {
    private final UserService service=new UserServiceImpl();
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{req.getRequestDispatcher("/view/forgot-password.jsp").forward(req,resp);}
    @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        String email=req.getParameter("email"); email=email==null?"":email.trim();
        User user=service.findByEmail(email);
        if(user==null){req.setAttribute("alert","Không tìm thấy tài khoản với email này.");doGet(req,resp);return;}
        String otp=OtpUtil.generateOtp();
        try{
            EmailUtil.sendOtp(email,otp,"xác nhận quên mật khẩu");
            HttpSession s=req.getSession(true); long now=System.currentTimeMillis(); s.setAttribute("forgotUserId",user.getId());s.setAttribute("forgotEmail",email);s.setAttribute("forgotOtp",otp);s.setAttribute("forgotOtpExpire",now+OtpUtil.OTP_EXPIRE_MILLIS);s.setAttribute("forgotOtpLastSent",now);s.removeAttribute("forgotVerified");
            resp.sendRedirect(req.getContextPath()+"/verify-forgot-otp");
        }catch(RuntimeException e){req.setAttribute("alert",e.getMessage());doGet(req,resp);}
    }
}
