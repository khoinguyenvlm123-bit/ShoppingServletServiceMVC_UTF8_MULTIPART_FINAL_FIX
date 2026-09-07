# ShoppingServletServiceMVC - Tomcat 11 + Jakarta + CRUD Category

## 1. Database
Project TIẾP TỤC dùng database cũ `ShoppingDB` và bảng `[User]` hiện tại.
Chỉ cần chạy file:

`database/ShoppingDB_Category.sql`

để tạo thêm bảng `Category`.

## 2. SQL Server
- TCP/IP: Enabled
- Port: 1433
- SQL Server service: Running

## 3. DBConnection
Mặc định:
- database: ShoppingDB
- user: sa
- password: 123

Nếu mật khẩu `sa` khác, sửa file:
`src/main/java/vn/iotstar/connection/DBConnection.java`

hoặc truyền VM option:
`-Dshopping.db.password=MAT_KHAU_CUA_BAN`

## 4. Tomcat / Java
- Tomcat 11
- Jakarta Servlet 6.1
- Project compile target Java 17, có thể chạy bằng JDK mới hơn.

## 5. Chạy
1. Maven Reload
2. Rebuild Project
3. Deploy `ShoppingServletServiceMVC:war exploded`
4. Start Tomcat
5. Login admin: admin / 123456 (nếu bạn vẫn giữ dữ liệu cũ)
6. Mở `Trang admin -> Quản lý danh mục (CRUD Category)`

URL trực tiếp:
`http://localhost:8080/ShoppingServletServiceMVC/admin/category/list`

## 6. Upload ảnh
Không dùng `commons-fileupload` cũ. Project dùng Jakarta `@MultipartConfig` + `Part`.
Ảnh mặc định lưu tại:
`<user.home>/shopping-upload/category`

Có thể đổi bằng VM option:
`-Dshopping.upload.dir=C:\\upload`
