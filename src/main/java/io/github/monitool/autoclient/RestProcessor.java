package io.github.monitool.autoclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.github.monitool.autoclient.dto.LoginDTO;
import io.github.monitool.autoclient.dto.response.DataResponse;
import io.github.monitool.autoclient.dto.response.SensorResponse;
import io.github.monitool.autoclient.dto.response.LoginResponse;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Bartosz Głowacki on 2015-03-28.
 */
public class RestProcessor {

    private static String AUTH_TOKEN;
    private static final String API_URL = "http://monitool.herokuapp.com:80/api/";

    private static final String EMAIL = "autoclient@monitool.com";
    private static final String PASSWORD = "autoclient";

    private static RestProcessor instance;

    private RestProcessor(){};

    public static RestProcessor getInstance(){
        if(instance==null){
            instance = new RestProcessor();
        }
        return instance;
    }

    private ClientHttp client = new ClientHttp();

    public void login() throws IOException {
        LoginDTO loginDTO = new LoginDTO(EMAIL,PASSWORD);
        String response = client.post(buildURL("Users/login"),buildJSON(loginDTO));
        AUTH_TOKEN = parseToken(response);
    }

    public List<SensorResponse> getSensors() throws IOException {
        String response = client.get(buildURL("sensors"));
        return parseSensors(response);
    }

    public DataResponse getData(String sensorId) throws IOException  {
        String response = client.get(buildDataURL("data", sensorId));
        return parseData(response).get(0);
    }

    private String buildURL(String resource) {
        return buildDataURL(resource,null);
    }

    private String buildDataURL(String resource,String sensorId){
        StringBuilder builder = new StringBuilder();
        builder.append(API_URL).append(resource).append("?");
        if(sensorId!=null){
            builder.append("filter[where][sensorId]=").append(sensorId);
            builder.append("&filter[order]=date%20DESC");
            builder.append("&filter[limit]=1&");
        }
        if(AUTH_TOKEN!=null){
            builder.append("&access_token=").append(AUTH_TOKEN);
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
