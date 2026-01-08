package com.devekoc.altaris.specifications;

import com.devekoc.altaris.entities.Office;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;

public class OfficeSpecification {
    public static Specification<@NonNull Office> globalSearch(String text) {
        return (root, query, cb) -> {
            if (text == null || text.isEmpty()) {
                return cb.conjunction();
            }

            String pattern = "%" + text.toLowerCase() + "%";

            return cb.or(
                    // Les champs propres au Bureau
                    cb.like(cb.lower(root.get("active")), pattern),
                    cb.like(cb.lower(root.get("description")), pattern)
            );
        };
    }

}
