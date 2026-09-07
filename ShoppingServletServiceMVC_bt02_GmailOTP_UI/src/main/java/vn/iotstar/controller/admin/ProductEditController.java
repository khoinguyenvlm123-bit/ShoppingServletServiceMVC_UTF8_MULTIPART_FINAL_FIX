package vn.iotstar.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.entity.*;
import vn.iotstar.service.*;
import vn.iotstar.service.impl.*;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/admin/product/edit")
public class ProductEditController extends HttpServlet {
    private final ProductService service=new ProductServiceImpl();
    private final CategoryService categoryService=new CategoryServiceImpl();
    protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        if(!AdminAuth.requireAdmin(req,resp))return;
        int id=Integer.parseInt(req.getParameter("id")); Product p=service.findById(id);
        if(p==null){resp.sendError(404);return;}
        req.setAttribute("product",p); req.setAttribute("categories",categoryService.findAll());
        req.getRequestDispatcher("/views/admin/edit-product.jsp").forward(req,resp);
    }
    protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        if(!AdminAuth.requireAdmin(req,resp))return; req.setCharacterEncoding("UTF-8");
        try{
            int id=Integer.parseInt(req.getParameter("id")); Product p=service.findById(id);
            if(p==null){resp.sendError(404);return;}
            p.setName(req.getParameter("name").trim()); p.setPrice(new BigDecimal(req.getParameter("price")));
            p.setQuantity(Integer.parseInt(req.getParameter("quantity"))); p.setDescription(req.getParameter("description"));
            p.setImage(req.getParameter("image")); p.setCategory(categoryService.findById(Integer.parseInt(req.getParameter("categoryId"))));
            service.update(p); resp.sendRedirect(req.getContextPath()+"/admin/product/list");
        }catch(Exception e){req.setAttribute("error","Không thể cập nhật sản phẩm: "+e.getMessage());doGet(req,resp);}
    }
}
