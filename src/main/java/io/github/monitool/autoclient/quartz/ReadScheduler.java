package io.github.monitool.autoclient.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Created by Bartosz Głowacki on 2015-03-28.
 */
public class ReadScheduler {

        public static void start(String cron) {
            start(cron, ReadJob.class);
        }

        static void start(String cron, Class<? extends Job> jobClass) {
            JobDetail job = JobBuilder.newJob(jobClass).build();
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
