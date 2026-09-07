<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ page import="vn.iotstar.entity.User" %>
<%
    User user = (User) request.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    String success = (String) session.getAttribute("profileSuccess");
    if (success != null) session.removeAttribute("profileSuccess");
    String avatarSrc = null;
    if (user.getAvatar() != null && !user.getAvatar().isBlank()) {
        String av = user.getAvatar();
        avatarSrc = av.startsWith("http://") || av.startsWith("https://")
                ? av
                : request.getContextPath() + "/image?fname=" + java.net.URLEncoder.encode(av, java.nio.charset.StandardCharsets.UTF_8).replace("%2F", "/");
    }
%>
<!DOCTYPE html>
<html>
<head><title>Thông tin cá nhân</title></head>
<body>
<section class="profile-card">
    <div class="profile-heading">
        <div>
            <h1>Thông tin cá nhân</h1>
            <p>Cập nhật họ tên, số điện thoại và ảnh đại diện của bạn.</p>
        </div>
    </div>

    <% if (success != null) { %><div class="alert-success"><%=success%></div><% } %>
    <% if (request.getAttribute("error") != null) { %><div class="alert-error"><%=request.getAttribute("error")%></div><% } %>

    <form action="<%=request.getContextPath()%>/profile" method="post" enctype="multipart/form-data" accept-charset="UTF-8" class="profile-form">
        <div class="avatar-panel">
            <div class="avatar-preview-wrap">
                <% if (avatarSrc != null) { %>
                    <img id="avatarPreview" class="avatar-preview" src="<%=avatarSrc%>" alt="Avatar">
                <% } else { %>
                    <div id="avatarPlaceholder" class="avatar-placeholder">Ảnh</div>
                    <img id="avatarPreview" class="avatar-preview hidden" alt="Avatar">
                <% } %>
            </div>
            <label class="btn-upload" for="images">Chọn ảnh mới</label>
            <input id="images" name="images" type="file" accept="image/png,image/jpeg,image/gif,image/webp" hidden>
            <small>JPG, PNG, GIF, WEBP. Tối đa 5 MB.</small>
        </div>

        <div class="profile-fields">
            <div class="form-row">
                <label>Tên đăng nhập</label>
                <input type="text" value="<%=user.getUserName()%>" disabled>
            </div>
            <div class="form-row">
                <label>Email</label>
                <input type="email" value="<%=user.getEmail()%>" disabled>
            </div>
            <div class="form-row">
                <label for="fullname">Họ và tên <span>*</span></label>
                <input id="fullname" name="fullname" type="text" maxlength="150" required value="<%=user.getFullName() == null ? "" : user.getFullName()%>">
            </div>
            <div class="form-row">
                <label for="phone">Số điện thoại</label>
                <input id="phone" name="phone" type="tel" maxlength="15" value="<%=user.getPhone() == null ? "" : user.getPhone()%>" placeholder="Ví dụ: 0912345678">
            </div>
            <div class="form-actions">
                <a class="btn-secondary" href="<%=request.getContextPath()%>/home">Hủy</a>
                <button class="btn-save" type="submit">Lưu thay đổi</button>
            </div>
        </div>
    </form>
</section>
<script>
    const input = document.getElementById('images');
    const preview = document.getElementById('avatarPreview');
    input.addEventListener('change', function () {
        const file = this.files && this.files[0];
        if (!file) return;
        preview.src = URL.createObjectURL(file);
        preview.classList.remove('hidden');
        const placeholder = document.getElementById('avatarPlaceholder');
        if (placeholder) placeholder.style.display = 'none';
    });
</script>
</body>
</html>
