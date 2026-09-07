<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ page import="java.util.List,vn.iotstar.entity.PurchaseOrder" %>
<!DOCTYPE html>
<html lang="vi">
<head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Đơn mua của tôi</title></head>
<body>
<% List<PurchaseOrder> orders=(List<PurchaseOrder>)request.getAttribute("orders"); %>
<div class="shop-container">
    <h1>Đơn mua của tôi</h1>
    <p class="shop-muted">Danh sách sản phẩm bạn đã mua bằng chức năng mua hàng JPA.</p>
    <div class="order-table-wrap">
        <table class="order-table">
            <thead><tr><th>Mã đơn</th><th>Sản phẩm</th><th>Số lượng</th><th>Đơn giá</th><th>Thành tiền</th><th>Trạng thái</th><th>Ngày đặt</th></tr></thead>
            <tbody>
            <% if(orders==null||orders.isEmpty()){ %>
                <tr><td colspan="7" class="empty">Bạn chưa có đơn mua nào.</td></tr>
            <% } else { for(PurchaseOrder o:orders){ %>
                <tr>
                    <td>#<%=o.getId()%></td>
                    <td><a href="<%=request.getContextPath()%>/product/detail?id=<%=o.getProduct().getId()%>"><%=o.getProduct().getName()%></a></td>
                    <td><%=o.getQuantity()%></td>
                    <td><%=String.format("%,.0f",o.getUnitPrice())%> đ</td>
                    <td><b><%=String.format("%,.0f",o.getTotalPrice())%> đ</b></td>
                    <td>Đã đặt</td>
                    <td><%=o.getCreatedAt()%></td>
                </tr>
            <% }} %>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
