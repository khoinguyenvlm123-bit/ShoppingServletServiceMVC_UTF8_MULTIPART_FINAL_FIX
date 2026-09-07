<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Đăng ký tài khoản</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/auth.css"></head>
<body><main class="auth-page"><section class="auth-shell">
  <aside class="auth-visual">
    <div class="brand"><div class="brand-mark">U</div><span>UTE Shopping</span></div>
    <div class="visual-copy"><h1>Tạo tài khoản<br>chỉ trong vài phút.</h1><p>Đăng ký tài khoản, nhận mã OTP qua Gmail và kích hoạt an toàn trước khi sử dụng hệ thống.</p>
      <div class="feature-list"><div class="feature-item"><span class="feature-icon">✓</span> OTP 6 số gửi trực tiếp qua Gmail</div><div class="feature-item"><span class="feature-icon">✓</span> Mã xác thực có hiệu lực 5 phút</div><div class="feature-item"><span class="feature-icon">✓</span> Không tạo tài khoản khi chưa xác thực</div></div>
    </div>
    <div style="position:relative;z-index:1;font-size:13px;opacity:.8">Shopping Servlet Service MVC</div>
  </aside>
  <div class="auth-panel"><div class="auth-panel-inner">
    <div class="auth-head"><h2>Tạo tài khoản mới</h2><p>Điền thông tin bên dưới. Hệ thống sẽ gửi OTP đến Gmail của bạn.</p></div>
    <% if(request.getAttribute("alert")!=null){ %><div class="alert alert-error"><%=request.getAttribute("alert")%></div><% } %>
    <form action="${pageContext.request.contextPath}/register" method="post">
      <div class="form-grid">
        <div class="form-group"><label>Tài khoản</label><div class="input-wrap"><input type="text" name="username" value="${param.username}" placeholder="Ví dụ: user01" required autofocus><span class="input-icon">👤</span></div></div>
        <div class="form-group"><label>Họ và tên</label><div class="input-wrap"><input type="text" name="fullname" value="${param.fullname}" placeholder="Nguyễn Văn A" required><span class="input-icon">✎</span></div></div>
        <div class="form-group full"><label>Email nhận OTP</label><div class="input-wrap"><input type="email" name="email" value="${param.email}" placeholder="example@gmail.com" required><span class="input-icon">✉</span></div><div class="helper">Nên dùng Gmail đang hoạt động để nhận mã xác thực ngay.</div></div>
        <div class="form-group full"><label>Số điện thoại</label><div class="input-wrap"><input type="text" name="phone" value="${param.phone}" placeholder="09xxxxxxxx" pattern="[0-9]{9,11}" required><span class="input-icon">☎</span></div></div>
        <div class="form-group"><label>Mật khẩu</label><div class="input-wrap"><input id="regPassword" type="password" name="password" minlength="6" placeholder="Tối thiểu 6 ký tự" required><button class="toggle-password" type="button" data-toggle-password="regPassword" aria-label="Hiện mật khẩu">👁</button></div></div>
        <div class="form-group"><label>Nhập lại mật khẩu</label><div class="input-wrap"><input id="regRePassword" type="password" name="repassword" minlength="6" placeholder="Nhập lại mật khẩu" required><button class="toggle-password" type="button" data-toggle-password="regRePassword" aria-label="Hiện mật khẩu">👁</button></div></div>
      </div>
      <button class="auth-btn" type="submit">Đăng ký & gửi mã OTP</button>
      <div class="auth-footer">Đã có tài khoản? <a href="${pageContext.request.contextPath}/login">Đăng nhập ngay</a></div>
    </form>
  </div></div>
</section></main><script src="${pageContext.request.contextPath}/assets/js/auth.js"></script></body></html>
