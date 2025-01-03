package com.test.task.novisign.model.mapper;

public interface BaseMapper<E, D> {

    E toEntity(D dto);

    D toDto(E entity);
}
