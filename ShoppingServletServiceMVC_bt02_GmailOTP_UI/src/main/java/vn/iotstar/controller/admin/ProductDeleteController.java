package vn.iotstar.controller.admin;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.service.ProductService;
import vn.iotstar.service.impl.ProductServiceImpl;
import java.io.IOException;

@WebServlet("/admin/product/delete")
public class ProductDeleteController extends HttpServlet {
    private final ProductService service=new ProductServiceImpl();
    protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws IOException{
        if(!AdminAuth.requireAdmin(req,resp))return;
        try{service.delete(Integer.parseInt(req.getParameter("id")));}catch(Exception ignored){}
        resp.sendRedirect(req.getContextPath()+"/admin/product/list");
    }
}
