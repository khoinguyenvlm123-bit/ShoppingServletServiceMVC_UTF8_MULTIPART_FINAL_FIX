package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.service.ProductService;
import vn.iotstar.service.impl.ProductServiceImpl;
import java.io.IOException;

@WebServlet("/product")
public class ProductController extends HttpServlet {
    private final ProductService service=new ProductServiceImpl();
    @Override protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
        final int pageSize=6;
        int page=1;
        try{page=Integer.parseInt(req.getParameter("page"));}catch(Exception ignored){}
        if(page<1)page=1;
        long total=service.count();
        int totalPages=(int)Math.ceil(total/(double)pageSize);
        if(totalPages>0&&page>totalPages)page=totalPages;
        req.setAttribute("products",service.findPage(page,pageSize));
        req.setAttribute("page",page);
        req.setAttribute("totalPages",totalPages);
        req.setAttribute("totalProducts",total);
        req.getRequestDispatcher("/views/web/product-list.jsp").forward(req,resp);
    }
}
