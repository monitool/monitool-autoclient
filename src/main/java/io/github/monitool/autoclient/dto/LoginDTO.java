package io.github.monitool.autoclient.dto;

/**
 * Created by Bartosz Głowacki on 2015-03-28.
 */
public class LoginDTO {
    private String url;
    private String email;
    private String password;
    private String authToken;

    public LoginDTO(){}

    public LoginDTO(String url, String email,String password){
        this.url=url;
        this.email=email;
        this.password=password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
