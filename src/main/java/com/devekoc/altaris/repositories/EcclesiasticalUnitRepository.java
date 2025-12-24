package com.devekoc.altaris.repositories;

import java.util.Optional;

public interface EcclesiasticalUnitRepository<T, ID> {
    boolean existsByName(String name);
    Optional<T> findById(ID id);
}
