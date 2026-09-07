FIX HTTP 500 + QUẢN LÝ SẢN PHẨM/TÀI KHOẢN

1. Lỗi HTTP 500 do list-category.jsp vẫn import vn.iotstar.model.Category trong khi DAO trả vn.iotstar.entity.Category. Đã đổi toàn bộ JSP sang vn.iotstar.entity.Category.
2. Đã thêm Product theo JPA: entity -> dao -> service -> controller -> view.
3. Đã thêm quản lý tài khoản: danh sách, tìm kiếm, thêm, sửa, xóa; giữ JDBC/User hiện tại để không phá chức năng đăng nhập/đăng ký.
4. Chạy lại file database/ShoppingDB_Category.sql để tạo bảng Product nếu chưa có.
5. Clean/Rebuild project và xóa deployment cũ trên Tomcat trước khi chạy lại để tránh class JSP cũ còn cache.
