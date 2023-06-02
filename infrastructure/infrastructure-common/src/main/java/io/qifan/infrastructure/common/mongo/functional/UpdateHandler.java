package io.qifan.infrastructure.common.mongo.functional;

import java.util.function.Consumer;


public interface UpdateHandler<T> {

    Executor<T> update(Consumer<T> consumer);

}
