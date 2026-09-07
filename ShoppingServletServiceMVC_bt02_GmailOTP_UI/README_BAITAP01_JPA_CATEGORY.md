# Bài tập 01 (24/06/2026) - CRUD Category bằng JPA API

Project đã được chuyển riêng phần **Category** từ JDBC sang **JPA API**.

## Phạm vi đúng theo bài hiện tại
- Entity bắt buộc: `Category`.
- Bảng dùng lại: `Category(cate_id, cate_name, icons)`.
- Giữ nguyên đăng nhập, Admin, giao diện CRUD Category và upload ảnh của project.
- Không thêm `Video`.
- Không thêm `status` vì database/project hiện tại không có cột này.
- Không thêm chức năng quản lý đối tượng khác.

## Cấu trúc JPA đã thêm
- `vn.iotstar.model.Category`: JPA Entity.
- `vn.iotstar.connection.JPAConfig`: tạo `EntityManager`.
- `META-INF/persistence.xml`: cấu hình persistence unit.
- `CategoryDao` + `CategoryDaoImpl`: CRUD/query bằng EntityManager, JPQL, NamedQuery.
- `CategoryService` + `CategoryServiceImpl`: tầng Service.
- Các controller Category hiện tại tiếp tục dùng Service, URL và JSP cũ không đổi.

## Các phương thức Category
- `insert(Category category)`
- `update(Category category)`
- `delete(int cateId)`
- `findById(int cateId)`
- `findByCategoryname(String name)`
- `findAll()`
- `searchByName(String categoryName)`
- `findAll(int page, int pageSize)`
- `count()`

## Database mặc định
JPA dùng cùng cấu hình với project:
- URL: `jdbc:sqlserver://localhost:1433;databaseName=ServletCRUDMVC;encrypt=false;trustServerCertificate=true`
- user: `sa`
- password: `123`

Có thể đổi bằng VM options:
- `-Dshopping.db.url=...`
- `-Dshopping.db.user=...`
- `-Dshopping.db.password=...`

## Chạy
1. Chạy SQL tạo database/bảng trong `database/ShoppingDB_Category.sql` nếu chưa có.
2. Maven Reload.
3. Build project.
4. Deploy lên Tomcat 11.
5. Đăng nhập Admin và vào `/admin/category/list`.
