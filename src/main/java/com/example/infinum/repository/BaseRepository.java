package com.example.infinum.repository;

import com.example.infinum.domain.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity<ID>, ID>
        extends JpaRepository<T, ID>, QuerydslPredicateExecutor<T> {

    List<T> findByActiveTrueOrderByCreatedDate();

    Optional<T> findByIdAndActiveTrue(ID id);

    @Modifying
    @Query("UPDATE #{#entityName} e SET e.active = false WHERE e.id = :id AND e.active = true")
    int softDeleteById(@Param("id") ID id);
}
