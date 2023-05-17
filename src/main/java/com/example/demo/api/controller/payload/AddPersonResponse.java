package com.example.demo.api.controller.payload;

import com.example.demo.api.model.Meeting;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddPersonResponse {
    private Meeting meeting;
    private String warning;
}
