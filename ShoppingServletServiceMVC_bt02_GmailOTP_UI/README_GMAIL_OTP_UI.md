# Bổ sung giao diện + OTP Gmail

## 1. Giao diện đã làm lại
- Đăng nhập
- Đăng ký
- Quên mật khẩu
- Nhập OTP đăng ký
- Nhập OTP quên mật khẩu
- Đặt lại mật khẩu
- Giao diện responsive cho desktop/mobile
- Có nút hiện/ẩn mật khẩu
- OTP hiển thị 6 ô riêng, hỗ trợ dán cả 6 số
- Có đồng hồ đếm ngược thời hạn OTP
- Có chức năng gửi lại OTP, giới hạn 60 giây/lần
- Email hiển thị dạng che bớt ký tự để bảo mật

## 2. Cấu hình gửi OTP bằng Gmail
File cấu hình local:

`src/main/resources/mail.properties`

Nội dung:

```properties
mail.username=YOUR_GMAIL@gmail.com
mail.password=YOUR_APP_PASSWORD_16_CHARS
mail.fromName=UTE Shopping
mail.host=smtp.gmail.com
mail.port=587
```

### Cách lấy Gmail App Password
1. Vào Google Account.
2. Bật **2-Step Verification / Xác minh 2 bước**.
3. Tìm **App passwords / Mật khẩu ứng dụng**.
4. Tạo một App Password cho ứng dụng Java/Tomcat.
5. Copy 16 ký tự và gán vào `mail.password`.
6. Có thể dán App Password có khoảng trắng; code sẽ tự bỏ khoảng trắng.

> Không dùng mật khẩu Gmail đăng nhập thông thường.

## 3. Không làm lộ mật khẩu Gmail trên GitHub
`src/main/resources/mail.properties` đã được thêm vào `.gitignore`.

Project có file mẫu:

`src/main/resources/mail.properties.example`

Khi push GitHub, chỉ file `.example` được đưa lên. File chứa Gmail/App Password thật không được commit.

Nếu file `mail.properties` đã từng được Git track trước đó, chạy:

```bash
git rm --cached src/main/resources/mail.properties
```

sau đó commit lại.

## 4. Có thể dùng VM Options thay cho file properties
Code vẫn hỗ trợ:

```text
-Dshopping.mail.username=yourgmail@gmail.com
-Dshopping.mail.password=YOUR_APP_PASSWORD
```

Hoặc environment variables:

```text
SHOPPING_MAIL_USERNAME
SHOPPING_MAIL_PASSWORD
```

Thứ tự ưu tiên: VM Options -> Environment Variables -> mail.properties.

## 5. Luồng đăng ký OTP
`/register`
-> gửi OTP Gmail
-> `/verify-register-otp`
-> OTP đúng
-> tạo tài khoản
-> `/login?activated=1`

OTP hết hạn sau 5 phút. Gửi lại OTP tối đa 1 lần/60 giây.

## 6. Luồng quên mật khẩu OTP
`/forgot-password`
-> gửi OTP Gmail
-> `/verify-forgot-otp`
-> xác minh thành công
-> `/reset-password`
-> đăng nhập bằng mật khẩu mới.

## 7. Lỗi Gmail thường gặp
- `Username and Password not accepted`: đang dùng mật khẩu Gmail thường hoặc App Password sai.
- Không có mục App Password: cần bật 2-Step Verification; tài khoản Google Workspace của trường/công ty cũng có thể chặn tính năng này.
- Timeout: kiểm tra Internet/firewall và SMTP port 587.
- Không thấy OTP: kiểm tra Spam/Thư rác.
