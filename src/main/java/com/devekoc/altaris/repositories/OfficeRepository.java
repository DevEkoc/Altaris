package com.devekoc.altaris.repositories;

import com.devekoc.altaris.entities.Office;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OfficeRepository extends
        JpaRepository<@NonNull Office, @NonNull Integer>,
        JpaSpecificationExecutor<@NonNull Office>
{

}
