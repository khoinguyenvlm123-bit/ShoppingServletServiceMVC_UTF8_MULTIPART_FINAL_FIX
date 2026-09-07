<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ page import="vn.iotstar.entity.User,vn.iotstar.entity.Product,java.util.List" %>
<!DOCTYPE html><html><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Trang chủ</title><link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/admin.css"></head><body>
<% User currentUser=(User)request.getAttribute("user"); if(currentUser==null){response.sendRedirect(request.getContextPath()+"/login");return;} List<Product> latest=(List<Product>)request.getAttribute("latestProducts"); %>
<div class="app"><header class="topbar"><div class="brand">Shopping MVC</div><div class="top-user"><span>Xin chào <b><%=currentUser.getFullName()%></b></span><a class="logout" href="<%=request.getContextPath()%>/logout">Đăng xuất</a></div></header>
<%
String homeAvatarSrc = null;
if (currentUser.getAvatar() != null && !currentUser.getAvatar().isBlank()) {
    String avatar = currentUser.getAvatar();
    homeAvatarSrc = (avatar.startsWith("http://") || avatar.startsWith("https://"))
            ? avatar
            : request.getContextPath() + "/image?fname="
                + java.net.URLEncoder.encode(avatar, java.nio.charset.StandardCharsets.UTF_8).replace("%2F", "/");
}
%>
<aside class="sidebar"><div class="profile"><div class="avatar"><% if (homeAvatarSrc != null) { %><img src="<%=homeAvatarSrc%>" alt="Ảnh đại diện"><% } else { %><%=currentUser.getRoleid()==1?"AD":"US"%><% } %></div><div class="role">Bạn là <%=currentUser.getRoleid()==1?"Admin":"User"%></div></div><nav class="menu"><a class="active" href="<%=request.getContextPath()%>/home">Trang chủ</a><a href="<%=request.getContextPath()%>/profile">Thông tin cá nhân</a><a href="<%=request.getContextPath()%>/product">Tất cả sản phẩm</a><% if(currentUser.getRoleid()==1){ %><div class="menu-title dark">Quản lý Danh mục</div><div class="submenu"><a href="<%=request.getContextPath()%>/admin/category/add">Thêm danh mục mới</a><a href="<%=request.getContextPath()%>/admin/category/list">Danh sách danh mục</a></div><a href="<%=request.getContextPath()%>/admin/product/list">Quản lý sản phẩm</a><a href="<%=request.getContextPath()%>/admin/account/list">Quản lý tài khoản</a><% } %></nav></aside>
<main class="content"><section class="card"><div class="home-box"><h2>Xin chào, <%=currentUser.getFullName()%></h2><p>Đây là 10 sản phẩm mới nhất.</p><div class="product-grid compact-grid">
<% if(latest!=null&&!latest.isEmpty()){ for(Product p:latest){ String img=p.getImage(); String src=(img==null||img.isBlank())?null:(img.startsWith("http://")||img.startsWith("https://")?img:request.getContextPath()+"/image?fname="+java.net.URLEncoder.encode(img,java.nio.charset.StandardCharsets.UTF_8).replace("%2F","/")); %>
<a class="product-card" href="<%=request.getContextPath()%>/product/detail?id=<%=p.getId()%>"><div class="product-image"><%if(src!=null){%><img src="<%=src%>" alt="<%=p.getName()%>"><%}else{%><span>Không có ảnh</span><%}%></div><div class="product-body"><h3><%=p.getName()%></h3><p class="price"><%=String.format("%,.0f",p.getPrice())%> đ</p><p><%=p.getCategory()!=null?p.getCategory().getName():""%></p></div></a>
<% }}else{ %><p>Chưa có sản phẩm. Admin hãy thêm sản phẩm trong mục Quản lý sản phẩm.</p><% } %></div><div style="margin-top:18px"><a class="btn btn-primary" href="<%=request.getContextPath()%>/product">Xem tất cả sản phẩm</a></div></div></section></main></div></body></html>
