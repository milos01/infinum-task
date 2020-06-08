package com.example.infinum.service.impl;

import com.example.infinum.domain.BaseEntity;
import com.example.infinum.exceptions.NotFoundException;
import com.example.infinum.repository.BaseRepository;
import com.example.infinum.service.BaseService;
import com.querydsl.core.types.dsl.PathBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
public abstract class BaseServiceImpl<D, T extends BaseEntity<ID>, ID> implements BaseService<D, ID> {

    protected final BaseRepository<T, ID> repository;
    protected final ModelMapper mapper;

    private final PathBuilder<T> entityPath;

    private Class<D> dtoType;
    private Class<T> entityType;
    private Class<ID> keyType;

    @SuppressWarnings("unchecked")
    public BaseServiceImpl(BaseRepository<T, ID> baseRepository, ModelMapper mapper) {
        this.repository = baseRepository;
        this.mapper = mapper;

        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;

        this.dtoType = (Class<D>) pt.getActualTypeArguments()[0];
        this.entityType = (Class<T>) pt.getActualTypeArguments()[1];
        this.keyType = (Class<ID>) pt.getActualTypeArguments()[2];

        this.entityPath = new PathBuilder<>(this.entityType, this.entityType.getSimpleName().toLowerCase());
    }

    @Override
    @Transactional(readOnly = true)
    public D findById(ID id) {
        return this.repository.findByIdAndActiveTrue(this.keyType.cast(id))
                .map(this::toDto)
                .orElseThrow(() -> new NotFoundException(this.entityType, "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<D> findAll() {
        return this.repository.findByActiveTrueOrderByCreatedDate()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public D save(D entityDto) {
        T entity = this.toEntity(entityDto);
        this.repository.save(entity);
        return this.toDto(entity);
    }

    @Override
    @Transactional
    public void deleteById(ID id) {
        int rowCount = this.repository.softDeleteById(this.keyType.cast(id));

        if (rowCount == 0) {
            throw new NotFoundException(this.entityType, "id", id);
        }
    }

    private T toEntity(D dto) {
        return this.mapper.map(dto, this.entityType);
    }

    private D toDto(T entity) {
        return this.mapper.map(entity, this.dtoType);
    }
}
