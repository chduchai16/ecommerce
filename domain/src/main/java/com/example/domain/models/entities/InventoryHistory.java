package com.example.domain.models.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class InventoryHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "action", nullable = false, length = 50)
    private String action; // IMPORT, EXPORT, ADJUST

    @Column(name = "quantity_before")
    private Integer quantityBefore;

    @Column(name = "quantity_after")
    private Integer quantityAfter;

    @Column(name = "quantity_change", nullable = false)
    private Integer quantityChange;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User performedBy;

    @Column(name = "reason", columnDefinition = "NVARCHAR(255)")
    private String reason;

    @Column(name = "note", columnDefinition = "NVARCHAR(255)")
    private String note;

    @Column(name = "performed_at", nullable = false)
    private LocalDateTime performedAt;
}
