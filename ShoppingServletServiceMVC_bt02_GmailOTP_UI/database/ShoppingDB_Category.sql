CREATE DATABASE ServletCRUDMVC;
GO

USE ServletCRUDMVC;
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE Category(
    [cate_id] [int] IDENTITY(1,1) NOT NULL,
    [cate_name] [nvarchar](255) NOT NULL,
    [icons] [nvarchar](255) NULL,
    PRIMARY KEY CLUSTERED
        (
        [cate_id] ASC
        )WITH (
        PAD_INDEX = OFF,
        STATISTICS_NORECOMPUTE = OFF,
        IGNORE_DUP_KEY = OFF,
        ALLOW_ROW_LOCKS = ON,
        ALLOW_PAGE_LOCKS = ON
        ) ON [PRIMARY]
    ) ON [PRIMARY]
    GO
    USE ServletCRUDMVC;
GO

CREATE TABLE [User] (
                        id INT IDENTITY(1,1) PRIMARY KEY,
    email NVARCHAR(100),
    username NVARCHAR(50) UNIQUE,
    fullname NVARCHAR(100),
    password NVARCHAR(100),
    avatar NVARCHAR(255) NULL,
    roleid INT DEFAULT 5,
    phone NVARCHAR(20),
    createddate DATE
    );
GO

INSERT INTO [User]
(
    email,
    username,
    fullname,
    password,
    avatar,
    roleid,
    phone,
    createddate
)
VALUES
(
    'admin@gmail.com',
    'admin',
    N'Quan Tri Vien',
    '123456',
    NULL,
    1,
    '0900000000',
    GETDATE()
);
GO

-- Bảng Product dùng cho chức năng quản lý sản phẩm
IF OBJECT_ID('Product', 'U') IS NULL
BEGIN
    CREATE TABLE Product (
        product_id INT IDENTITY(1,1) PRIMARY KEY,
        product_name NVARCHAR(255) NOT NULL,
        price DECIMAL(18,2) NOT NULL DEFAULT 0,
        quantity INT NOT NULL DEFAULT 0,
        description NVARCHAR(500) NULL,
        image NVARCHAR(255) NULL,
        cate_id INT NOT NULL,
        CONSTRAINT FK_Product_Category FOREIGN KEY (cate_id) REFERENCES Category(cate_id)
    );
END
GO
