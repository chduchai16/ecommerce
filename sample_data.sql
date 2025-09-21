-- Script thêm dữ liệu mẫu cho ecommerce database
-- Chạy script này sau khi đã có tables và roles, users

-- 1. Categories (10 danh mục)
INSERT INTO categories (name, created_at, updated_at) VALUES
(N'Điện thoại', GETDATE(), GETDATE()),
(N'Laptop', GETDATE(), GETDATE()),
(N'Máy tính bảng', GETDATE(), GETDATE()),
(N'Phụ kiện điện tử', GETDATE(), GETDATE()),
(N'Đồng hồ thông minh', GETDATE(), GETDATE()),
(N'Tai nghe', GETDATE(), GETDATE()),
(N'Sạc dự phòng', GETDATE(), GETDATE()),
(N'Ốp lưng & Bao da', GETDATE(), GETDATE()),
(N'Thiết bị mạng', GETDATE(), GETDATE()),
(N'Gaming', GETDATE(), GETDATE());

-- 2. Products (10 sản phẩm)
INSERT INTO products (name, description, price, original_price, discount, review_count, in_stock, tags, stock_quantity, category_id, status, created_at, updated_at) VALUES
(N'iPhone 15 Pro Max 256GB', N'Smartphone cao cấp từ Apple với chip A17 Pro mạnh mẽ', 29990000, 32990000, 9, 156, 1, N'apple,iphone,smartphone,premium', 25, 1, 'ACTIVE', GETDATE(), GETDATE()),
(N'Samsung Galaxy S24 Ultra', N'Flagship Android với S Pen tích hợp và camera 200MP', 26990000, 29990000, 10, 203, 1, N'samsung,galaxy,android,camera', 18, 1, 'ACTIVE', GETDATE(), GETDATE()),
(N'MacBook Air M3 13 inch', N'Laptop siêu nhẹ với chip M3 hiệu năng cao', 28990000, 31990000, 9, 89, 1, N'apple,macbook,laptop,m3', 12, 2, 'ACTIVE', GETDATE(), GETDATE()),
(N'Dell XPS 13 Plus', N'Laptop Windows cao cấp thiết kế hiện đại', 25990000, 27990000, 7, 67, 1, N'dell,windows,laptop,business', 8, 2, 'ACTIVE', GETDATE(), GETDATE()),
(N'iPad Pro 12.9 inch M4', N'Máy tính bảng chuyên nghiệp cho sáng tạo', 23990000, 25990000, 8, 142, 1, N'apple,ipad,tablet,creative', 15, 3, 'ACTIVE', GETDATE(), GETDATE()),
(N'AirPods Pro 2nd Gen', N'Tai nghe không dây với chống ồn chủ động', 5990000, 6990000, 14, 324, 1, N'apple,airpods,wireless,noise-cancelling', 50, 6, 'ACTIVE', GETDATE(), GETDATE()),
(N'Sony WH-1000XM5', N'Tai nghe over-ear chống ồn hàng đầu', 7990000, 8990000, 11, 189, 1, N'sony,headphones,noise-cancelling,premium', 30, 6, 'ACTIVE', GETDATE(), GETDATE()),
(N'Apple Watch Series 9 GPS', N'Đồng hồ thông minh với tính năng sức khỏe', 8990000, 9990000, 10, 267, 1, N'apple,watch,smartwatch,health', 35, 5, 'ACTIVE', GETDATE(), GETDATE()),
(N'Anker PowerBank 20000mAh', N'Sạc dự phòng dung lượng cao sạc nhanh', 1290000, 1590000, 19, 445, 1, N'anker,powerbank,charging,portable', 100, 7, 'ACTIVE', GETDATE(), GETDATE()),
(N'Logitech MX Master 3S', N'Chuột không dây cho productivity và gaming', 2390000, 2790000, 14, 156, 1, N'logitech,mouse,wireless,productivity', 40, 4, 'ACTIVE', GETDATE(), GETDATE());

