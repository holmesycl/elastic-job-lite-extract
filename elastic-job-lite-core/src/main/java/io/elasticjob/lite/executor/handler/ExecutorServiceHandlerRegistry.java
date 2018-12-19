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

import io.elasticjob.lite.util.concurrent.ExecutorServiceObject;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * 线程池服务处理器注册表.
 *
 * @author zhangliang
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExecutorServiceHandlerRegistry {

    private static final Map<String, ExecutorServiceObject> REGISTRY = new HashMap<>();

    public static synchronized ExecutorServiceObject getExecutorServiceHandler(final String executorServiceName, final ExecutorServiceHandler executorServiceHandler) {
        if (!REGISTRY.containsKey(executorServiceName)) {
            REGISTRY.put(executorServiceName, executorServiceHandler.createExecutorService(executorServiceName));
        }
        return REGISTRY.get(executorServiceName);
    }

    public static synchronized ExecutorServiceObject resetExecutorServiceHandler(final String executorServiceName, final ExecutorServiceHandler executorServiceHandler) {
        getExecutorServiceHandler(executorServiceName, null).getExecutorService().shutdown();
        REGISTRY.put(executorServiceName, executorServiceHandler.createExecutorService(executorServiceName));
        return REGISTRY.get(executorServiceName);
    }
}
