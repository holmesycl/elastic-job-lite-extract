package io.elasticjob.lite.internal.schedule.impl.quartz16.job;

import io.elasticjob.lite.api.ElasticJob;
import io.elasticjob.lite.executor.JobExecutorFactory;
import io.elasticjob.lite.executor.JobFacade;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class LiteJob implements Job {

    public static final Object ELASTIC_JOB_DATA_MAP_KEY = "ELASTIC_JOB_DATA_MAP_KEY";

    public static final String JOB_FACADE_DATA_MAP_KEY = "JOB_FACADE_DATA_MAP_KEY";

    @Override
    public void execute(final JobExecutionContext context) throws JobExecutionException {
        ElasticJob elasticJob = (ElasticJob) context.getJobDetail().getJobDataMap().get(ELASTIC_JOB_DATA_MAP_KEY);
        JobFacade jobFacade = (JobFacade) context.getJobDetail().getJobDataMap().get(JOB_FACADE_DATA_MAP_KEY);
        JobExecutorFactory.getJobExecutor(elasticJob, jobFacade).execute();
    }

}
