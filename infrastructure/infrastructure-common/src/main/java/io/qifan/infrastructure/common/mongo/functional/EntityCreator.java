package io.qifan.infrastructure.common.mongo.functional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author qifan
 */

@Slf4j
public class EntityCreator<T, ID> implements Create<T>, UpdateHandler<T>, Executor<T> {

    private final CrudRepository<T, ID> repository;
    private T entity;
    private Consumer<T> successHook = t -> log.info("save success");
    private Consumer<? super Throwable> errorHook = e -> e.printStackTrace();

    public EntityCreator(CrudRepository<T, ID> repository) {
        this.repository = repository;
    }


    @Override
    public Executor<T> errorHook(Consumer<? super Throwable> consumer) {
        this.errorHook = consumer;
        return this;
    }

    @Override
    public UpdateHandler<T> create(Supplier<T> supplier) {
        this.entity = supplier.get();
        return this;
    }

    @Override
    public Executor<T> update(Consumer<T> consumer) {
        consumer.accept(this.entity);
        return this;
    }

    @Override
    public Optional<T> execute() {
        T save = repository.save(entity);
        successHook.accept(entity);
        return Optional.of(save);
    }

    @Override
    public Executor<T> successHook(Consumer<T> consumer) {
        this.successHook = consumer;
        return this;
    }

}

