package com.example.domain.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "fullname", columnDefinition = "NVARCHAR(255)")
    private String fullName;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "email", columnDefinition = "NVARCHAR(255)")
    private String email;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "avatar", columnDefinition = "NVARCHAR(500)")
    private String avatar;

    @Column(name = "address", columnDefinition = "NVARCHAR(255)")
    private String address;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender", length = 10, columnDefinition = "NVARCHAR(255)")
    private String gender;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonBackReference
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "status")
    private Integer status;

    @Column(name = "shop_name", columnDefinition = "NVARCHAR(255)")
    private String shopName;

    @Column(name = "shop_description", columnDefinition = "NVARCHAR(1000)")
    private String shopDescription;

    @Column(name = "shop_logo", columnDefinition = "NVARCHAR(500)")
    private String shopLogo;

    @Column(name = "business_license", columnDefinition = "NVARCHAR(255)")
    private String businessLicense;

    @Column(name = "tax_code", columnDefinition = "NVARCHAR(50)")
    private String taxCode;

    @Column(name = "seller_rating", columnDefinition = "DECIMAL(2,1) DEFAULT 0")
    @Builder.Default
    private Double sellerRating = 0.0;

    @Column(name = "total_sales", columnDefinition = "BIGINT DEFAULT 0")
    @Builder.Default
    private Long totalSales = 0L;

    @Column(name = "is_verified", columnDefinition = "BIT DEFAULT 0")
    @Builder.Default
    private Boolean isVerified = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.getName().toUpperCase()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status != null && status != 3;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status != null && status == 0;
    }
}
