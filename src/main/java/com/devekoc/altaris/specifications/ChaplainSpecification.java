package com.devekoc.altaris.specifications;

import com.devekoc.altaris.entities.Chaplain;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;

public class ChaplainSpecification {
    public static Specification<@NonNull Chaplain> globalSearch(String text) {
        return (root, query, cb) -> {
            if (text == null || text.isEmpty()) {
                return cb.conjunction();
            }

            String pattern = "%" + text.toLowerCase() + "%";

            return cb.or(
                    // Les champs propres au Chaplain
                    cb.like(cb.lower(root.get("name")), pattern),
                    cb.like(cb.lower(root.get("surname")), pattern),
                    cb.like(cb.lower(root.get("priestlyRank")), pattern),
                    cb.like(cb.lower(root.get("phone")), pattern)
            );
        };
    }

}