-- 3. Product Specifications (10 thông số kỹ thuật)
INSERT INTO product_specifications (product_id, spec_key, spec_value, created_at, updated_at) VALUES
(1, N'Màn hình', N'6.7 inch Super Retina XDR OLED', GETDATE(), GETDATE()),
(1, N'Chip', N'Apple A17 Pro', GETDATE(), GETDATE()),
(2, N'Màn hình', N'6.8 inch Dynamic AMOLED 2X', GETDATE(), GETDATE()),
(2, N'Camera', N'200MP chính + 50MP telephoto + 12MP ultrawide', GETDATE(), GETDATE()),
(3, N'Màn hình', N'13.6 inch Liquid Retina', GETDATE(), GETDATE()),
(3, N'Chip', N'Apple M3 8-core CPU', GETDATE(), GETDATE()),
(4, N'Màn hình', N'13.4 inch InfinityEdge', GETDATE(), GETDATE()),
(4, N'CPU', N'Intel Core i7-1360P', GETDATE(), GETDATE()),
(5, N'Màn hình', N'12.9 inch Liquid Retina XDR', GETDATE(), GETDATE()),
(6, N'Kết nối', N'Bluetooth 5.3, Lightning', GETDATE(), GETDATE());

-- 4. Coupons (10 mã giảm giá)
INSERT INTO coupons (code, discount_percent, discount_money, expiration_date, status, created_at, updated_at) VALUES
('WELCOME10', 10, NULL, '2025-12-31', 1, GETDATE(), GETDATE()),
('TECH20', 20, NULL, '2025-11-30', 1, GETDATE(), GETDATE()),
('SAVE500K', NULL, 500000, '2025-10-31', 1, GETDATE(), GETDATE()),
('IPHONE15', 15, NULL, '2025-09-30', 1, GETDATE(), GETDATE()),
('LAPTOP25', 25, NULL, '2025-12-15', 1, GETDATE(), GETDATE()),
('FREESHIP', NULL, 50000, '2025-12-31', 1, GETDATE(), GETDATE()),
('STUDENT', 12, NULL, '2025-12-31', 1, GETDATE(), GETDATE()),
('FLASH30', 30, NULL, '2025-09-25', 1, GETDATE(), GETDATE()),
('VIP100K', NULL, 100000, '2025-11-30', 1, GETDATE(), GETDATE()),
('APPLE20', 20, NULL, '2025-10-31', 1, GETDATE(), GETDATE());

-- 5. Carts (10 giỏ hàng - giả sử có user_id từ 1-10)
-- Lưu ý: Cần có users trước khi chạy phần này
INSERT INTO carts (user_id, created_at, updated_at) VALUES
(1, GETDATE(), GETDATE()),
(2, GETDATE(), GETDATE()),
(3, GETDATE(), GETDATE()),
(4, GETDATE(), GETDATE()),
(5, GETDATE(), GETDATE()),
(6, GETDATE(), GETDATE()),
(7, GETDATE(), GETDATE()),
(8, GETDATE(), GETDATE()),
(9, GETDATE(), GETDATE()),
(10, GETDATE(), GETDATE());

-- 6. Cart Items (10 sản phẩm trong giỏ hàng)
INSERT INTO cart_items (cart_id, product_id, quantity, created_at, updated_at) VALUES
(1, 1, 1, GETDATE(), GETDATE()),
(1, 6, 2, GETDATE(), GETDATE()),
(2, 2, 1, GETDATE(), GETDATE()),
(3, 3, 1, GETDATE(), GETDATE()),
(3, 7, 1, GETDATE(), GETDATE()),
(4, 4, 1, GETDATE(), GETDATE()),
(5, 5, 1, GETDATE(), GETDATE()),
(6, 8, 1, GETDATE(), GETDATE()),
(7, 9, 3, GETDATE(), GETDATE()),
(8, 10, 2, GETDATE(), GETDATE());

