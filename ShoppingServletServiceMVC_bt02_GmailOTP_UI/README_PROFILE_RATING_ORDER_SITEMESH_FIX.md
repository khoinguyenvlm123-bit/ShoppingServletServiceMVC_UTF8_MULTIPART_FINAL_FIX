# Profile + Rating + Buy + SiteMesh 3.2.1

## 1. Lỗi 404 SiteMesh đã sửa

Lỗi cũ:
`JSP file [/WEB-INF/decorators/decorators/user.jsp] not found`

Nguyên nhân: SiteMesh 3 có decorator-prefix mặc định `/WEB-INF/decorators/`, nhưng `sitemesh3.xml` cũ lại cấu hình `/decorators/user.jsp`, nên đường dẫn bị ghép thành `.../decorators/decorators/user.jsp`.

Cấu trúc mới:

```
src/main/webapp/
├─ commons/web/
│  ├─ header.jsp
│  ├─ footer.jsp
│  └─ left.jsp
├─ views/web/
│  ├─ profile.jsp
│  ├─ product-list.jsp
│  ├─ product-detail.jsp
│  └─ my-orders.jsp
└─ WEB-INF/
   ├─ decorators/
   │  └─ user.jsp
   ├─ sitemesh3.xml
   └─ web.xml
```

`sitemesh3.xml` dùng `decorator="user.jsp"` vì SiteMesh tự lấy prefix `/WEB-INF/decorators/`.

## 2. Profile User

URL: `/profile`

- GET: lấy User mới nhất từ database bằng JPA.
- POST: cập nhật `fullname`, `phone`, `avatar`.
- Form dùng `enctype="multipart/form-data"`.
- Servlet dùng `@MultipartConfig`.
- File input tên `images`.
- Ảnh lưu ở `${user.home}/shopping-upload/avatars`.
- DB chỉ lưu đường dẫn `avatars/<uuid>.<ext>` trong cột `avatar`.
- Chấp nhận JPG/JPEG/PNG/GIF/WEBP, tối đa 5 MB.

## 3. Đánh giá sản phẩm

URL POST: `/rating/save`

Các file tách riêng:
- `entity/ProductRating.java`
- `dao/ProductRatingDao.java`
- `dao/impl/ProductRatingDaoImpl.java`
- `service/ProductRatingService.java`
- `service/impl/ProductRatingServiceImpl.java`
- `controller/RatingController.java`

Mỗi User có một đánh giá cho một Product; gửi lại sẽ cập nhật số sao và nhận xét. Trang chi tiết hiển thị điểm trung bình, số lượt đánh giá và danh sách nhận xét.

## 4. Mua hàng

URL POST: `/order/buy`
URL GET: `/order/my`

Các file tách riêng:
- `entity/PurchaseOrder.java`
- `dao/PurchaseOrderDao.java`
- `dao/impl/PurchaseOrderDaoImpl.java`
- `service/PurchaseOrderService.java`
- `service/impl/PurchaseOrderServiceImpl.java`
- `controller/BuyController.java`
- `controller/MyOrdersController.java`

Khi mua, JPA transaction khóa Product, kiểm tra tồn kho, trừ quantity và tạo PurchaseOrder trong cùng transaction.

## 5. Database

Project đang bật `hibernate.hbm2ddl.auto=update`, vì vậy Hibernate có thể tự tạo 2 bảng mới. Nếu giảng viên yêu cầu script SQL, chạy:

`database/ShoppingDB_Profile_Rating_Order.sql`

## 6. SiteMesh theo tài liệu

- `pom.xml`: SiteMesh `3.2.1`.
- `web.xml`: `ConfigurableSiteMeshFilter`, URL pattern `/*`.
- `WEB-INF/sitemesh3.xml`: mapping URL -> decorator.
- `WEB-INF/decorators/user.jsp`: layout chung.
- `commons/web/header.jsp` và `footer.jsp`: phần dùng chung.
- `view/*.jsp`: content riêng, SiteMesh đưa `body` vào layout.

## 7. Tomcat

Ảnh lỗi cho thấy bạn đang chạy Tomcat 10.1.x. Project đã đổi `jakarta.servlet-api` về 6.0.0 cho đúng Servlet API của Tomcat 10.1 và SiteMesh 3.2.x.

Sau khi thay project:
1. Maven -> Reload All Maven Projects.
2. Build -> Rebuild Project.
3. Xóa artifact/deployment cũ trong Tomcat nếu IntelliJ vẫn cache.
4. Deploy lại WAR exploded.
5. Truy cập `/profile` và `/product`.

## Fix avatar ở trang chủ
Trang `view/home.jsp` đã được sửa để lấy `currentUser.getAvatar()` và hiển thị qua `/image?fname=...`.
Nếu người dùng chưa có avatar, giao diện mới hiển thị chữ `AD` hoặc `US` mặc định.
