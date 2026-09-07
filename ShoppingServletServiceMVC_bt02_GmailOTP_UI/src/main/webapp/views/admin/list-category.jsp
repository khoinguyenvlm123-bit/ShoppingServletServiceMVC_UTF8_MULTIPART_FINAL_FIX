<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="vn.iotstar.entity.Category" %>
<%@ page import="vn.iotstar.entity.User" %>
<%@ page import="vn.iotstar.util.Constant" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản lý danh mục</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/admin.css">
</head>
<body>
<%
    List<Category> cateList = (List<Category>) request.getAttribute("cateList");
    String keyword = (String) request.getAttribute("keyword");
    User account = (User) session.getAttribute(Constant.SESSION_ACCOUNT);
    String fullName = account != null && account.getFullName() != null ? account.getFullName() : "Admin";
%>
<div class="app">
    <header class="topbar">
        <div class="brand">Dashboard</div>
        <div class="top-user"><span>Xin chào <b><%=fullName%></b></span><a class="logout" href="<%=request.getContextPath()%>/logout">Đăng xuất</a></div>
    </header>
    <aside class="sidebar">
        <div class="profile"><div class="avatar">AD</div><div class="role">Bạn là Admin</div></div>
        <nav class="menu">
            <a href="<%=request.getContextPath()%>/admin/home">Dashboard</a>
            <div class="menu-title dark">Quản lý Danh mục</div>
            <div class="submenu">
                <a href="<%=request.getContextPath()%>/admin/category/add">Thêm danh mục mới</a>
                <a href="<%=request.getContextPath()%>/admin/category/list">Danh sách danh mục</a>
            </div>
            <a href="<%=request.getContextPath()%>/admin/product/list">Quản lý sản phẩm</a>
            <a href="<%=request.getContextPath()%>/admin/account/list">Quản lý tài khoản</a>
        </nav>
    </aside>
    <main class="content">
        <section class="card">
            <div class="card-body">
                <h1 class="page-title">Quản lý danh mục</h1>
                <div class="subtitle">Nơi bạn có thể quản lý danh mục của mình</div>
                <div class="section-title">Danh sách danh mục</div>
                <div class="toolbar">
                    <div class="left-tools">
                        <select class="select"><option>10</option></select><span>records per page</span>
                        <a class="btn btn-primary" href="<%=request.getContextPath()%>/admin/category/add">+ Thêm danh mục</a>
                    </div>
                    <form class="right-tools" method="get" action="<%=request.getContextPath()%>/admin/category/list">
                        <label>Search:</label>
                        <input class="search" name="keyword" value="<%=keyword == null ? "" : keyword.replace("\"", "&quot;")%>">
                        <button class="btn" type="submit">Tìm</button>
                    </form>
                </div>
                <table>
                    <thead><tr><th style="width:65px">STT</th><th style="width:36%">Hình ảnh</th><th>Tên danh mục</th><th style="width:170px">Hành động</th></tr></thead>
                    <tbody>
                    <% if (cateList != null && !cateList.isEmpty()) { int stt = 1; for (Category cate : cateList) { %>
                    <tr>
                        <td><%=stt++%></td>
                        <td><div class="thumb-wrap">
                            <% if (cate.getIcon() != null && !cate.getIcon().isBlank()) { %>
                            <img class="thumb" src="<%=request.getContextPath()%>/image?fname=<%=java.net.URLEncoder.encode(cate.getIcon(), java.nio.charset.StandardCharsets.UTF_8).replace("%2F", "/")%>" alt="Ảnh danh mục">
                            <% } else { %><span class="no-image">Không có ảnh</span><% } %>
                        </div></td>
                        <td><%=cate.getName()%></td>
                        <td><a class="action-link" href="<%=request.getContextPath()%>/admin/category/edit?id=<%=cate.getId()%>">Sửa</a> | <a class="action-link delete" href="<%=request.getContextPath()%>/admin/category/delete?id=<%=cate.getId()%>" onclick="return confirm('Bạn có chắc muốn xóa danh mục này?')">Xóa</a></td>
                    </tr>
                    <% }} else { %><tr><td class="empty" colspan="4">Chưa có danh mục.</td></tr><% } %>
                    </tbody>
                </table>
            </div>
        </section>
    </main>
</div>
</body>
</html>
