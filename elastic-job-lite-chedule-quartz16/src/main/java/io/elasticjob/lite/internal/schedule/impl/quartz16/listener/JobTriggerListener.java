package io.elasticjob.lite.internal.schedule.impl.quartz16.listener;

import io.elasticjob.lite.internal.sharding.ExecutionService;
import io.elasticjob.lite.internal.sharding.ShardingService;
import lombok.RequiredArgsConstructor;
import org.quartz.Trigger;
import org.quartz.listeners.TriggerListenerSupport;

@RequiredArgsConstructor
public final class JobTriggerListener extends TriggerListenerSupport {

    private final ExecutionService executionService;

    private final ShardingService shardingService;

    @Override
    public String getName() {
        return "JobTriggerListener";
    }

    @Override
    public void triggerMisfired(final Trigger trigger) {
        if (null != trigger.getPreviousFireTime()) {
            executionService.setMisfire(shardingService.getLocalShardingItems());
        }
    }
}
