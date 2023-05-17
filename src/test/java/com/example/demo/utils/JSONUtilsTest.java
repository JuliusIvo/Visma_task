package com.example.demo.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class JSONUtilsTest {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TestClass{
        private String field;
    }
    private static final String EXPECTED_JSON = "{\"field\":\"boogula\"}";

    @Test
    void toJSON__converts__object__toJson(){
        var object = new TestClass("boogula");
        assertThat(JSONUtils.toJSON(object)).isEqualTo(EXPECTED_JSON);
    }

    @Test
    void toObject__converts__JSON__toObject(){
        var object = JSONUtils.toObject(EXPECTED_JSON, TestClass.class);
        assertThat(object).isEqualTo(new TestClass("boogula"));
    }

}