package io.elasticjob.lite.internal.schedule.impl.quartz16.controller;

import io.elasticjob.lite.exception.JobSystemException;
import io.elasticjob.lite.internal.schedule.JobScheduleController;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.quartz.*;

import java.text.ParseException;

@Data
@RequiredArgsConstructor
public class Quartz16JobScheduleController implements JobScheduleController {

    private final Scheduler scheduler;

    private final JobDetail jobDetail;

    private final String triggerIdentity;

    /**
     * 调度作业.
     *
     * @param cron CRON表达式
     */
    @Override
    public void scheduleJob(final String cron) {
        try {
            Trigger trigger = scheduler.getTrigger(triggerIdentity, null);
            if (trigger == null) {
                scheduler.scheduleJob(jobDetail, createTrigger(cron));
            }
            scheduler.start();
        } catch (final SchedulerException ex) {
            throw new JobSystemException(ex);
        }
    }

    /**
     * 重新调度作业.
     *
     * @param cron CRON表达式
     */
    @Override
    public synchronized void rescheduleJob(final String cron) {
        try {
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerIdentity, null);
            if (!scheduler.isShutdown() && null != trigger && !cron.equals(trigger.getCronExpression())) {
                scheduler.rescheduleJob(triggerIdentity, null, createTrigger(cron));
            }
        } catch (final SchedulerException ex) {
            throw new JobSystemException(ex);
        }
    }

    private CronTrigger createTrigger(final String cron) {
        return buildTrigger(cron);
    }

    private CronTrigger buildTrigger(final String cron) {
        try {
            CronTrigger cronTrigger = new CronTrigger(triggerIdentity, null, cron);
            cronTrigger.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
            cronTrigger.addTriggerListener("JobTriggerListener");
            return cronTrigger;
        } catch (ParseException e) {
            throw new JobSystemException(e);
        }
    }

    /**
     * 判断作业是否暂停.
     *
     * @return 作业是否暂停
     */
    @Override
    public synchronized boolean isPaused() {
        try {
            return !scheduler.isShutdown() && Trigger.STATE_PAUSED == scheduler.getTriggerState(triggerIdentity, null);
        } catch (final SchedulerException ex) {
            throw new JobSystemException(ex);
        }
    }

    /**
     * 暂停作业.
     */
    @Override
    public synchronized void pauseJob() {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.pauseAll();
            }
        } catch (final SchedulerException ex) {
            throw new JobSystemException(ex);
        }
    }

    /**
     * 恢复作业.
     */
    @Override
    public synchronized void resumeJob() {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.resumeAll();
            }
        } catch (final SchedulerException ex) {
            throw new JobSystemException(ex);
        }
    }

    /**
     * 立刻启动作业.
     */
    @Override
    public synchronized void triggerJob() {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.triggerJob(jobDetail.getName(), jobDetail.getGroup());
            }
        } catch (final SchedulerException ex) {
            throw new JobSystemException(ex);
        }
    }

    /**
     * 关闭调度器.
     */
    @Override
    public synchronized void shutdown() {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.shutdown();
            }
        } catch (final SchedulerException ex) {
            throw new JobSystemException(ex);
        }
    }
}
