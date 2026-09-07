<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%
String flashError=(String)session.getAttribute("otpFlashError");
String flashSuccess=(String)session.getAttribute("otpFlashSuccess");
session.removeAttribute("otpFlashError"); session.removeAttribute("otpFlashSuccess");
Long expire=(Long)request.getAttribute("otpExpire"); if(expire==null) expire=0L;
Long resendWait=(Long)request.getAttribute("resendWait"); if(resendWait==null) resendWait=0L;
%>
<!DOCTYPE html><html lang="vi"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Xác nhận OTP đăng ký</title><link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/auth.css"></head>
<body><main class="auth-page"><section class="auth-shell compact-shell"><aside class="auth-visual"><div class="brand"><div class="brand-mark">U</div><span>UTE Shopping</span></div><div class="visual-copy"><h1>Kiểm tra<br>Gmail của bạn.</h1><p>Một mã xác thực 6 số vừa được gửi đến email đăng ký. Nhập đúng OTP để hoàn tất kích hoạt tài khoản.</p><div class="feature-list"><div class="feature-item"><span class="feature-icon">🔒</span> Không chia sẻ OTP cho người khác</div><div class="feature-item"><span class="feature-icon">⏱</span> OTP tự hết hạn sau 5 phút</div></div></div><div></div></aside>
<div class="auth-panel"><a class="back-home" href="${pageContext.request.contextPath}/register">← Đổi thông tin đăng ký</a><div class="auth-panel-inner otp-card"><div class="mail-circle">✉</div><div class="auth-head"><h2>Xác minh email</h2><p>Mã OTP đã gửi đến <span class="otp-email">${maskedEmail}</span></p></div>
<% if(request.getAttribute("alert")!=null){ %><div class="alert alert-error" style="text-align:left"><%=request.getAttribute("alert")%></div><% } %>
<% if(flashError!=null){ %><div class="alert alert-error" style="text-align:left"><%=flashError%></div><% } %>
<% if(flashSuccess!=null){ %><div class="alert alert-success" style="text-align:left"><%=flashSuccess%></div><% } %>
<form method="post" action="${pageContext.request.contextPath}/verify-register-otp"><input type="hidden" name="otp" id="otpValue"><div class="otp-inputs"><input class="otp-digit" inputmode="numeric" maxlength="1" autofocus><input class="otp-digit" inputmode="numeric" maxlength="1"><input class="otp-digit" inputmode="numeric" maxlength="1"><input class="otp-digit" inputmode="numeric" maxlength="1"><input class="otp-digit" inputmode="numeric" maxlength="1"><input class="otp-digit" inputmode="numeric" maxlength="1"></div><div class="otp-meta">Mã hết hạn sau <strong data-otp-expire="<%=expire%>">--:--</strong></div><button class="auth-btn" type="submit">Xác nhận & kích hoạt tài khoản</button></form>
<form class="resend-form" method="post" action="${pageContext.request.contextPath}/resend-register-otp"><span style="font-size:14px;color:#7a8699">Chưa nhận được email? </span><button class="link-button" data-resend-button data-cooldown="<%=resendWait%>" type="submit">Gửi lại mã OTP</button></form>
<div class="small-note" style="margin-top:20px;text-align:left">Nếu không thấy email, hãy kiểm tra thư mục <b>Spam/Thư rác</b> và chắc chắn địa chỉ Gmail bạn nhập là chính xác.</div></div></div></section></main><script src="${pageContext.request.contextPath}/assets/js/auth.js"></script></body></html>
