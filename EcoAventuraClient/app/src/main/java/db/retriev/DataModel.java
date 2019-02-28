package db.retriev;

import java.io.Serializable;

/**
 * Created by Marius on 20-Apr-17.
 */

public class DataModel  {
    private String id;
    private String time;
    private String model;
    private String code;

    public DataModel() {
    }

    public DataModel(String id, String time, String model, String code) {
        this.id = id;
        this.time = time;
        this.model = model;
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString () {
        return "[" + id + ", " + time + ", " + model + ", " + code + "]";
    }
}
