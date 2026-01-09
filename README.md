# E-Commerce Application

Ứng dụng thương mại điện tử được xây dựng bằng Spring Boot, cung cấp đầy đủ các tính năng quản lý sản phẩm, đơn hàng, giỏ hàng và người dùng.

## Công nghệ sử dụng

- **Backend Framework**: Spring Boot 3.3.3
- **Java Version**: 17
- **Database**: SQL Server / MySQL
- **Authentication**: JWT (JSON Web Token)
- **ORM**: Spring Data JPA / Hibernate
- **Security**: Spring Security
- **API Documentation**: Swagger/OpenAPI
- **Build Tool**: Maven
- **Others**: Lombok, ModelMapper

## Tính năng chính

- **Quản lý người dùng**: Đăng ký, đăng nhập, phân quyền
- **Quản lý sản phẩm**: CRUD sản phẩm, phân loại, tìm kiếm
- **Quản lý giỏ hàng**: Thêm, xóa, cập nhật sản phẩm trong giỏ
- **Quản lý đơn hàng**: Đặt hàng, theo dõi trạng thái
- **Quản lý danh mục**: Phân loại sản phẩm theo danh mục
- **Quản lý kho**: Theo dõi số lượng tồn kho
- **Quản lý coupon**: Tạo và áp dụng mã giảm giá
- **Đánh giá sản phẩm**: Cho phép người dùng đánh giá và nhận xét
- **Upload media**: Quản lý hình ảnh sản phẩm và avatar người dùng
- **Xác thực JWT**: Bảo mật API với token

## Cấu trúc dự án

```
ecommerce/
├── domain/                     # Module domain chung
│   └── src/main/java/
├── shopapp/                    # Module ứng dụng chính
│   ├── src/main/java/com/example/shopapp/
│   │   ├── configurations/    # Cấu hình Spring
│   │   ├── controllers/       # REST API endpoints
│   │   ├── filters/           # Security filters
│   │   ├── helpers/           # Utility classes
│   │   ├── models/            # Entity và Enum
│   │   ├── repositories/      # Data access layer
│   │   ├── services/          # Business logic
│   │   ├── specifications/    # JPA specifications
│   │   └── transfer/          # DTOs và Mappers
│   └── src/main/resources/
│       └── application.properties
├── product_images/            # Thư mục lưu ảnh sản phẩm
├── user_avatars/              # Thư mục lưu avatar
├── init.sql                   # Script khởi tạo database
└── README.md
```

## API Endpoints

Base URL: `http://localhost:8080/api`

### Authentication
- POST `/auth/register` - Đăng ký tài khoản
- POST `/auth/login` - Đăng nhập

### Users
- GET `/users` - Lấy danh sách người dùng
- GET `/users/{id}` - Lấy thông tin người dùng
- PUT `/users/{id}` - Cập nhật thông tin
- DELETE `/users/{id}` - Xóa người dùng

### Products
- GET `/products` - Lấy danh sách sản phẩm (có phân trang)
- GET `/products/{id}` - Lấy chi tiết sản phẩm
- POST `/products` - Tạo sản phẩm mới
- PUT `/products/{id}` - Cập nhật sản phẩm
- DELETE `/products/{id}` - Xóa sản phẩm

### Categories
- GET `/categories` - Lấy danh sách danh mục
- POST `/categories` - Tạo danh mục mới
- PUT `/categories/{id}` - Cập nhật danh mục
- DELETE `/categories/{id}` - Xóa danh mục

### Carts
- GET `/carts` - Lấy giỏ hàng
- POST `/carts/items` - Thêm sản phẩm vào giỏ
- PUT `/carts/items/{id}` - Cập nhật số lượng
- DELETE `/carts/items/{id}` - Xóa sản phẩm khỏi giỏ

### Orders
- GET `/orders` - Lấy danh sách đơn hàng
- GET `/orders/{id}` - Lấy chi tiết đơn hàng
- POST `/orders` - Tạo đơn hàng mới
- PUT `/orders/{id}` - Cập nhật trạng thái đơn hàng

### Coupons
- GET `/coupons` - Lấy danh sách coupon
- POST `/coupons` - Tạo coupon mới
- GET `/coupons/validate` - Kiểm tra mã coupon

### Ratings
- GET `/ratings/product/{productId}` - Lấy đánh giá của sản phẩm
- POST `/ratings` - Tạo đánh giá mới

### Inventory
- GET `/inventory` - Kiểm tra tồn kho
- PUT `/inventory/{productId}` - Cập nhật số lượng tồn kho

### Media
- POST `/media/upload` - Upload file
- GET `/media/{filename}` - Lấy file

### Roles
- GET `/roles` - Lấy danh sách vai trò

## Hướng dẫn cài đặt

### Yêu cầu

- Java 17 trở lên
- Maven 3.6+
- SQL Server hoặc MySQL
- IDE: IntelliJ IDEA / Eclipse

### Các bước cài đặt

1. **Clone repository**
   ```bash
   git clone <repository-url>
   cd ecommerce
   ```

2. **Cấu hình database**
   
   Chạy script `init.sql` để tạo database:
   ```sql
   CREATE DATABASE ecommerce;
   ```

3. **Cấu hình application.properties**
   
   Cập nhật file `shopapp/src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=ecommerce
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

4. **Build project**
   ```bash
   cd shopapp
   mvn clean install
   ```

5. **Chạy ứng dụng**
   ```bash
   mvn spring-boot:run
   ```
## Cấu hình

### Database
- Database được cấu hình tự động tạo/cập nhật schema với `spring.jpa.hibernate.ddl-auto=update`
- Có thể chuyển đổi giữa SQL Server và MySQL bằng cách thay đổi datasource URL

### JWT Authentication
- Secret key được cấu hình trong `application.properties`
- Token được sử dụng để xác thực các API requests

### File Upload
- Ảnh sản phẩm: `/product_images`
- Avatar người dùng: `/user_avatars`

## Docker

Build và chạy với Docker:

```bash
docker build -t ecommerce-app .
docker run -p 8080:8080 ecommerce-app
```

## Testing

Chạy tests:
```bash
mvn test
```

## Tác giả

Chu Đức Hải

## License

This project is for educational purposes.