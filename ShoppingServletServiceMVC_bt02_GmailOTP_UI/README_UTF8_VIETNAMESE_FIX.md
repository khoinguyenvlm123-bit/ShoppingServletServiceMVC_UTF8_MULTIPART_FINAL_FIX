# FIX tiếng Việt có dấu toàn project

Đã bổ sung `vn.iotstar.filter.EncodingFilter` và map `/*` trong `WEB-INF/web.xml`.
Filter chạy trước SiteMesh và ép cả request/response về UTF-8, vì vậy các form ở Profile,
Admin Category, Product, Account, Rating... đều dùng cùng một encoding.

Ngoài ra:
- Toàn bộ JSP có `pageEncoding="UTF-8"`.
- Maven source/report encoding là UTF-8.
- Entity User `fullname` dùng `nvarchar`.
- Có script `database/ShoppingDB_UTF8_Unicode_FIX.sql` để đổi các cột nhập tiếng Việt sang NVARCHAR.

## Sau khi thay project
1. Maven -> Reload All Maven Projects.
2. Build -> Rebuild Project.
3. Stop Tomcat và redeploy artifact `war exploded`.
4. Nếu dữ liệu vừa nhập vẫn lỗi dấu, chạy `database/ShoppingDB_UTF8_Unicode_FIX.sql` trên SQL Server.

Lưu ý: chuỗi đã lưu sai trước đây (ví dụ `Nguyá»…`) không thể tự khôi phục chính xác chỉ bằng đổi encoding;
hãy nhập lại dữ liệu đó sau khi áp dụng bản fix.
