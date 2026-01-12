package com.devekoc.altaris.repositories;

import com.devekoc.altaris.entities.Office;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OfficeRepository extends
        JpaRepository<@NonNull Office, @NonNull Integer>,
        JpaSpecificationExecutor<@NonNull Office>
{
    boolean existsByUnitIdAndActiveTrue(Integer id);

    boolean existsByUnitIdAndActiveTrueAndIdNot(Integer id, int id1);

    Optional<Office> findByUnitId(Integer unitId);

    boolean existsByUnitId(int unitId);
}
