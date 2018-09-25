package io.elasticjob.lite.internal.schedule.impl.quartz16.controller;

import com.google.common.base.Optional;
import io.elasticjob.lite.api.ElasticJob;
import io.elasticjob.lite.api.script.ScriptJob;
import io.elasticjob.lite.exception.JobConfigurationException;
import io.elasticjob.lite.exception.JobSystemException;
import io.elasticjob.lite.internal.schedule.JobScheduleController;
import io.elasticjob.lite.internal.schedule.JobScheduleControllerFactory;
import io.elasticjob.lite.internal.schedule.impl.quartz16.job.LiteJob;
import io.elasticjob.lite.internal.schedule.impl.quartz16.listener.JobTriggerListener;
import io.elasticjob.lite.internal.schedule.impl.quartz16.plugin.JobShutdownHookPlugin;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Properties;

@Slf4j
public class Quartz16JobScheduleControllerFactory extends JobScheduleControllerFactory {

    public JobScheduleController create() {
        return new Quartz16JobScheduleController(createScheduler(), createJobDetail(), getJobRootConfiguration().getTypeConfig().getCoreConfig().getJobName());
    }

    private JobDetail createJobDetail() {
        JobDetail jobDetail = new JobDetail(getJobRootConfiguration().getTypeConfig().getCoreConfig().getJobName(), null, LiteJob.class);
        jobDetail.getJobDataMap().put(LiteJob.JOB_FACADE_DATA_MAP_KEY, getJobFacade());
        String jobClass = getJobRootConfiguration().getTypeConfig().getJobClass();
        Optional<ElasticJob> elasticJobInstance = createElasticJobInstance();
        if (elasticJobInstance.isPresent()) {
            jobDetail.getJobDataMap().put(LiteJob.ELASTIC_JOB_DATA_MAP_KEY, elasticJobInstance.get());
        } else if (!jobClass.equals(ScriptJob.class.getCanonicalName())) {
            try {
                jobDetail.getJobDataMap().put(LiteJob.ELASTIC_JOB_DATA_MAP_KEY, Class.forName(jobClass).newInstance());
            } catch (final ReflectiveOperationException ex) {
                throw new JobConfigurationException("Elastic-Job: Job class '%s' can not initialize.", jobClass);
            }
        }
        return jobDetail;
    }

    private Optional<ElasticJob> createElasticJobInstance() {
        return Optional.absent();
    }

    private Scheduler createScheduler() {
        Scheduler result;
        try {
            StdSchedulerFactory factory = new StdSchedulerFactory();
            factory.initialize(getBaseQuartzProperties());
            result = factory.getScheduler();
            result.addTriggerListener(new JobTriggerListener(getSchedulerFacade().getExecutionService(), getSchedulerFacade().getShardingService()));
        } catch (final SchedulerException ex) {
            throw new JobSystemException(ex);
        }
        return result;
    }

    private Properties getBaseQuartzProperties() {
        Properties result = new Properties();
        result.put("org.quartz.threadPool.class", org.quartz.simpl.SimpleThreadPool.class.getName());
        result.put("org.quartz.threadPool.threadCount", "1");
        result.put("org.quartz.scheduler.instanceName", getJobRootConfiguration().getTypeConfig().getCoreConfig().getJobName());
        result.put("org.quartz.jobStore.misfireThreshold", "1");
        result.put("org.quartz.plugin.shutdownhook.class", JobShutdownHookPlugin.class.getName());
        result.put("org.quartz.plugin.shutdownhook.cleanShutdown", Boolean.TRUE.toString());
        return result;
    }
}
