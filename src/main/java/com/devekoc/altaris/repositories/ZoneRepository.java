package com.devekoc.altaris.repositories;

import com.devekoc.altaris.entities.Zone;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ZoneRepository extends
        JpaRepository<@NonNull Zone, @NonNull Integer>,
        JpaSpecificationExecutor<@NonNull Zone>,
        EcclesiasticalUnitRepository<@NonNull Zone, Integer>
{

}
