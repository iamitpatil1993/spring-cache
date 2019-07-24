package com.example.spring.cahce.stragegy;

import java.util.Optional;

/**
 * defines contract for read operation with cache support.
 *
 * @param <EntityType> Entity Type to read
 * @param <ID>         Id type of entity primary key
 * @author amit
 */
public interface ReadOperation<EntityType, ID> {

    Optional<EntityType> read(final ID id);
}
