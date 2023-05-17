package com.example.demo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.SneakyThrows;


public class JSONUtils {
    private static ObjectMapper objectMapper;
    @SneakyThrows
    public static String toJSON(Object object){
        return objectMapper().writeValueAsString(object);
    }

    @SneakyThrows
    public static <T> T toObject(String json, Class<T> type){
        return objectMapper().readValue(json, type);
    }

    private static ObjectMapper objectMapper(){
        if(objectMapper==null){
            var mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            mapper.findAndRegisterModules();
            objectMapper = mapper;
        }
        return objectMapper;
    }

}
