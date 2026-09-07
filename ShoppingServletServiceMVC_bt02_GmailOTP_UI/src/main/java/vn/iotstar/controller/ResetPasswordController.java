package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;
import vn.iotstar.service.impl.UserServiceImpl;
import java.io.IOException;

@WebServlet("/reset-password")
public class ResetPasswordController extends HttpServlet {
    private final UserService service=new UserServiceImpl();
    private boolean allowed(HttpSession s){return s!=null&&Boolean.TRUE.equals(s.getAttribute("forgotVerified"))&&s.getAttribute("forgotUserId")!=null;}
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{HttpSession s=req.getSession(false);if(!allowed(s)){resp.sendRedirect(req.getContextPath()+"/forgot-password");return;}req.getRequestDispatcher("/view/reset-password.jsp").forward(req,resp);}
    @Override protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        HttpSession s=req.getSession(false);if(!allowed(s)){resp.sendRedirect(req.getContextPath()+"/forgot-password");return;}
        String p=req.getParameter("password"),rp=req.getParameter("repassword");if(p==null||p.length()<6||!p.equals(rp)){req.setAttribute("alert","Mật khẩu phải có ít nhất 6 ký tự và hai lần nhập phải trùng nhau.");doGet(req,resp);return;}
        User u=service.findById((Integer)s.getAttribute("forgotUserId"));if(u==null){resp.sendRedirect(req.getContextPath()+"/forgot-password");return;}u.setPassWord(p);service.update(u);
        s.removeAttribute("forgotUserId");s.removeAttribute("forgotEmail");s.removeAttribute("forgotOtp");s.removeAttribute("forgotOtpExpire");s.removeAttribute("forgotOtpLastSent");s.removeAttribute("forgotVerified");resp.sendRedirect(req.getContextPath()+"/login?reset=1");
    }
}
