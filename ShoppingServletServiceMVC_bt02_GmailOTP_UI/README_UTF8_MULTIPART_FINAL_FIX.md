# UTF-8 multipart final fix

Bản này sửa thêm lỗi tiếng Việt trong form có `multipart/form-data`.

## Nguyên nhân
`request.setCharacterEncoding("UTF-8")` không phải lúc nào cũng đủ cho field text nằm trong multipart trên mọi container/cấu hình. Khi đó chuỗi như `Quản Trị Viên` có thể bị lưu thành `Qu?n Tr? Viên`.

## Đã sửa
- `MultipartUtf8.java`: đọc bytes của Part text và giải mã trực tiếp bằng UTF-8.
- Profile: fullname/phone dùng `MultipartUtf8`.
- Category add/edit: tên danh mục dùng `MultipartUtf8`.
- Các form multipart thêm `accept-charset="UTF-8"`.
- JDBC SQL Server thêm `sendStringParametersAsUnicode=true`.
- SQL migration chuyển cột text sang `NVARCHAR`.

## Quan trọng
Nếu database đang có `Qu?n Tr? Viên`, các dấu đã bị mất thật và không thể khôi phục tự động. Sau khi chạy `database/ShoppingDB_UTF8_Unicode_FIX.sql`, hãy nhập lại `Quản Trị Viên` rồi lưu lại.
