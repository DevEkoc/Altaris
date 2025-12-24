package com.devekoc.altaris.specifications;

import com.devekoc.altaris.entities.Province;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;

public class ProvinceSpecification {

    public static Specification<@NonNull Province> globalSearch(String text) {
        return (root, query, cb) -> {
            if (text == null || text.isEmpty()) {
                return cb.conjunction();
            }

            String pattern = "%" + text.toLowerCase() + "%";

            Join<Object, Object> chaplainJoin = root.join("chaplain", JoinType.LEFT);
            Join<Object, Object> officeJoin = root.join("office", JoinType.LEFT);


            return cb.or(
                    cb.like(cb.lower(root.get("name")), pattern),
                    cb.like(cb.lower(root.get("description")), pattern),
                    cb.like(cb.lower(root.get("saintPatron")), pattern),
                    cb.like(cb.lower(root.get("localite")), pattern),
                    cb.like(cb.lower(root.get("headquarter")), pattern),
                    cb.like(cb.lower(root.get("archbishop")), pattern),

                    cb.like(cb.lower(chaplainJoin.get("name")), pattern),
                    cb.like(cb.lower(chaplainJoin.get("surname")), pattern),

                    cb.like(cb.lower(officeJoin.get("description")), pattern)
            );
        };
    }
}
