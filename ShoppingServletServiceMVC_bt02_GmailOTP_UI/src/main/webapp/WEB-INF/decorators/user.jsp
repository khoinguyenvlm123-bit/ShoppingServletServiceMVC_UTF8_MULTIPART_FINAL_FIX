<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><sitemesh:write property="title"/></title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/admin.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/profile.css">
    <sitemesh:write property="head"/>
</head>
<body>
    <%@ include file="/commons/web/header.jsp" %>
    <main class="sitemesh-content">
        <sitemesh:write property="body"/>
    </main>
    <%@ include file="/commons/web/footer.jsp" %>
</body>
</html>
