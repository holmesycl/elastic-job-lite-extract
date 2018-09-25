package io.elasticjob.lite.internal.schedule;

import io.elasticjob.lite.config.JobRootConfiguration;
import io.elasticjob.lite.exception.JobConfigurationException;
import io.elasticjob.lite.executor.JobFacade;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class JobScheduleControllerFactoryBuilder {

    private static final String PROVIDER_FILE = "META-INF/io.elasticjob.lite.internal.schedule.JobScheduleControllerFactory";
    private static String PROVIDER_CLAZZ;

    public static JobScheduleControllerFactory build(SchedulerFacade schedulerFacade, JobFacade jobFacade, JobRootConfiguration jobRootConfiguration) {
        JobScheduleControllerFactory jobScheduleControllerFactory = null;
        try {
            jobScheduleControllerFactory = findJobScheduleControllerFactory();
        } catch (Exception e) {
            log.error("加载任务调度控制器工厂出错了，错误信息：", e);
            throw new JobConfigurationException("未提供任务调度控制器工厂实现.");
        }
        jobScheduleControllerFactory.setSchedulerFacade(schedulerFacade);
        jobScheduleControllerFactory.setJobFacade(jobFacade);
        jobScheduleControllerFactory.setJobRootConfiguration(jobRootConfiguration);
        return jobScheduleControllerFactory;
    }

    private static JobScheduleControllerFactory findJobScheduleControllerFactory() throws Exception {
        if (PROVIDER_CLAZZ == null) {
            synchronized (JobScheduleControllerFactoryBuilder.class) {
                if (PROVIDER_CLAZZ == null) {
                    BufferedReader br = null;
                    try {
                        br = new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(PROVIDER_FILE), "utf-8"));
                        PROVIDER_CLAZZ = br.readLine().trim();
                    } finally {
                        if (br != null) {
                            try {
                                br.close();
                            } catch (IOException e) {
                                //
                            }
                        }
                    }
                }
            }
        }
        return (JobScheduleControllerFactory) Class.forName(PROVIDER_CLAZZ).newInstance();
    }

}
