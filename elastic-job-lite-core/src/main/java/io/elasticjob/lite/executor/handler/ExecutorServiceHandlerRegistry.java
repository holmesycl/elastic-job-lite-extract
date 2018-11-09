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

package io.elasticjob.lite.executor.handler;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * 线程池服务处理器注册表.
 *
 * @author zhangliang
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExecutorServiceHandlerRegistry {

    private static final Map<String, ExecutorService> REGISTRY = new HashMap<>();

    /**
     * @param executorServiceName
     * @param executorService
     * @return
     */
    public static synchronized void registry(final String executorServiceName, final ExecutorService executorService) {
        if (!REGISTRY.containsKey(executorServiceName)) {
            REGISTRY.put(executorServiceName, executorService);
        }
    }

    public static synchronized ExecutorService getExecutorServiceHandler(final String executorServiceName, final ExecutorServiceHandler executorServiceHandler) {
        if (!REGISTRY.containsKey(executorServiceName)) {
            REGISTRY.put(executorServiceName, executorServiceHandler.createExecutorService(executorServiceName));
        }
        return REGISTRY.get(executorServiceName);
    }

    public static synchronized ExecutorService obtain(final String executorServiceName) {
        if (!REGISTRY.containsKey(executorServiceName)) {
            throw new RuntimeException("不存在的线程池服务。");
        }
        return REGISTRY.get(executorServiceName);
    }

    /**
     * 从注册表中删除该作业线程池服务.
     *
     * @param executorServiceName 名称
     */
    public static synchronized void remove(final String executorServiceName) {
        REGISTRY.remove(executorServiceName);
    }
}
