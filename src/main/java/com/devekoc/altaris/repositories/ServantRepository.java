package com.devekoc.altaris.repositories;

import com.devekoc.altaris.entities.Servant;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ServantRepository extends
        JpaRepository<@NonNull Servant, @NonNull Integer>,
        JpaSpecificationExecutor<@NonNull Servant>,
        EcclesiasticalUnitRepository<@NonNull Servant, Integer>
{

}
