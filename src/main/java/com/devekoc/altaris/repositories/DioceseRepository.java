package com.devekoc.altaris.repositories;

import com.devekoc.altaris.entities.Diocese;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DioceseRepository extends
        JpaRepository<@NonNull Diocese, @NonNull Integer>,
        JpaSpecificationExecutor<@NonNull Diocese>,
        EcclesiasticalUnitRepository<@NonNull Diocese, Integer>
{

}
