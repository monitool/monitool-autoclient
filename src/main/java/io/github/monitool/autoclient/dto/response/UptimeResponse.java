package io.github.monitool.autoclient.dto.response;

import java.util.Date;

/**
 * Created by Bartosz Głowacki on 2015-05-07.
 */
public class UptimeResponse {
    private Date started;
    private Double uptime;

    public Date getStarted() {
        return started;
    }

    public void setStarted(Date started) {
        this.started = started;
    }

    public Double getUptime() {
        return uptime;
    }

    public void setUptime(Double uptime) {
        this.uptime = uptime;
    }
}
