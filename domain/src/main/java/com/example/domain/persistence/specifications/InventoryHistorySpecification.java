package com.example.domain.persistence.specifications;

import com.example.domain.models.entities.InventoryHistory;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class InventoryHistorySpecification {

    public static Specification<InventoryHistory> hasSellerId(Integer sellerId) {
        return (root, query, criteriaBuilder) -> {
            if (sellerId == null) {
                return criteriaBuilder.conjunction();
            }
            var productJoin = root.join("product");
            var sellerJoin = productJoin.join("seller");
            query.orderBy(criteriaBuilder.desc(root.get("performedAt")));
            return criteriaBuilder.equal(sellerJoin.get("id"), sellerId);
        };
    }

    public static Specification<InventoryHistory> hasSellerIdAndActionImport(Integer sellerId) {
        return (root, query, criteriaBuilder) -> {
            if (sellerId == null) {
                return criteriaBuilder.conjunction();
            }
            var productJoin = root.join("product");
            var sellerJoin = productJoin.join("seller");
            query.orderBy(criteriaBuilder.desc(root.get("performedAt")));
            return criteriaBuilder.and(
                    criteriaBuilder.equal(sellerJoin.get("id"), sellerId),
                    criteriaBuilder.equal(root.get("action"), "IMPORT")
            );
        };
    }

    public static Specification<InventoryHistory> hasSellerIdAndActionExport(Integer sellerId) {
        return (root, query, criteriaBuilder) -> {
            if (sellerId == null) {
                return criteriaBuilder.conjunction();
            }
            var productJoin = root.join("product");
            var sellerJoin = productJoin.join("seller");
            query.orderBy(criteriaBuilder.desc(root.get("performedAt")));
            return criteriaBuilder.and(
                    criteriaBuilder.equal(sellerJoin.get("id"), sellerId),
                    criteriaBuilder.equal(root.get("action"), "EXPORT")
            );
        };
    }

    public static Specification<InventoryHistory> hasSellerIdAndActionAdjust(Integer sellerId) {
        return (root, query, criteriaBuilder) -> {
            if (sellerId == null) {
                return criteriaBuilder.conjunction();
            }
            var productJoin = root.join("product");
            var sellerJoin = productJoin.join("seller");
            query.orderBy(criteriaBuilder.desc(root.get("performedAt")));
            return criteriaBuilder.and(
                    criteriaBuilder.equal(sellerJoin.get("id"), sellerId),
                    criteriaBuilder.equal(root.get("action"), "ADJUST")
            );
        };
    }

    public static Specification<InventoryHistory> hasSellerIdAndDateBetween(
            Integer sellerId,
            LocalDateTime startDate,
            LocalDateTime endDate) {
        return (root, query, criteriaBuilder) -> {
            if (sellerId == null) {
                return criteriaBuilder.conjunction();
            }
            var productJoin = root.join("product");
            var sellerJoin = productJoin.join("seller");
            query.orderBy(criteriaBuilder.desc(root.get("performedAt")));

            if (startDate != null && endDate != null) {
                return criteriaBuilder.and(
                        criteriaBuilder.equal(sellerJoin.get("id"), sellerId),
                        criteriaBuilder.between(root.get("performedAt"), startDate, endDate)
                );
            } else if (startDate != null) {
                return criteriaBuilder.and(
                        criteriaBuilder.equal(sellerJoin.get("id"), sellerId),
                        criteriaBuilder.greaterThanOrEqualTo(root.get("performedAt"), startDate)
                );
            } else if (endDate != null) {
                return criteriaBuilder.and(
                        criteriaBuilder.equal(sellerJoin.get("id"), sellerId),
                        criteriaBuilder.lessThanOrEqualTo(root.get("performedAt"), endDate)
                );
            }
            return criteriaBuilder.equal(sellerJoin.get("id"), sellerId);
        };
    }

    /**
     * Lọc theo seller ID, action và date range
     */
    public static Specification<InventoryHistory> hasSellerIdActionAndDateBetween(
            Integer sellerId,
            String action,
            LocalDateTime startDate,
            LocalDateTime endDate) {
        return (root, query, criteriaBuilder) -> {
            if (sellerId == null || action == null) {
                return criteriaBuilder.conjunction();
            }
            var productJoin = root.join("product");
            var sellerJoin = productJoin.join("seller");
            query.orderBy(criteriaBuilder.desc(root.get("performedAt")));

            if (startDate != null && endDate != null) {
                return criteriaBuilder.and(
                        criteriaBuilder.equal(sellerJoin.get("id"), sellerId),
                        criteriaBuilder.equal(root.get("action"), action),
                        criteriaBuilder.between(root.get("performedAt"), startDate, endDate)
                );
            } else if (startDate != null) {
                return criteriaBuilder.and(
                        criteriaBuilder.equal(sellerJoin.get("id"), sellerId),
                        criteriaBuilder.equal(root.get("action"), action),
                        criteriaBuilder.greaterThanOrEqualTo(root.get("performedAt"), startDate)
                );
            } else if (endDate != null) {
                return criteriaBuilder.and(
                        criteriaBuilder.equal(sellerJoin.get("id"), sellerId),
                        criteriaBuilder.equal(root.get("action"), action),
                        criteriaBuilder.lessThanOrEqualTo(root.get("performedAt"), endDate)
                );
            }
            return criteriaBuilder.and(
                    criteriaBuilder.equal(sellerJoin.get("id"), sellerId),
                    criteriaBuilder.equal(root.get("action"), action)
            );
        };
    }


    public static Specification<InventoryHistory> hasProductId(Integer productId) {
        return (root, query, criteriaBuilder) -> {
            if (productId == null) {
                return criteriaBuilder.conjunction();
            }
            query.orderBy(criteriaBuilder.desc(root.get("performedAt")));
            return criteriaBuilder.equal(root.get("product").get("id"), productId);
        };
    }


    public static Specification<InventoryHistory> hasProductIdAndActionImport(Integer productId) {
        return (root, query, criteriaBuilder) -> {
            if (productId == null) {
                return criteriaBuilder.conjunction();
            }
            query.orderBy(criteriaBuilder.desc(root.get("performedAt")));
            return criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("product").get("id"), productId),
                    criteriaBuilder.equal(root.get("action"), "IMPORT")
            );
        };
    }


    public static Specification<InventoryHistory> hasProductIdAndAction(Integer productId, String action) {
        return (root, query, criteriaBuilder) -> {
            if (productId == null || action == null) {
                return criteriaBuilder.conjunction();
            }
            query.orderBy(criteriaBuilder.desc(root.get("performedAt")));
            return criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("product").get("id"), productId),
                    criteriaBuilder.equal(root.get("action"), action)
            );
        };
    }

    public static Specification<InventoryHistory> hasAction(String action) {
        return (root, query, criteriaBuilder) -> {
            if (action == null || action.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("action"), action);
        };
    }

    public static Specification<InventoryHistory> hasSellerIdAndAction(Integer sellerId, String action) {
        return (root, query, criteriaBuilder) -> {
            if (sellerId == null && (action == null || action.isEmpty())) {
                return criteriaBuilder.conjunction();
            }
            var productJoin = root.join("product");
            var sellerJoin = productJoin.join("seller");
            query.orderBy(criteriaBuilder.desc(root.get("performedAt")));

            if (sellerId != null && action != null && !action.isEmpty()) {
                return criteriaBuilder.and(
                        criteriaBuilder.equal(sellerJoin.get("id"), sellerId),
                        criteriaBuilder.equal(root.get("action"), action)
                );
            } else if (sellerId != null) {
                return criteriaBuilder.equal(sellerJoin.get("id"), sellerId);
            } else {
                return criteriaBuilder.equal(root.get("action"), action);
            }
        };
    }

    public static Specification<InventoryHistory> hasSellerIdAndProductName(Integer sellerId, String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (sellerId == null) {
                return criteriaBuilder.conjunction();
            }
            var productJoin = root.join("product");
            var sellerJoin = productJoin.join("seller");
            query.orderBy(criteriaBuilder.desc(root.get("performedAt")));

            if (keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.equal(sellerJoin.get("id"), sellerId);
            }
            return criteriaBuilder.and(
                    criteriaBuilder.equal(sellerJoin.get("id"), sellerId),
                    criteriaBuilder.like(criteriaBuilder.lower(productJoin.get("name")),
                            "%" + keyword.toLowerCase() + "%")
            );
        };
    }

    public static Specification<InventoryHistory> hasDateBetween(
            LocalDateTime startDate,
            LocalDateTime endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null && endDate == null) {
                return criteriaBuilder.conjunction();
            }
            if (startDate != null && endDate != null) {
                return criteriaBuilder.between(root.get("performedAt"), startDate, endDate);
            } else if (startDate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("performedAt"), startDate);
            } else {
                return criteriaBuilder.lessThanOrEqualTo(root.get("performedAt"), endDate);
            }
        };
    }

    public static Specification<InventoryHistory> orderByPerformedAtDesc() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("performedAt")));
            return criteriaBuilder.conjunction();
        };
    }
}

