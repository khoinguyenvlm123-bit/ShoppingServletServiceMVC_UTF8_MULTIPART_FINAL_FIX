USE ServletCRUDMVC;
GO

-- 1. Đảm bảo bảng Category tồn tại
IF OBJECT_ID('dbo.Category', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.Category(
        cate_id INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
        cate_name NVARCHAR(255) NOT NULL,
        icons NVARCHAR(255) NULL
    );
END
GO

-- 2. Đảm bảo bảng User tồn tại cho đăng nhập / quản lý tài khoản
IF OBJECT_ID('dbo.[User]', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.[User](
        id INT IDENTITY(1,1) PRIMARY KEY,
        email NVARCHAR(100) NULL,
        username NVARCHAR(50) NOT NULL UNIQUE,
        fullname NVARCHAR(100) NULL,
        password NVARCHAR(100) NOT NULL,
        avatar NVARCHAR(255) NULL,
        roleid INT NOT NULL DEFAULT 5,
        phone NVARCHAR(20) NULL,
        createddate DATE NULL
    );
END
GO

-- Tạo tài khoản admin mẫu nếu chưa có
IF NOT EXISTS (SELECT 1 FROM dbo.[User] WHERE username = 'admin')
BEGIN
    INSERT INTO dbo.[User](email, username, fullname, password, avatar, roleid, phone, createddate)
    VALUES ('admin@gmail.com', 'admin', N'Quản trị viên', '123456', NULL, 1, '0900000000', GETDATE());
END
GO

-- 3. Đảm bảo bảng Product tồn tại
IF OBJECT_ID('dbo.Product', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.Product(
        product_id INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
        product_name NVARCHAR(255) NOT NULL,
        price DECIMAL(18,2) NOT NULL DEFAULT 0,
        quantity INT NOT NULL DEFAULT 0,
        description NVARCHAR(500) NULL,
        image NVARCHAR(255) NULL,
        cate_id INT NOT NULL,
        CONSTRAINT FK_Product_Category
            FOREIGN KEY (cate_id) REFERENCES dbo.Category(cate_id)
    );
END
GO

-- Dữ liệu mẫu để trang sản phẩm có dữ liệu nếu Category đã có
IF NOT EXISTS (SELECT 1 FROM dbo.Product)
   AND EXISTS (SELECT 1 FROM dbo.Category)
BEGIN
    DECLARE @cateId INT = (SELECT TOP 1 cate_id FROM dbo.Category ORDER BY cate_id);
    INSERT INTO dbo.Product(product_name, price, quantity, description, image, cate_id)
    VALUES
        (N'Sản phẩm mẫu 1', 100000, 10, N'Dữ liệu mẫu', NULL, @cateId),
        (N'Sản phẩm mẫu 2', 200000, 5, N'Dữ liệu mẫu', NULL, @cateId);
END
GO
