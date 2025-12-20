package com.devekoc.altaris.repositories;

import com.devekoc.altaris.entities.Province;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinceRepository extends JpaRepository< @NonNull Province, @NonNull Integer> {
    boolean existsByName(String name);
}
