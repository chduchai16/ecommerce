-- Tạo database
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'ecommerce')
BEGIN
    CREATE DATABASE ecommerce;
END
GO

USE ecommerce;
GO

-- Tạo login cho user riêng
IF NOT EXISTS (SELECT * FROM sys.sql_logins WHERE name = 'shopuser')
BEGIN
    CREATE LOGIN shopuser WITH PASSWORD = 'ShopApp@123';
END
GO

-- Tạo user trong DB tương ứng login
CREATE USER shopuser FOR LOGIN shopuser;
EXEC sp_addrolemember 'db_owner', 'shopuser';
GO
