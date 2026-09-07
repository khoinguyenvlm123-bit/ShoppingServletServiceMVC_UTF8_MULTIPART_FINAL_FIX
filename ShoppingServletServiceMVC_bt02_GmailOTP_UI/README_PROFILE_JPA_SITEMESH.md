# Chức năng Profile User - JPA + Multipart + SiteMesh

## Chức năng đã thêm
- User đăng nhập có thể mở `/profile`.
- Xem username, email (không cho sửa).
- Cập nhật `fullname`.
- Cập nhật `phone`, có kiểm tra trùng số điện thoại.
- Upload ảnh đại diện bằng `multipart/form-data`.
- Chỉ nhận JPG/JPEG/PNG/GIF/WEBP, tối đa 5 MB.
- Ảnh lưu ngoài database tại `${user.home}/shopping-upload/avatars`.
- Database chỉ lưu đường dẫn dạng `avatars/<uuid>.<ext>` trong cột `avatar` hiện có.
- Khi thay avatar mới, file avatar cũ do hệ thống upload sẽ được xóa.
- Sau update, object User trong session được load lại để giao diện hiển thị dữ liệu mới ngay.
- Giao diện `/profile` được quản lý bởi SiteMesh decorator.

## Các file chính
### Controller
`src/main/java/vn/iotstar/controller/ProfileController.java`

### JPA
Project đã có sẵn và tiếp tục sử dụng:
- `entity/User.java`
- `dao/UserDao.java`
- `dao/impl/UserDaoImpl.java`
- `service/UserService.java`
- `service/impl/UserServiceImpl.java`

### View
`src/main/webapp/view/profile.jsp`

### SiteMesh layout
`src/main/webapp/decorators/user.jsp`

### SiteMesh config
`src/main/webapp/WEB-INF/sitemesh3.xml`

### SiteMesh filter
`src/main/webapp/WEB-INF/web.xml`

### CSS riêng
`src/main/webapp/assets/css/profile.css`

### Upload
Dùng lại `src/main/java/vn/iotstar/util/UploadConstant.java`.
Ảnh được đọc qua servlet có sẵn `/image?fname=...`.

## Dependency SiteMesh
Đã thêm vào `pom.xml`:

```xml
<dependency>
    <groupId>org.sitemesh</groupId>
    <artifactId>sitemesh-webfilter</artifactId>
    <version>3.2.3</version>
</dependency>
```

## Cách chạy
1. Mở project bằng IntelliJ IDEA.
2. Nhấn Maven > Reload All Maven Projects.
3. Chạy lại Tomcat 11.
4. Đăng nhập.
5. Chọn `Thông tin cá nhân` hoặc truy cập `/profile`.
6. Sửa fullname/phone, chọn ảnh rồi bấm `Lưu thay đổi`.

## Nếu database của bạn thiếu cột
Entity hiện tại của project đã map các cột:
- `fullname`
- `phone`
- `avatar`

Nếu bảng `[User]` của SQL Server chưa có một trong các cột đó, chạy phù hợp với database của bạn, ví dụ:

```sql
IF COL_LENGTH('[User]', 'fullname') IS NULL
    ALTER TABLE [User] ADD fullname NVARCHAR(150) NULL;

IF COL_LENGTH('[User]', 'phone') IS NULL
    ALTER TABLE [User] ADD phone VARCHAR(20) NULL;

IF COL_LENGTH('[User]', 'avatar') IS NULL
    ALTER TABLE [User] ADD avatar NVARCHAR(500) NULL;
```

## Đổi thư mục upload (tùy chọn)
Mặc định: `${user.home}/shopping-upload`.
Có thể cấu hình VM option khi chạy Tomcat:

`-Dshopping.upload.dir=D:\shopping-upload`
