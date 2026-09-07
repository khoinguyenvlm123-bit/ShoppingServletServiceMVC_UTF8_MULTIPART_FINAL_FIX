/*
  FIX Unicode triệt để cho SQL Server.
  Chạy 1 lần trên đúng database ServletCRUDMVC.
  Lưu ý: dữ liệu đã bị biến thành dấu ? trước đó không thể tự khôi phục; cần nhập lại sau khi chạy script.
*/
USE ServletCRUDMVC;
GO

-- Kiểm tra kiểu cột hiện tại
SELECT TABLE_NAME, COLUMN_NAME, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH
FROM INFORMATION_SCHEMA.COLUMNS
WHERE (TABLE_NAME = 'User' AND COLUMN_NAME IN ('fullname','avatar','phone'))
   OR (TABLE_NAME = 'Category' AND COLUMN_NAME IN ('cate_name','icons'))
   OR (TABLE_NAME = 'Product' AND COLUMN_NAME IN ('product_name','description'))
   OR (TABLE_NAME = 'ProductRating' AND COLUMN_NAME = 'comment');
GO

IF OBJECT_ID('dbo.[User]', 'U') IS NOT NULL
BEGIN
    IF COL_LENGTH('dbo.[User]', 'fullname') IS NOT NULL
        ALTER TABLE dbo.[User] ALTER COLUMN fullname NVARCHAR(150) NULL;
    IF COL_LENGTH('dbo.[User]', 'avatar') IS NOT NULL
        ALTER TABLE dbo.[User] ALTER COLUMN avatar NVARCHAR(500) NULL;
    IF COL_LENGTH('dbo.[User]', 'phone') IS NOT NULL
        ALTER TABLE dbo.[User] ALTER COLUMN phone NVARCHAR(30) NULL;
END
GO

IF OBJECT_ID('dbo.Category', 'U') IS NOT NULL
BEGIN
    IF COL_LENGTH('dbo.Category', 'cate_name') IS NOT NULL
        ALTER TABLE dbo.Category ALTER COLUMN cate_name NVARCHAR(255) NOT NULL;
    IF COL_LENGTH('dbo.Category', 'icons') IS NOT NULL
        ALTER TABLE dbo.Category ALTER COLUMN icons NVARCHAR(500) NULL;
END
GO

IF OBJECT_ID('dbo.Product', 'U') IS NOT NULL
BEGIN
    IF COL_LENGTH('dbo.Product', 'product_name') IS NOT NULL
        ALTER TABLE dbo.Product ALTER COLUMN product_name NVARCHAR(255) NOT NULL;
    IF COL_LENGTH('dbo.Product', 'description') IS NOT NULL
        ALTER TABLE dbo.Product ALTER COLUMN description NVARCHAR(MAX) NULL;
END
GO

IF OBJECT_ID('dbo.ProductRating', 'U') IS NOT NULL AND COL_LENGTH('dbo.ProductRating', 'comment') IS NOT NULL
    ALTER TABLE dbo.ProductRating ALTER COLUMN comment NVARCHAR(1000) NULL;
GO

PRINT N'Đã chuyển các cột nhập tiếng Việt sang NVARCHAR.';
GO
