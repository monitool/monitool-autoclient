package io.github.monitool.autoclient.quartz;

import io.github.monitool.autoclient.RestProcessor;
import io.github.monitool.autoclient.dto.response.DataResponse;
import io.github.monitool.autoclient.dto.response.SensorResponse;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bartosz Głowacki on 2015-03-28.
 */
public class ReadJob implements Job {

    RestProcessor restProcessor;

    public void execute(JobExecutionContext context) throws JobExecutionException {
        restProcessor=RestProcessor.getInstance();
        try {
            List<SensorResponse> sensors = restProcessor.getSensors();
            List<DataResponse> newestData = new ArrayList<DataResponse>();
            for(SensorResponse s:sensors){
                DataResponse data = restProcessor.getData(s.getId());
                newestData.add(data);
            }
            print(sensors,newestData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void print(List<SensorResponse> sensors, List<DataResponse> data){
        for(DataResponse d:data){
            System.out.println("Sensor id: "+d.getSensorId()+", memLoad: "+d.getMemLoad()+", cpuLoad: "+d.getCpuLoad());
        }
    }

}
