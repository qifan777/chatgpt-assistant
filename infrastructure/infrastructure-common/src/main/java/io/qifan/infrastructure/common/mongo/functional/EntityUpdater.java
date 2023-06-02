package io.qifan.infrastructure.common.mongo.functional;


import io.qifan.infrastructure.common.constants.ResultCode;
import io.qifan.infrastructure.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.CrudRepository;
import org.springframework.util.Assert;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;


@Slf4j
public class EntityUpdater<T, ID> implements Loader<T, ID>, UpdateHandler<T>, Executor<T> {

    private final CrudRepository<T, ID> repository;
    private T entity;
    private Consumer<T> successHook = t -> log.info("update success");
    private Consumer<? super Throwable> errorHook = e -> e.printStackTrace();

    public EntityUpdater(CrudRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    public Optional<T> execute() {
        T save = repository.save(entity);
        successHook.accept(save);
        return Optional.of(save);
    }

    @Override
    public UpdateHandler<T> loadById(ID id) {
        Assert.notNull(id, "id不能为空");
        Optional<T> loadEntity = repository.findById(id);
        this.entity = loadEntity.orElseThrow(() -> new BusinessException(ResultCode.NotFindError));
        return this;
    }

    @Override
    public UpdateHandler<T> load(Supplier<T> t) {
        this.entity = t.get();
        Assert.notNull(entity, "id不能为空");
        return this;
    }

    @Override
    public Executor<T> update(Consumer<T> consumer) {
        Assert.notNull(entity, "entity不能为空");
        consumer.accept(this.entity);
        return this;
    }

    @Override
    public Executor<T> successHook(Consumer<T> consumer) {
        this.successHook = consumer;
        return this;
    }

    @Override
    public Executor<T> errorHook(Consumer<? super Throwable> consumer) {
        this.errorHook = consumer;
        return this;
    }

}