-- 7. Orders (10 đơn hàng)
INSERT INTO orders (user_id, full_name, email, phone_number, address, note, order_date, status, total_money, shipping_method, shipping_address, shipping_date, tracking_number, payment_method, active, created_at, updated_at) VALUES
(1, N'Nguyễn Văn A', 'nguyenvana@email.com', '0901234567', N'123 Nguyễn Huệ, Q1, TP.HCM', N'Giao hàng giờ hành chính', '2024-09-20 10:30:00', N'DELIVERED', 30980000, N'EXPRESS', N'123 Nguyễn Huệ, Q1, TP.HCM', '2024-09-21 14:00:00', 'TN001234567', N'CREDIT_CARD', 1, GETDATE(), GETDATE()),
(2, N'Trần Thị B', 'tranthib@email.com', '0901234568', N'456 Lê Lợi, Q3, TP.HCM', N'Để ở bảo vệ', '2024-09-19 15:45:00', N'SHIPPED', 26990000, N'STANDARD', N'456 Lê Lợi, Q3, TP.HCM', '2024-09-20 09:00:00', 'TN001234568', N'BANK_TRANSFER', 1, GETDATE(), GETDATE()),
(3, N'Lê Văn C', 'levanc@email.com', '0901234569', N'789 Võ Thị Sáu, Q10, TP.HCM', NULL, '2024-09-18 09:15:00', N'PROCESSING', 28990000, N'EXPRESS', N'789 Võ Thị Sáu, Q10, TP.HCM', NULL, 'TN001234569', N'COD', 1, GETDATE(), GETDATE()),
(4, N'Phạm Thị D', 'phamthid@email.com', '0901234570', N'321 Pasteur, Q1, TP.HCM', N'Gọi trước khi giao', '2024-09-17 14:20:00', N'DELIVERED', 25990000, N'STANDARD', N'321 Pasteur, Q1, TP.HCM', '2024-09-18 16:30:00', 'TN001234570', N'CREDIT_CARD', 1, GETDATE(), GETDATE()),
(5, N'Hoàng Văn E', 'hoangvane@email.com', '0901234571', N'654 Cách Mạng Tháng 8, Q3', N'Nhà màu xanh', '2024-09-16 11:00:00', N'CANCELLED', 23990000, N'EXPRESS', N'654 Cách Mạng Tháng 8, Q3', NULL, 'TN001234571', N'BANK_TRANSFER', 0, GETDATE(), GETDATE()),
(6, N'Vũ Thị F', 'vuthif@email.com', '0901234572', N'987 Điện Biên Phủ, Q3', NULL, '2024-09-15 16:30:00', N'DELIVERED', 5990000, N'STANDARD', N'987 Điện Biên Phủ, Q3', '2024-09-16 10:00:00', 'TN001234572', N'COD', 1, GETDATE(), GETDATE()),
(7, N'Đặng Văn G', 'dangvang@email.com', '0901234573', N'147 Nguyễn Đình Chiểu, Q1', N'Tầng 3', '2024-09-14 13:45:00', N'SHIPPED', 7990000, N'EXPRESS', N'147 Nguyễn Đình Chiểu, Q1', '2024-09-15 08:00:00', 'TN001234573', N'CREDIT_CARD', 1, GETDATE(), GETDATE()),
(8, N'Bùi Thị H', 'buithih@email.com', '0901234574', N'258 Hai Bà Trưng, Q1', NULL, '2024-09-13 10:15:00', N'PROCESSING', 8990000, N'STANDARD', N'258 Hai Bà Trưng, Q1', NULL, 'TN001234574', N'BANK_TRANSFER', 1, GETDATE(), GETDATE()),
(9, N'Ngô Văn I', 'ngovani@email.com', '0901234575', N'369 Nam Kỳ Khởi Nghĩa, Q3', N'Giao buổi chiều', '2024-09-12 08:30:00', N'DELIVERED', 3870000, N'STANDARD', N'369 Nam Kỳ Khởi Nghĩa, Q3', '2024-09-13 15:00:00', 'TN001234575', N'COD', 1, GETDATE(), GETDATE()),
(10, N'Lý Thị K', 'lythik@email.com', '0901234576', N'741 Cộng Hòa, Tân Bình', NULL, '2024-09-11 12:00:00', N'DELIVERED', 2390000, N'EXPRESS', N'741 Cộng Hòa, Tân Bình', '2024-09-12 11:00:00', 'TN001234576', N'CREDIT_CARD', 1, GETDATE(), GETDATE());

