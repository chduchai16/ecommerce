package com.example.shopapp.models.enums;

public enum UserStatus {
    ACTIVE,      // Người dùng đang hoạt động
    INACTIVE,    // Người dùng bị khóa hoặc ngừng hoạt động
    PENDING,     // Người dùng mới tạo, chưa xác thực
    BANNED,      // Người dùng vi phạm, bị cấm
    DELETED      // Người dùng đã bị xóa (soft delete)
}
