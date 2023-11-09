package com.self.demo01.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadPoolUtil {

    private ThreadPoolUtil() {
        threadPool = Executors.newSingleThreadExecutor();
    }

    private static class Holder {
        @SuppressWarnings({"FieldMayBeFinal", "CanBeFinal"})
        private static ThreadPoolUtil INSTANCE = new ThreadPoolUtil();
    }

    public static ThreadPoolUtil getInstance() {
        return Holder.INSTANCE;
    }

    @SuppressWarnings("CanBeFinal")
    public ExecutorService threadPool;

    /**
     * 无返回值直接执行
     *
     * @param runnable
     */
    @SuppressWarnings("JavaDoc")
    public void execute(Runnable runnable) {
        threadPool.execute(runnable);
    }

    /**
     * 返回值直接执行
     *
     * @param callable
     */
    @SuppressWarnings("JavaDoc")
    public <T> Future<T> submit(Callable<T> callable) {
        return threadPool.submit(callable);
    }

    /**
     * 关闭线程池
     */
    public void shutdown() {
        threadPool.shutdown();
    }


}
