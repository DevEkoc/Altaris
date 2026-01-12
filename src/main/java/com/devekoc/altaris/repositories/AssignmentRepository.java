package com.devekoc.altaris.repositories;

import com.devekoc.altaris.entities.Assignment;
import com.devekoc.altaris.enumerations.EcclesiasticalLevel;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AssignmentRepository extends
        JpaRepository<@NonNull Assignment, @NonNull Integer>,
        JpaSpecificationExecutor<@NonNull Assignment>
{
    boolean existsByOfficeId(Integer id);

    boolean existsByPositionAndOfficeId(String position, Integer officeId);

    boolean existsByPositionAndOfficeIdAndIdNot(String position, Integer officeId, int id);

    boolean existsByServantIdAndOfficeId(Integer id, Integer id1);

    boolean existsByServantIdAndOfficeEcclesiasticalLevel(Integer id, EcclesiasticalLevel ecclesiasticalLevel);

    boolean existsByServant_IdAndOffice_EcclesiasticalLevelAndOffice_ActiveTrue(Integer id, EcclesiasticalLevel ecclesiasticalLevel);

    boolean existsByServant_IdAndOffice_EcclesiasticalLevelAndOffice_ActiveTrueAndIdNot(Integer id, EcclesiasticalLevel ecclesiasticalLevel, Integer assignmentId);

    List<Assignment> findByOfficeId(Integer id);
    List<Assignment> findByOfficeIdIn(List<Integer> ids);
}
