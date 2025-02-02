package vn.arius.finalProject.dto;

import lombok.Data;

@Data
public class RestResponse<T> {
    private int statusCode;
    private String error;

    // Message có thể là string hoặc arrayList
    private Object message;
    private T data;


}
