package io.github.monitool.autoclient;

import com.google.common.base.Strings;
import io.github.monitool.autoclient.dto.response.DataResponse;
import io.github.monitool.autoclient.dto.response.SensorResponse;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by Bartosz Głowacki on 2015-03-29.
 */
public class Printer {

    public static void print(Map<DataResponse,SensorResponse> data){
        final String HEADER="Sensor Name\t\t\t\tMemory Load\tCPU Load\tDisc Load";
        NumberFormat format=new DecimalFormat();
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
        StringBuilder sb;
        System.out.println(HEADER);
        for(Map.Entry<DataResponse,SensorResponse> entry:data.entrySet()){
            sb = new StringBuilder();
            String name = Strings.padEnd(entry.getValue().getName(),35,' ');
            sb.append(name.substring(0,35));
            sb.append("\t");
            sb.append(format.format(entry.getKey().getCpuLoad()));
            sb.append("\t\t");
            sb.append(format.format(entry.getKey().getMemLoad()));
            sb.append("\t\t");
            sb.append(format.format(entry.getKey().getDiscLoad()));
            System.out.println(sb.toString());
        }
    }

    public static void clear(){
        for(int i=0;i<128;++i){
            System.out.print("\b");
        }
    }



}
