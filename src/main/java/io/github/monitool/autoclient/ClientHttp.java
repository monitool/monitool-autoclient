package io.github.monitool.autoclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.sun.javaws.exceptions.ErrorCodeResponseException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;

import javax.xml.ws.http.HTTPException;

public class ClientHttp {
    private OkHttpClient client =new OkHttpClient(); //HttpClientBuilder.create().build();
    public static final MediaType JSON = MediaType
            .parse("application/json; charset=utf-8");

    public String get(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        return response.body().string();
        /*HttpGet request = new HttpGet(url);
        request.addHeader("User-Agent", HTTP.USER_AGENT);
        HttpResponse response = client.execute(request);
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return result.toString();*/
    }

    public String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = client.newCall(request).execute();
        if(response.code()!=200){
            throw new HTTPException(response.code());
        }
        return response.body().string();
        /*HttpPost post = new HttpPost(url);
        post.setHeader("User-Agent", HTTP.USER_AGENT);
        post.addHeader("content-type", "application/json");
        post.setEntity(new StringEntity(json));
        HttpResponse response = client.execute(post);
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return result.toString();*/
    }
}
