package com.devekoc.altaris.specifications;

import com.devekoc.altaris.entities.Servant;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;

public class ServantSpecification {
    public static Specification<@NonNull Servant> globalSearch(String text) {
        return (root, query, cb) -> {
            if (text == null || text.isEmpty()) {
                return cb.conjunction();
            }

            String pattern = "%" + text.toLowerCase() + "%";

            Join<Object, Object> parishJoin = root.join("parish", JoinType.LEFT);


            return cb.or(
                    // Les champs propres au Servant
                    cb.like(cb.lower(root.get("serialNumber")), pattern),
                    cb.like(cb.lower(root.get("name")), pattern),
                    cb.like(cb.lower(root.get("surname")), pattern),
                    cb.like(cb.lower(root.get("gender")), pattern),
                    cb.like(cb.lower(root.get("grade")), pattern),
                    cb.like(cb.lower(root.get("phone")), pattern),


                    // Les champs de la Paroisse à laquelle est relié le Servant
                    cb.like(cb.lower(parishJoin.get("name")), pattern)
            );
        };
    }

}
