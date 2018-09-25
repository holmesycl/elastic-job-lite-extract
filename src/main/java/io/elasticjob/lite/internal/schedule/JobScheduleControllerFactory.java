package io.elasticjob.lite.internal.schedule;

import io.elasticjob.lite.config.JobRootConfiguration;
import io.elasticjob.lite.executor.JobFacade;
import lombok.Data;

@Data
public abstract class JobScheduleControllerFactory {

    private SchedulerFacade schedulerFacade;

    private JobFacade jobFacade;

    private JobRootConfiguration jobRootConfiguration;

    public abstract JobScheduleController create();
}
