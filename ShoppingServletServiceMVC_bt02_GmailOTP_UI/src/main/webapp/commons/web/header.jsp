<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ page import="vn.iotstar.entity.User,vn.iotstar.util.Constant" %>
<%
    User headerUser = (User) session.getAttribute(Constant.SESSION_ACCOUNT);
%>
<header class="shop-header">
    <a class="shop-brand" href="<%=request.getContextPath()%>/home">Shopping MVC</a>
    <nav>
        <a href="<%=request.getContextPath()%>/home">Trang chủ</a>
        <a href="<%=request.getContextPath()%>/product">Sản phẩm</a>
        <% if (headerUser != null) { %>
            <a href="<%=request.getContextPath()%>/profile">Thông tin cá nhân</a>
            <a href="<%=request.getContextPath()%>/order/my">Đơn mua</a>
            <a href="<%=request.getContextPath()%>/logout">Đăng xuất</a>
        <% } else { %>
            <a href="<%=request.getContextPath()%>/login">Đăng nhập</a>
        <% } %>
    </nav>
</header>
