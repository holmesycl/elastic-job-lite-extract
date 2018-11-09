package io.elasticjob.lite.internal.schedule;

import lombok.extern.log4j.Log4j;

import java.util.Iterator;
import java.util.ServiceLoader;

@Log4j
public class JobScheduleControllerFactoryManager {

    private static JobScheduleControllerFactory jobScheduleControllerFactory;

    static {
        loadInitialJobScheduleControllerFactory();
    }

    public static JobScheduleControllerFactory obtain() {
        return jobScheduleControllerFactory;
    }

    private static void loadInitialJobScheduleControllerFactory() {
        ServiceLoader<JobScheduleControllerFactory> jobScheduleControllerFactories = ServiceLoader.load(JobScheduleControllerFactory.class);
        Iterator<JobScheduleControllerFactory> jobScheduleControllerFactoriesIterator = jobScheduleControllerFactories.iterator();
        try {
            while (jobScheduleControllerFactoriesIterator.hasNext()) {
                jobScheduleControllerFactory = jobScheduleControllerFactoriesIterator.next();
            }
            log.error("JobScheduleControllerFactory初始化结束。结果为：" + jobScheduleControllerFactory.getClass().getCanonicalName());
        } catch (Throwable t) {
            // Do nothing
        }
    }
}
