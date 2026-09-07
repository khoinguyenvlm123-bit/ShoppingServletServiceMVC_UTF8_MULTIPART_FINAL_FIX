# Bài tập 02 - OTP, Login, Forgot Password, Product

## Chức năng đã bổ sung
1. Đăng ký tài khoản và gửi OTP 6 số qua email; chỉ tạo tài khoản sau khi OTP đúng.
2. Đăng nhập/đăng xuất/remember account theo source MVC hiện có.
3. Quên mật khẩu: nhập email -> nhận OTP -> xác nhận OTP -> đặt mật khẩu mới.
4. Product liên kết Many-to-One với Category:
   - CRUD Product cho Admin.
   - Home hiển thị 10 sản phẩm mới nhất.
   - `/product` hiển thị tất cả sản phẩm, 6 sản phẩm/trang.
   - `/product/detail?id=...` hiển thị chi tiết sản phẩm.

## Database
Database mặc định: `ServletCRUDMVC`.
Chạy file:
- `database/ShoppingDB_Category.sql` nếu tạo DB mới; hoặc
- `database/ShoppingDB_Update_Category_Product_Account.sql` nếu DB/Category/User đã có.

Cấu hình DB nằm trong `vn.iotstar.connection.JPAConfig`:
- URL mặc định: `jdbc:sqlserver://localhost:1433;databaseName=ServletCRUDMVC;encrypt=false;trustServerCertificate=true`
- user mặc định: `sa`
- password mặc định: `123`

Có thể đổi bằng VM options:
`-Dshopping.db.url=... -Dshopping.db.user=... -Dshopping.db.password=...`

## Cấu hình Gmail để gửi OTP
Không ghi Gmail/App Password trực tiếp vào source hoặc GitHub.

### 1. Tạo Gmail App Password
Bật xác minh 2 bước cho tài khoản Google, sau đó tạo App Password cho ứng dụng.

### 2. Thêm VM options khi chạy Tomcat trong IntelliJ
Run -> Edit Configurations -> Tomcat Server -> VM options:

`-Dshopping.mail.username=YOUR_EMAIL@gmail.com -Dshopping.mail.password=YOUR_16_CHAR_APP_PASSWORD`

Mặc định source dùng:
- SMTP: `smtp.gmail.com`
- port: `587`
- STARTTLS: bật

Có thể đổi thêm:
`-Dshopping.mail.host=... -Dshopping.mail.port=... -Dshopping.mail.from=...`

## URL để test
- `/register` - đăng ký và gửi OTP
- `/verify-register-otp` - kích hoạt tài khoản
- `/login` - đăng nhập
- `/forgot-password` - yêu cầu OTP quên mật khẩu
- `/verify-forgot-otp` - xác nhận OTP
- `/reset-password` - đổi mật khẩu
- `/home` - 10 sản phẩm mới nhất sau khi đăng nhập
- `/product` - danh sách, 6 sản phẩm/trang
- `/product/detail?id=1` - chi tiết sản phẩm
- `/admin/product/list` - CRUD Product cho Admin
- `/admin/category/list` - CRUD Category
- `/admin/account/list` - CRUD Account

## Tài khoản admin mẫu
Nếu chạy SQL mẫu:
- username: `admin`
- password: `123456`

## Chạy project
1. Mở project bằng IntelliJ IDEA.
2. Load/Reimport Maven để tải dependency mới `org.eclipse.angus:angus-mail:2.0.3`.
3. Cấu hình Tomcat 11.
4. Thêm VM options cho DB/email như trên.
5. Run Tomcat và test các URL.

## Trước khi push GitHub
Không commit Gmail App Password. Chỉ commit source + pom.xml + SQL + README.
