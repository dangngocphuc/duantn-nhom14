package com.example.DaPhone.Model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response<T>{

    private String errorCode;
    private String errorMessage;
    private T result;
    private Long rowCount;
    private List<T> results;

    public Response(Long rowCount, List<T> results) {
        super();
        this.rowCount = rowCount;
        this.results = results;
    }
    public Response(T result) {
        super();
        this.result = result;
    }
	public Response(String errorCode, String errorMessage) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	
	
}
