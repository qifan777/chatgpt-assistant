package io.qifan.infrastructure.common.mongo.functional;

import java.util.function.Supplier;


public interface Loader<T, ID> {

    UpdateHandler<T> loadById(ID id);

    UpdateHandler<T> load(Supplier<T> t);

}
