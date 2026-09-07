SỬA LỖI HTTP 500 - Invalid object name 'Product'

Ảnh lỗi cho thấy request đang chạy ProductListController, không phải AccountListController.
SQL Server báo bảng Product chưa tồn tại.

Đã sửa:
1. persistence.xml: hibernate.hbm2ddl.auto = update để JPA/Hibernate tự đồng bộ bảng Product.
2. Thêm database/ShoppingDB_Update_Category_Product_Account.sql.
3. File SQL này KHÔNG CREATE DATABASE lại; chỉ bổ sung Category/User/Product nếu thiếu.

Cách chạy khuyến nghị:
- Mở SQL Server Management Studio.
- Chọn database ServletCRUDMVC.
- Chạy file database/ShoppingDB_Update_Category_Product_Account.sql.
- Stop Tomcat, Maven Reload, Rebuild Project, Start Tomcat.

URL:
/admin/category/list
/admin/product/list
/admin/account/list
