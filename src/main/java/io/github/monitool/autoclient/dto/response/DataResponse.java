package io.github.monitool.autoclient.dto.response;

import java.util.Date;

/**
 * Created by Bartosz Głowacki on 2015-03-28.
 */
public class DataResponse {

    private String hostId;
    private Date date;
    private double cpuLoad;
    private double memLoad;
    private double discLoad;

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String sensorId) {
        this.hostId = sensorId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getCpuLoad() {
        return cpuLoad;
    }

    public void setCpuLoad(double cpuLoad) {
        this.cpuLoad = cpuLoad;
    }

    public double getMemLoad() {
        return memLoad;
    }

    public void setMemLoad(double memLoad) {
        this.memLoad = memLoad;
    }

    public double getDiscLoad() {
        return discLoad;
    }

    public void setDiscLoad(double discLoad) {
        this.discLoad = discLoad;
    }

}
