package com.devekoc.altaris.repositories;

import com.devekoc.altaris.entities.Chaplain;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ChaplainRepository extends
        JpaRepository<@NonNull Chaplain, @NonNull Integer>,
        JpaSpecificationExecutor<@NonNull Chaplain>
{

}
