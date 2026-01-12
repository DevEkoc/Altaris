package com.devekoc.altaris.repositories;

import com.devekoc.altaris.entities.Parish;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ParishRepository extends
        JpaRepository<@NonNull Parish, @NonNull Integer>,
        JpaSpecificationExecutor<@NonNull Parish>,
        EcclesiasticalUnitRepository<@NonNull Parish, Integer>
{

}
