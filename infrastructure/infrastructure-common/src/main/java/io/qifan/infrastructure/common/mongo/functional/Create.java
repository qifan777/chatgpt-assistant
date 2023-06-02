package io.qifan.infrastructure.common.mongo.functional;

import java.util.function.Supplier;


public interface Create<T> {

    UpdateHandler<T> create(Supplier<T> supplier);

}
