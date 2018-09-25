package io.elasticjob.lite.internal.schedule.impl.quartz16.plugin;

import io.elasticjob.lite.internal.election.LeaderService;
import io.elasticjob.lite.internal.instance.InstanceService;
import io.elasticjob.lite.internal.schedule.JobRegistry;
import io.elasticjob.lite.reg.base.CoordinatorRegistryCenter;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.plugins.management.ShutdownHookPlugin;

public final class JobShutdownHookPlugin extends ShutdownHookPlugin {

    private String jobName;

    @Override
    public void initialize(final String name, final Scheduler scheduler) throws SchedulerException {
        super.initialize(name, scheduler);
        jobName = scheduler.getSchedulerName();
    }

    @Override
    public void shutdown() {
        CoordinatorRegistryCenter regCenter = JobRegistry.getInstance().getRegCenter(jobName);
        if (null == regCenter) {
            return;
        }
        LeaderService leaderService = new LeaderService(regCenter, jobName);
        if (leaderService.isLeader()) {
            leaderService.removeLeader();
        }
        new InstanceService(regCenter, jobName).removeInstance();
    }
}