package com.devekoc.altaris.repositories;

import com.devekoc.altaris.entities.Province;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProvinceRepository extends
        JpaRepository< @NonNull Province, @NonNull Integer>,
        JpaSpecificationExecutor<@NonNull Province>,
        EcclesiasticalUnitRepository<@NonNull Province, Integer>
{
    boolean existsByName(String name);

}
