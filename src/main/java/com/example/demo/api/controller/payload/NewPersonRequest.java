package com.example.demo.api.controller.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewPersonRequest {
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime addedToMeetingAt;
}
