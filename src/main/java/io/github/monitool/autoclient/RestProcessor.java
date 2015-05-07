package io.github.monitool.autoclient;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.github.monitool.autoclient.dto.LoginDTO;
import io.github.monitool.autoclient.dto.response.DataResponse;
import io.github.monitool.autoclient.dto.response.SensorResponse;
import io.github.monitool.autoclient.dto.response.LoginResponse;

import javax.xml.ws.http.HTTPException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Bartosz Głowacki on 2015-03-28.
 */
public class RestProcessor {

    private static RestProcessor instance;
/*
    private RestProcessor(){};

    public static RestProcessor getInstance(){
        if(instance==null){
            instance = new RestProcessor();
        }
        return instance;
    }
*/
    private ClientHttp client = new ClientHttp();
    List<LoginDTO> monitors = Lists.newArrayList();
    Map<String,LoginDTO> sensorTokens = Maps.newHashMap();

    public void login() throws IOException {
        ConfigParser parser = new ConfigParser();
        monitors = parser.parse();
        for(LoginDTO monitor:monitors) {
            String response = client.post(buildURL("Users/login",monitor), buildJSON(monitor));
            if(response.contains("error")){
                System.out.println("Invalid credentials for monitor at: " + monitor.getUrl());
                throw new HTTPException(400);
            }
            monitor.setAuthToken(parseToken(response));
        }
    }

    public List<SensorResponse> getSensors() throws IOException {
        if(monitors.isEmpty()) login();
        List<SensorResponse> sensors = Lists.newArrayList();
        sensorTokens.clear();
        for(LoginDTO monitor:monitors) {
            String response = client.get(buildURL("hosts",monitor));
            List<SensorResponse> tmpSensors = parseSensors(response);
            sensors.addAll(tmpSensors);
            for(SensorResponse sensor:tmpSensors){
                sensorTokens.put(sensor.getId(), monitor);
            }
        }
        return sensors;
    }

    public DataResponse getData(String sensorId) throws IOException  {
        if(monitors.isEmpty()) login();
        String response = client.get(buildURL("hosts/" + sensorId + "/data/",this.sensorTokens.get(sensorId)));
        List<DataResponse> data = parseData(response);
        return data.size()==0?null:data.get(0);
    }


    private String buildURL(String resource, LoginDTO loginDTO){
        StringBuilder builder = new StringBuilder();
        builder.append(loginDTO.getUrl()).append(resource).append("?");
        if(resource.contains("data")){
            builder.append("filter[order]=date%20DESC");
            builder.append("&filter[limit]=1&");
        }
        if(!resource.contains("login")){
            builder.append("&access_token=").append(loginDTO.getAuthToken());
        }
        return builder.toString();
    }

    private String buildJSON(LoginDTO loginDTO){
        Gson json = new Gson();
        return json.toJson(loginDTO, LoginDTO.class);
    }

    private String parseToken(String response){
        Gson json = new Gson();
        return json.fromJson(response,LoginResponse.class).getId();
    }
    
    private List<SensorResponse> parseSensors(String response){
        Gson json = new Gson();
        Type collectionType = new TypeToken<List<SensorResponse>>(){}.getType();
        return json.fromJson(response, collectionType);
    }

    private List<DataResponse> parseData(String response){
        Gson json = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
        Type collectionType = new TypeToken<List<DataResponse>>(){}.getType();
        return json.fromJson(response, collectionType);
    }




}
