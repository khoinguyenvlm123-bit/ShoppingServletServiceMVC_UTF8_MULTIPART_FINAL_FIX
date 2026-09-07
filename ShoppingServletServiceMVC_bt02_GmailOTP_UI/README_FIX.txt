SHOPPING SERVLET SERVICE MVC - BAN DA SUA

1. Database dùng: ShoppingDB, bảng [User] đúng theo script bạn đã tạo.
2. Tài khoản test admin: admin / 123456.
3. Kết nối SQL Server mặc định trong DBConnection.java:
   - server: localhost:1433
   - database: ShoppingDB
   - user: sa
   - password: 123
   Nếu máy bạn dùng mật khẩu sa khác thì chỉ đổi PASSWORD trong DBConnection.java
   hoặc truyền system property shopping.db.password.
4. Project dùng Java 17 + Jakarta Servlet 6.0, phù hợp Tomcat 10.1+.
5. Các phần đã sửa:
   - Hoàn thiện insert/check email/check username/check phone trong UserDaoImpl.
   - Đồng bộ tham số register.
   - Kiểm tra nhập lại mật khẩu.
   - Đồng bộ session bằng key account.
   - Remember-me khôi phục User thật vào session.
   - Logout xóa cả session và cookie.
   - Sửa redirect theo role qua /waiting.
   - Sửa welcome page để đi qua /login controller.
   - Cập nhật web.xml theo Jakarta Servlet 6.

Chạy project, truy cập context root hoặc /login.