-- 8. Order Details (Chi tiết đơn hàng)
INSERT INTO order_details (order_id, product_id, price, number_of_products, total_money, created_at, updated_at) VALUES
(1, 1, 29990000, 1, 29990000, GETDATE(), GETDATE()),
(1, 6, 5990000, 1, 5990000, GETDATE(), GETDATE()),
(2, 2, 26990000, 1, 26990000, GETDATE(), GETDATE()),
(3, 3, 28990000, 1, 28990000, GETDATE(), GETDATE()),
(4, 4, 25990000, 1, 25990000, GETDATE(), GETDATE()),
(5, 5, 23990000, 1, 23990000, GETDATE(), GETDATE()),
(6, 6, 5990000, 1, 5990000, GETDATE(), GETDATE()),
(7, 7, 7990000, 1, 7990000, GETDATE(), GETDATE()),
(8, 8, 8990000, 1, 8990000, GETDATE(), GETDATE()),
(9, 9, 1290000, 3, 3870000, GETDATE(), GETDATE()),
(10, 10, 2390000, 1, 2390000, GETDATE(), GETDATE());

-- 9. Ratings (10 đánh giá sản phẩm)
INSERT INTO ratings (user_id, product_id, rating, comment, created_at, updated_at) VALUES
(1, 1, 5, N'iPhone 15 Pro Max rất tuyệt vời, camera chụp ảnh đẹp!', GETDATE(), GETDATE()),
(2, 2, 4, N'Galaxy S24 Ultra pin trâu, S Pen rất tiện', GETDATE(), GETDATE()),
(3, 3, 5, N'MacBook Air M3 nhanh và nhẹ, phù hợp mang đi làm', GETDATE(), GETDATE()),
(4, 4, 4, N'Dell XPS 13 Plus thiết kế đẹp nhưng hơi nóng', GETDATE(), GETDATE()),
(5, 5, 5, N'iPad Pro M4 vẽ digital art rất mượt', GETDATE(), GETDATE()),
(6, 6, 5, N'AirPods Pro 2 chống ồn tốt, âm thanh trong', GETDATE(), GETDATE()),
(7, 7, 4, N'Sony WH-1000XM5 âm thanh hay nhưng hơi to', GETDATE(), GETDATE()),
(8, 8, 4, N'Apple Watch theo dõi sức khỏe chính xác', GETDATE(), GETDATE()),
(9, 9, 5, N'Anker PowerBank sạc nhanh, dung lượng lớn', GETDATE(), GETDATE()),
(10, 10, 4, N'Logitech MX Master 3S ergonomic tốt', GETDATE(), GETDATE());

-- Lưu ý:
-- 1. Trước khi chạy script này, đảm bảo đã có:
--    - Bảng roles với dữ liệu
--    - Bảng users với ít nhất 10 users (id từ 1-10)
-- 2. Không thêm dữ liệu cho bảng product_images vì bạn sẽ tự upload ảnh sau
-- 3. Có thể cần điều chỉnh các ID foreign key cho phù hợp với dữ liệu thực tế
-- 4. Các giá trị datetime sử dụng GETDATE() cho SQL Server

PRINT 'Đã thêm dữ liệu mẫu thành công!';
