package io.github.monitool.autoclient.quartz;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import io.github.monitool.autoclient.Mode;
import io.github.monitool.autoclient.Printer;
import io.github.monitool.autoclient.RestProcessor;
import io.github.monitool.autoclient.dto.response.DataResponse;
import io.github.monitool.autoclient.dto.response.SensorResponse;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.xml.ws.http.HTTPException;
import java.io.IOException;
import java.util.*;

/**
 * Created by Bartosz Głowacki on 2015-03-28.
 */
public class ReadJob implements Job {

    RestProcessor restProcessor;

    public void execute(JobExecutionContext context) throws JobExecutionException {
        restProcessor=new RestProcessor();
        try {
            List<SensorResponse> sensors = restProcessor.getSensors();
            List<DataResponse> newestData = new ArrayList<>();
            for(SensorResponse s:sensors) {
                DataResponse data = restProcessor.getData(s.getId());
                if(data!=null) newestData.add(data);
            }
            Mode mode = Mode.fromString((String) context.getJobDetail().getJobDataMap().get("mode"));
            Printer.clear();
            Printer.print(sortAndFilterData(sensors, newestData, mode));
        } catch (IOException|HTTPException e) {
            System.exit(1);
        }
    }

    private Map<DataResponse,SensorResponse> sortAndFilterData(List<SensorResponse> sensors,List<DataResponse> data, Mode mode){
        Comparator comparator;
        switch(mode){
            case CPU:
                comparator = new CpuComparator();
                break;
            case MEM:
                comparator = new MemComparator();
                break;
            case HDD:
                comparator = new DiscComparator();
                break;
            default:
                comparator = new MemComparator();
        }
        Map<DataResponse,SensorResponse> result = new TreeMap<DataResponse,SensorResponse>(comparator);
        data.sort(comparator);
        for(final DataResponse d:data){
            SensorResponse sensor = FluentIterable.from(sensors).firstMatch(new Predicate<SensorResponse>() {
                @Override
                public boolean apply(SensorResponse sensorResponse) {
                    return sensorResponse.getId().equals(d.getHostId());
                }
            }).orNull();
            if(sensor!=null) {
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

    class MemComparator implements Comparator<DataResponse>{
        @Override
        public int compare(DataResponse o1, DataResponse o2) {
            if(o1.getMemLoad()>(o2.getMemLoad())) return -1;
            else if(o1.getMemLoad()<(o2.getMemLoad())) return 1;
            else return 0;
        }
    }

    class DiscComparator implements Comparator<DataResponse>{
        @Override
        public int compare(DataResponse o1, DataResponse o2) {
            if(o1.getDiscLoad()>(o2.getDiscLoad())) return -1;
            else if(o1.getDiscLoad()<(o2.getDiscLoad())) return 1;
            else return 0;
        }
    }


}
