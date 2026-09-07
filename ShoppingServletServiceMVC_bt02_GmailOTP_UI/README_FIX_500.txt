Bản sửa lỗi HTTP 500 sau đăng nhập admin.

Đã sửa:
1. Loại bỏ JSTL/taglib khỏi login.jsp, register.jsp, home.jsp để tránh lỗi Jasper/JSTL khi deploy Tomcat.
2. home.jsp lấy User trực tiếp từ request và kiểm tra null trước khi hiển thị.
3. Giữ session thống nhất với key "account".
4. Giữ database ShoppingDB và tài khoản SQL Server như project gốc: sa / 123.
5. Không kèm thư mục target cũ để tránh Tomcat chạy class cũ.

Chạy với Java 17 + Tomcat 10.1+.
Sau khi mở project: Maven Reload -> Build/Rebuild -> xóa artifact/deployment cũ trên Tomcat -> deploy lại project.
