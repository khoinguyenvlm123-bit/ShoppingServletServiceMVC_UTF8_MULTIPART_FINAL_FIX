USE ServletCRUDMVC;
GO

/*
  Profile dùng các cột fullname, phone, avatar đang có trong bảng [User].
  Ảnh được upload bằng multipart và DB chỉ lưu đường dẫn tương đối trong avatar.
*/
IF COL_LENGTH('[User]', 'fullname') IS NULL
    ALTER TABLE [User] ADD fullname NVARCHAR(150) NULL;
GO
IF COL_LENGTH('[User]', 'phone') IS NULL
    ALTER TABLE [User] ADD phone VARCHAR(20) NULL;
GO
IF COL_LENGTH('[User]', 'avatar') IS NULL
    ALTER TABLE [User] ADD avatar NVARCHAR(255) NULL;
GO

/* Đánh giá sản phẩm: mỗi User chỉ có 1 đánh giá cho 1 Product, có thể cập nhật lại. */
IF OBJECT_ID('dbo.ProductRating', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.ProductRating(
        rating_id INT IDENTITY(1,1) PRIMARY KEY,
        user_id INT NOT NULL,
        product_id INT NOT NULL,
        stars INT NOT NULL,
        comment NVARCHAR(1000) NULL,
        created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
        updated_at DATETIME2 NULL,
        CONSTRAINT CK_ProductRating_Stars CHECK(stars BETWEEN 1 AND 5),
        CONSTRAINT UQ_ProductRating_User_Product UNIQUE(user_id, product_id),
        CONSTRAINT FK_ProductRating_User FOREIGN KEY(user_id) REFERENCES dbo.[User](id),
        CONSTRAINT FK_ProductRating_Product FOREIGN KEY(product_id) REFERENCES dbo.Product(product_id)
    );
END
GO

/* Đơn mua đơn giản cho chức năng Mua ngay. */
IF OBJECT_ID('dbo.PurchaseOrder', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.PurchaseOrder(
        order_id INT IDENTITY(1,1) PRIMARY KEY,
        user_id INT NOT NULL,
        product_id INT NOT NULL,
        quantity INT NOT NULL,
        unit_price DECIMAL(18,2) NOT NULL,
        total_price DECIMAL(18,2) NOT NULL,
        status VARCHAR(30) NOT NULL DEFAULT 'DA_DAT',
        created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
        CONSTRAINT CK_PurchaseOrder_Quantity CHECK(quantity > 0),
        CONSTRAINT FK_PurchaseOrder_User FOREIGN KEY(user_id) REFERENCES dbo.[User](id),
        CONSTRAINT FK_PurchaseOrder_Product FOREIGN KEY(product_id) REFERENCES dbo.Product(product_id)
    );
END
GO
