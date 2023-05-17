package com.example.demo.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Person {
    private String id = UUID.randomUUID().toString();
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime addedToMeetingAt;
}
