<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ page import="vn.iotstar.entity.User" %>
<%@ page import="vn.iotstar.util.Constant" %>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>Thêm danh mục</title><link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/admin.css"></head>
<body>
<% User account=(User)session.getAttribute(Constant.SESSION_ACCOUNT); String fullName=account!=null&&account.getFullName()!=null?account.getFullName():"Admin"; %>
<div class="app">
<header class="topbar"><div class="brand">Dashboard</div><div class="top-user"><span>Xin chào <b><%=fullName%></b></span><a class="logout" href="<%=request.getContextPath()%>/logout">Đăng xuất</a></div></header>
<aside class="sidebar"><div class="profile"><div class="avatar">AD</div><div class="role">Bạn là Admin</div></div><nav class="menu"><a href="<%=request.getContextPath()%>/admin/home">Dashboard</a><div class="menu-title dark">Quản lý Danh mục</div><div class="submenu"><a href="<%=request.getContextPath()%>/admin/category/add">Thêm danh mục mới</a><a href="<%=request.getContextPath()%>/admin/category/list">Danh sách danh mục</a></div><a href="<%=request.getContextPath()%>/admin/product/list">Quản lý sản phẩm</a><a href="<%=request.getContextPath()%>/admin/account/list">Quản lý tài khoản</a></nav></aside>
<main class="content"><section class="card form-card"><div class="form-head">Thêm danh mục</div><div class="form-body"><h1 class="form-title">Danh mục:</h1>
<% if(request.getAttribute("error")!=null){ %><div class="error"><%=request.getAttribute("error")%></div><% } %>
<form action="<%=request.getContextPath()%>/admin/category/add" method="post" enctype="multipart/form-data" accept-charset="UTF-8">
<div class="form-group"><label>Tên danh sách:</label><input class="input" type="text" name="name" placeholder="Nhập tên danh mục" required></div>
<div class="form-group"><label>Ảnh đại diện</label><input type="file" name="icon" accept="image/*"></div>
<button class="btn" type="submit">Thêm</button> <a class="btn btn-primary" href="<%=request.getContextPath()%>/admin/category/list">Hủy</a>
</form></div></section></main></div>
</body></html>
