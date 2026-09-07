package vn.iotstar.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.entity.*;
import vn.iotstar.service.*;
import vn.iotstar.service.impl.*;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/admin/product/add")
public class ProductAddController extends HttpServlet {
    private final ProductService service=new ProductServiceImpl();
    private final CategoryService categoryService=new CategoryServiceImpl();
    protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        if(!AdminAuth.requireAdmin(req,resp))return;
        req.setAttribute("categories",categoryService.findAll());
        req.getRequestDispatcher("/views/admin/add-product.jsp").forward(req,resp);
    }
    protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        if(!AdminAuth.requireAdmin(req,resp))return; req.setCharacterEncoding("UTF-8");
        try{
            Product p=new Product();
            p.setName(req.getParameter("name").trim());
            p.setPrice(new BigDecimal(req.getParameter("price")));
            p.setQuantity(Integer.parseInt(req.getParameter("quantity")));
            p.setDescription(req.getParameter("description"));
            p.setImage(req.getParameter("image"));
            Category c=categoryService.findById(Integer.parseInt(req.getParameter("categoryId")));
            if(c==null)throw new IllegalArgumentException("Danh mục không tồn tại");
            p.setCategory(c); service.insert(p); resp.sendRedirect(req.getContextPath()+"/admin/product/list");
        }catch(Exception e){req.setAttribute("error","Dữ liệu sản phẩm không hợp lệ: "+e.getMessage());doGet(req,resp);}
    }
}
