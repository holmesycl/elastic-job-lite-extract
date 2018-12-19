/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package io.elasticjob.lite.util.concurrent;

import com.google.common.base.Joiner;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.Getter;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.*;

/**
 * 线程池执行服务对象.
 *
 * @author zhangliang
 */
public final class ExecutorServiceObject {

    private final ThreadPoolExecutor threadPoolExecutor;

    @Getter
    private ExecutorService executorService;


    public ExecutorServiceObject(final String namingPattern, final int corePoolSize, final int maximumPoolSize) {
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 5L, TimeUnit.MINUTES, workQueue,
                new BasicThreadFactory.Builder().namingPattern(Joiner.on("-").join(namingPattern, "%s")).build());
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        this.executorService = MoreExecutors.listeningDecorator(MoreExecutors.getExitingExecutorService(threadPoolExecutor));
    }

    public ExecutorServiceObject(final String namingPattern, final int threadSize) {
        this(namingPattern, threadSize, threadSize);
    }

    public boolean isShutdown() {
        return threadPoolExecutor.isShutdown();
    }

    public int getActiveCount() {
        return threadPoolExecutor.getActiveCount();
    }

    public long getCompletedTaskCount() {
        return threadPoolExecutor.getCompletedTaskCount();
    }

    public int getCorePoolSize() {
        return threadPoolExecutor.getCorePoolSize();
    }

    public int getLargestPoolSize() {
        return threadPoolExecutor.getLargestPoolSize();
    }

    public int getMaximumPoolSize() {
        return threadPoolExecutor.getMaximumPoolSize();
    }

    public int getPoolSize() {
        return threadPoolExecutor.getPoolSize();
    }

    public long getTaskCount() {
        return threadPoolExecutor.getTaskCount();
    }

    public int getWorkQueueSize() {
        return threadPoolExecutor.getQueue().size();
    }

}
