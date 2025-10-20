# E-Commerce Application

Ứng dụng E-Commerce được xây dựng bằng Spring Boot với SQL Server database.

## 🚀 Cách chạy ứng dụng

### Yêu cầu
- Docker Desktop đã cài đặt
- Ít nhất 4GB RAM available
- Port 8080 và 1433 chưa bị sử dụng

### Bước 1: Tải file docker-compose.yml

Tải file `docker-compose.yml` về máy của bạn.

### Bước 2: Chạy ứng dụng

Mở Terminal/PowerShell tại thư mục chứa file `docker-compose.yml` và chạy:

```bash
docker-compose up -d
```

Lệnh này sẽ:
1. Tải SQL Server image
2. Tải E-Commerce API image từ Docker Hub
3. Tự động tạo database `ecommerce`
4. Tạo user `chuduchai1901` với quyền truy cập
5. Khởi động ứng dụng

### Bước 3: Kiểm tra trạng thái

```bash
docker-compose ps
```

Bạn sẽ thấy 3 services:
- `ecommerce-sqlserver` - SQL Server database (running)
- `ecommerce-db-init` - Database initializer (exited/completed)
- `ecommerce-app` - Spring Boot application (running)

### Bước 4: Truy cập ứng dụng

- **API Base URL**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Documentation**: http://localhost:8080/v3/api-docs

## 📋 API Endpoints chính

### Authentication
- `POST /api/auth/login` - Đăng nhập
- `POST /api/auth/register` - Đăng ký

### Products
- `GET /api/products` - Lấy danh sách sản phẩm
- `GET /api/products/{id}` - Chi tiết sản phẩm
- `POST /api/products` - Tạo sản phẩm mới (Admin)

### Categories
- `GET /api/categories` - Lấy danh sách danh mục

### Cart
- `GET /api/cart` - Xem giỏ hàng
- `POST /api/cart/items` - Thêm vào giỏ hàng

### Orders
- `GET /api/orders` - Xem đơn hàng
- `POST /api/orders` - Tạo đơn hàng mới

## 🛠️ Các lệnh hữu ích

### Xem logs
```bash
# Xem tất cả logs
docker-compose logs -f

# Xem log của app
docker-compose logs -f shopapp

# Xem log của database
docker-compose logs -f sqlserver
```

### Dừng ứng dụng
```bash
docker-compose down
```

### Dừng và xóa data
```bash
docker-compose down -v
```

### Restart ứng dụng
```bash
docker-compose restart shopapp
```

### Cập nhật lên phiên bản mới
```bash
docker-compose pull
docker-compose up -d
```

## 🔧 Cấu hình

### Database Connection
- **Host**: sqlserver (internal) / localhost (external)
- **Port**: 1433
- **Database**: ecommerce
- **Username**: chuduchai1901
- **Password**: chuduchai1901

### Kết nối SQL Server từ máy local

Nếu muốn kết nối trực tiếp vào SQL Server từ SSMS hoặc tool khác:

```
Server: localhost,1433
Authentication: SQL Server Authentication
Login: sa
Password: YourStrong@Password123
```

Hoặc dùng user application:
```
Login: chuduchai1901
Password: chuduchai1901
Database: ecommerce
```

## 🐛 Troubleshooting

### Port đã được sử dụng

Nếu gặp lỗi port đã được sử dụng, sửa trong `docker-compose.yml`:

```yaml
services:
  shopapp:
    ports:
      - "8081:8080"  # Đổi 8080 thành port khác
  
  sqlserver:
    ports:
      - "1434:1433"  # Đổi 1433 thành port khác
```

### Ứng dụng không khởi động

```bash
# Xem logs chi tiết
docker-compose logs shopapp

# Restart
docker-compose restart shopapp
```

### SQL Server không khởi động được

```bash
# Kiểm tra logs
docker-compose logs sqlserver

# Xóa volume và tạo lại
docker-compose down -v
docker-compose up -d
```

### Container bị lỗi "unhealthy"

```bash
# Restart tất cả services
docker-compose restart

# Hoặc rebuild
docker-compose down
docker-compose up -d
```

## 📊 Kiến trúc

```
┌─────────────────────────────────────────┐
│         Docker Host                      │
│                                          │
│  ┌────────────────────────────────────┐ │
│  │   ecommerce-net (bridge)           │ │
│  │                                     │ │
│  │  ┌──────────────┐  ┌─────────────┐ │ │
│  │  │  shopapp     │  │  sqlserver  │ │ │
│  │  │   :8080      │──│   :1433     │ │ │
│  │  └──────────────┘  └─────────────┘ │ │
│  │         │                │          │ │
│  │         │          ┌─────▼────┐    │ │
│  │         │          │  volume  │    │ │
│  │         │          └──────────┘    │ │
│  └─────────┼─────────────────────────┐ │
│            │                          │ │
└────────────┼──────────────────────────┘ │
             │                             
      ┌──────▼───────┐                   
      │ Host Ports   │                   
      │ 8080, 1433   │                   
      └──────────────┘                   
```

## 📝 Environment Variables

Bạn có thể tùy chỉnh các biến môi trường trong `docker-compose.yml`:

```yaml
environment:
  SPRING_DATASOURCE_URL: "jdbc:sqlserver://sqlserver:1433;databaseName=ecommerce"
  SPRING_DATASOURCE_USERNAME: "your-username"
  SPRING_DATASOURCE_PASSWORD: "your-password"
  SPRING_JPA_HIBERNATE_DDL_AUTO: "update"  # update, create, create-drop, validate, none
  SERVER_PORT: "8080"
```

## 🔒 Security Notes

**⚠️ CHÚ Ý**: File docker-compose.yml này dành cho môi trường **development/testing**. 

Khi deploy production, cần:
- Thay đổi tất cả passwords
- Sử dụng Docker secrets hoặc environment variables file
- Enable SSL/TLS
- Sử dụng managed database service
- Thiết lập proper firewall rules

## 📞 Hỗ trợ

Nếu gặp vấn đề, vui lòng:
1. Kiểm tra logs: `docker-compose logs -f`
2. Kiểm tra services đang chạy: `docker-compose ps`
3. Restart: `docker-compose restart`

## 📄 License

[Thêm license của bạn ở đây]

## 👨‍💻 Author

[Thêm thông tin của bạn ở đây]
