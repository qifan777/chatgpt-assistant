package io.qifan.infrastructure.common.mongo.functional;

import java.util.Optional;
import java.util.function.Consumer;


public interface Executor<T> {

    Optional<T> execute();

    Executor<T> successHook(Consumer<T> consumer);

    Executor<T> errorHook(Consumer<? super Throwable> consumer);

}
