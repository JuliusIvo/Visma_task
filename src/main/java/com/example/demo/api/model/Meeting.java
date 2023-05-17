package com.example.demo.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Meeting {

    private String id = UUID.randomUUID().toString();
    private String name;
    private Person responsiblePerson;
    private List<Person> attendees = new ArrayList<>();
    private String description;
    private Category category;
    private Type type;
    private LocalDateTime startDate;
    private LocalDateTime EndDate;

}
