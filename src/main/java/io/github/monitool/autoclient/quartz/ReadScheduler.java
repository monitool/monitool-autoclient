package io.github.monitool.autoclient.quartz;

import io.github.monitool.autoclient.Mode;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Created by Bartosz Głowacki on 2015-03-28.
 */
public class ReadScheduler {

        public static void start(String cron, Mode mode) {
            start(cron, ReadJob.class, mode);
        }

        static void start(String cron, Class<? extends Job> jobClass, Mode mode) {
            JobDetail job = JobBuilder.newJob(jobClass).usingJobData("mode",mode.getMode()).build();
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
            try {
                Scheduler scheduler = new StdSchedulerFactory().getScheduler();
                scheduler.start();
                scheduler.scheduleJob(job, trigger);
            } catch (SchedulerException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


}
