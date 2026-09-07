<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ page import="vn.iotstar.entity.Product,vn.iotstar.entity.ProductRating,vn.iotstar.entity.User,vn.iotstar.util.Constant,java.util.List" %>
<%!
    private String esc(String s) {
        if (s == null) return "";
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;").replace("\"","&quot;").replace("'","&#39;");
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <title>Chi tiết sản phẩm</title>
</head>
<body>
<%
    Product p=(Product)request.getAttribute("product");
    List<ProductRating> ratings=(List<ProductRating>)request.getAttribute("ratings");
    ProductRating myRating=(ProductRating)request.getAttribute("myRating");
    double averageStars=(Double)request.getAttribute("averageStars");
    long ratingCount=(Long)request.getAttribute("ratingCount");
    User account=(User)session.getAttribute(Constant.SESSION_ACCOUNT);
    String img=p.getImage();
    String src=(img==null||img.isBlank())?null:(img.startsWith("http://")||img.startsWith("https://")?img:request.getContextPath()+"/image?fname="+java.net.URLEncoder.encode(img,java.nio.charset.StandardCharsets.UTF_8).replace("%2F","/"));
    String message=(String)session.getAttribute("productMessage"); if(message!=null)session.removeAttribute("productMessage");
    String error=(String)session.getAttribute("productError"); if(error!=null)session.removeAttribute("productError");
%>
<div class="shop-container">
    <a class="back-link" href="<%=request.getContextPath()%>/product">← Quay lại danh sách</a>
    <% if(message!=null){ %><div class="shop-alert success"><%=esc(message)%></div><% } %>
    <% if(error!=null){ %><div class="shop-alert error-box"><%=esc(error)%></div><% } %>

    <section class="detail-card">
        <div class="detail-image">
            <%if(src!=null){%><img src="<%=src%>" alt="<%=esc(p.getName())%>"><%}else{%><span>Không có ảnh</span><%}%>
        </div>
        <div class="detail-info">
            <div class="category-pill"><%=p.getCategory()!=null?esc(p.getCategory().getName()):"Chưa phân loại"%></div>
            <h1><%=esc(p.getName())%></h1>
            <div class="rating-summary"><span class="stars-text">★</span> <b><%=String.format("%.1f",averageStars)%>/5</b> <span>(<%=ratingCount%> đánh giá)</span></div>
            <div class="detail-price"><%=String.format("%,.0f",p.getPrice())%> đ</div>
            <p><b>Số lượng còn:</b> <%=p.getQuantity()%></p>
            <p><b>Mô tả:</b><br><%=p.getDescription()==null||p.getDescription().isBlank()?"Chưa có mô tả.":esc(p.getDescription())%></p>

            <div class="buy-box" id="buy">
                <h3>Mua hàng</h3>
                <% if(account==null){ %>
                    <p>Bạn cần <a href="<%=request.getContextPath()%>/login">đăng nhập</a> để mua sản phẩm.</p>
                <% } else if(p.getQuantity()<=0){ %>
                    <button class="buy-btn" disabled>Hết hàng</button>
                <% } else { %>
                    <form action="<%=request.getContextPath()%>/order/buy" method="post" class="buy-form">
                        <input type="hidden" name="productId" value="<%=p.getId()%>">
                        <label for="quantity">Số lượng</label>
                        <input id="quantity" name="quantity" type="number" value="1" min="1" max="<%=p.getQuantity()%>" required>
                        <button class="buy-btn" type="submit">Mua ngay</button>
                    </form>
                <% } %>
            </div>
        </div>
    </section>

    <section class="rating-section" id="rating">
        <div class="rating-column">
            <h2>Đánh giá sản phẩm</h2>
            <% if(account==null){ %>
                <p>Bạn cần <a href="<%=request.getContextPath()%>/login">đăng nhập</a> để đánh giá.</p>
            <% } else { %>
                <form action="<%=request.getContextPath()%>/rating/save" method="post" class="rating-form">
                    <input type="hidden" name="productId" value="<%=p.getId()%>">
                    <label for="stars">Số sao</label>
                    <select id="stars" name="stars" required>
                        <% for(int s=5;s>=1;s--){ %><option value="<%=s%>" <%=myRating!=null&&myRating.getStars()==s?"selected":""%>><%=s%> sao</option><% } %>
                    </select>
                    <label for="comment">Nhận xét</label>
                    <textarea id="comment" name="comment" maxlength="1000" placeholder="Chia sẻ cảm nhận của bạn..."><%=myRating==null?"":esc(myRating.getComment())%></textarea>
                    <button class="rating-btn" type="submit"><%=myRating==null?"Gửi đánh giá":"Cập nhật đánh giá"%></button>
                </form>
            <% } %>
        </div>
        <div class="rating-column">
            <h2>Nhận xét (<%=ratingCount%>)</h2>
            <% if(ratings==null||ratings.isEmpty()){ %>
                <p class="shop-muted">Chưa có đánh giá nào cho sản phẩm này.</p>
            <% } else { for(ProductRating r:ratings){ %>
                <article class="review-item">
                    <div class="review-head"><b><%=esc(r.getUser().getFullName()==null?r.getUser().getUserName():r.getUser().getFullName())%></b><span class="review-stars"><% for(int i=1;i<=5;i++){ %><%=i<=r.getStars()?"★":"☆"%><% } %></span></div>
                    <p><%=r.getComment()==null||r.getComment().isBlank()?"Không có nhận xét.":esc(r.getComment())%></p>
                </article>
            <% }} %>
        </div>
    </section>
</div>
</body>
</html>
