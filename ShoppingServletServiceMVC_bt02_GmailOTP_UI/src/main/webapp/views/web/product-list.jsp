<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ page import="java.util.List,vn.iotstar.entity.Product" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <title>Sản phẩm</title>
</head>
<body>
<%
    List<Product> products=(List<Product>)request.getAttribute("products");
    int pageNo=(Integer)request.getAttribute("page");
    int totalPages=(Integer)request.getAttribute("totalPages");
%>
<div class="shop-container">
    <h1>Tất cả sản phẩm</h1>
    <p class="shop-muted">6 sản phẩm mỗi trang - bấm vào sản phẩm để xem đánh giá và mua hàng.</p>
    <div class="product-grid">
        <% if(products!=null) for(Product p:products){
            String img=p.getImage();
            String src=(img==null||img.isBlank())?null:(img.startsWith("http://")||img.startsWith("https://")?img:request.getContextPath()+"/image?fname="+java.net.URLEncoder.encode(img,java.nio.charset.StandardCharsets.UTF_8).replace("%2F","/"));
        %>
        <div class="product-card">
            <a class="product-main-link" href="<%=request.getContextPath()%>/product/detail?id=<%=p.getId()%>">
                <div class="product-image">
                    <%if(src!=null){%><img src="<%=src%>" alt="<%=p.getName()%>"><%}else{%><span>Không có ảnh</span><%}%>
                </div>
                <div class="product-body">
                    <h3><%=p.getName()%></h3>
                    <p class="price"><%=String.format("%,.0f",p.getPrice())%> đ</p>
                    <p>Còn <%=p.getQuantity()%> sản phẩm</p>
                </div>
            </a>
            <div class="card-actions">
                <a href="<%=request.getContextPath()%>/product/detail?id=<%=p.getId()%>#rating">★ Đánh giá</a>
                <a href="<%=request.getContextPath()%>/product/detail?id=<%=p.getId()%>#buy">🛒 Mua hàng</a>
            </div>
        </div>
        <% } %>
    </div>
    <div class="pagination">
        <% for(int i=1;i<=totalPages;i++){ %>
        <a class="<%=i==pageNo?"current":""%>" href="<%=request.getContextPath()%>/product?page=<%=i%>"><%=i%></a>
        <% } %>
    </div>
</div>
</body>
</html>
