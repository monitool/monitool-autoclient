package io.github.monitool.autoclient.dto.response;

import java.util.Date;

/**
 * Created by Bartosz Głowacki on 2015-03-28.
 */
public class SensorResponse {

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
