PROJECT ĐÃ CHUYỂN CRUD SANG JPA API

Các Entity tách riêng:
- vn.iotstar.entity.Category
- vn.iotstar.entity.Product
- vn.iotstar.entity.User

JPA cấu hình tại:
- vn.iotstar.connection.JPAConfig
- src/main/resources/META-INF/persistence.xml

DAO Category/Product/User dùng EntityManager và JPA API:
- persist() để thêm
- merge() để sửa
- find() để tìm theo id
- remove() để xóa
- TypedQuery + JPQL để danh sách/tìm kiếm
- EntityTransaction để quản lý transaction

Không còn DBConnection/PreparedStatement/ResultSet trong tầng DAO.
