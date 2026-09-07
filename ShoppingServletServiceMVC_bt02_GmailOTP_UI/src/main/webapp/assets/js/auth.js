document.addEventListener('DOMContentLoaded', function () {
  document.querySelectorAll('[data-toggle-password]').forEach(function (button) {
    button.addEventListener('click', function () {
      var input = document.getElementById(button.dataset.togglePassword);
      if (!input) return;
      var show = input.type === 'password';
      input.type = show ? 'text' : 'password';
      button.textContent = show ? '🙈' : '👁';
      button.setAttribute('aria-label', show ? 'Ẩn mật khẩu' : 'Hiện mật khẩu');
    });
  });

  var digits = Array.from(document.querySelectorAll('.otp-digit'));
  var hidden = document.getElementById('otpValue');
  if (digits.length && hidden) {
    function syncOtp() { hidden.value = digits.map(function (x) { return x.value; }).join(''); }
    digits.forEach(function (input, index) {
      input.addEventListener('input', function () {
        input.value = input.value.replace(/\D/g, '').slice(-1);
        if (input.value && index < digits.length - 1) digits[index + 1].focus();
        syncOtp();
      });
      input.addEventListener('keydown', function (e) {
        if (e.key === 'Backspace' && !input.value && index > 0) digits[index - 1].focus();
        if (e.key === 'ArrowLeft' && index > 0) digits[index - 1].focus();
        if (e.key === 'ArrowRight' && index < digits.length - 1) digits[index + 1].focus();
      });
      input.addEventListener('paste', function (e) {
        var text = (e.clipboardData || window.clipboardData).getData('text').replace(/\D/g, '').slice(0, 6);
        if (!text) return;
        e.preventDefault();
        digits.forEach(function (d, i) { d.value = text[i] || ''; });
        syncOtp();
        digits[Math.min(text.length, 6) - 1].focus();
      });
    });
    var form = hidden.closest('form');
    if (form) form.addEventListener('submit', function (e) { syncOtp(); if (hidden.value.length !== 6) { e.preventDefault(); digits[0].focus(); } });
  }

  var timer = document.querySelector('[data-otp-expire]');
  if (timer) {
    var expire = Number(timer.dataset.otpExpire || 0);
    function tick() {
      var left = Math.max(0, Math.floor((expire - Date.now()) / 1000));
      var m = Math.floor(left / 60), s = left % 60;
      timer.textContent = left > 0 ? m + ':' + String(s).padStart(2, '0') : 'đã hết hạn';
      if (left > 0) setTimeout(tick, 1000);
    }
    tick();
  }

  document.querySelectorAll('[data-resend-button]').forEach(function (button) {
    var wait = Number(button.dataset.cooldown || 60);
    var n = wait;
    function render() {
      if (n <= 0) { button.disabled = false; button.textContent = 'Gửi lại mã OTP'; return; }
      button.disabled = true; button.textContent = 'Gửi lại sau ' + n + 's'; n--; setTimeout(render, 1000);
    }
    render();
  });
});
