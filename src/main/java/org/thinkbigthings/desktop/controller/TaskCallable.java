package org.thinkbigthings.desktop.controller;

import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public interface TaskCallable<T> extends Callable<T> {

    void setCancelledStatusSupplier(Supplier<Boolean> cancellable);

    void setProgressStatusCallback(BiConsumer<Long, Long> updater);
}
