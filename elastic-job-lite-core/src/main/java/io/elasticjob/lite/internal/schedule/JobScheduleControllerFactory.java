package io.elasticjob.lite.internal.schedule;

import io.elasticjob.lite.config.JobRootConfiguration;
import io.elasticjob.lite.executor.JobFacade;

public interface JobScheduleControllerFactory {

    JobScheduleController create(SchedulerFacade schedulerFacade, JobFacade jobFacade, JobRootConfiguration jobRootConfiguration);

}
