package io.github.monitool.autoclient.quartz;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import io.github.monitool.autoclient.Printer;
import io.github.monitool.autoclient.RestProcessor;
import io.github.monitool.autoclient.dto.response.DataResponse;
import io.github.monitool.autoclient.dto.response.SensorResponse;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;
import java.util.*;

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
            for(SensorResponse s:sensors) {
                DataResponse data = restProcessor.getData(s.getId());
                newestData.add(data);
            }
            if(context.getPreviousFireTime()!=null){
                Printer.clear();
            }
            Printer.print(sortAndFilterData(sensors, newestData));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<DataResponse,SensorResponse> sortAndFilterData(List<SensorResponse> sensors,List<DataResponse> data){
        Map<DataResponse,SensorResponse> result = new TreeMap<DataResponse,SensorResponse>(new CpuComparator());
        data.sort(new CpuComparator());//TODO wybor sortowania przez flage
        for(final DataResponse d:data){
            SensorResponse sensor = FluentIterable.from(sensors).firstMatch(new Predicate<SensorResponse>() {
                @Override
                public boolean apply(SensorResponse sensorResponse) {
                    return sensorResponse.getId().equals(d.getSensorId());
                }
            }).orNull();
            if(sensor!=null) {
                //TODO filtrowanie nieaktywnych sensorow - jak beda aktualne odczyty
                result.put(d,sensor);
            }
            if(result.size()==10){
                break;
            }
        }
        return result;
    }

    class CpuComparator implements Comparator<DataResponse>{
        @Override
        public int compare(DataResponse o1, DataResponse o2) {
            if(o1.getCpuLoad()>(o2.getCpuLoad())) return -1;
            else if(o1.getCpuLoad()<(o2.getCpuLoad())) return 1;
            else return 0;
        }
    }


}
