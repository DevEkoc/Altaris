package com.devekoc.altaris.repositories;

import com.devekoc.altaris.entities.EcclesiasticalUnit;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EcclesiasticalUnitBaseRepository
        extends JpaRepository<@NonNull EcclesiasticalUnit, @NonNull Integer>
{

}

