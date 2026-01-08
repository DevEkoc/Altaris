package com.devekoc.altaris.repositories;

import com.devekoc.altaris.entities.Assignment;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AssignmentRepository extends
        JpaRepository<@NonNull Assignment, @NonNull Integer>,
        JpaSpecificationExecutor<@NonNull Assignment>
{
    boolean existsByOfficeId(Integer id);

    boolean existsByPositionAndOfficeId(String position, Integer officeId);
}
