package com.sparta.foodtruck.global.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;


@Getter
@Setter
public class CustomStatusAndMessageResponseDto extends CustomStatusResponseDto{

    @JsonInclude(JsonInclude.Include.NON_NULL)
    Map<String, String> msgList;

    public CustomStatusAndMessageResponseDto(boolean status) {
        super(status);
        msgList = new LinkedHashMap<>();
    }
    public void addMessage(String field, String msg){
        this.msgList.put(field,msg);
    }
}
