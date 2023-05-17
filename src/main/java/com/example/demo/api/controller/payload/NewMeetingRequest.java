package com.example.demo.api.controller.payload;

import com.example.demo.api.model.Category;
import com.example.demo.api.model.Person;
import com.example.demo.api.model.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NewMeetingRequest {
    private String name;
    private Person responsiblePerson;
    private List<Person> attendees = new ArrayList<>();
    private String description;
    private Category category;
    private Type type;
    private LocalDateTime startDate;
    private LocalDateTime EndDate;

}
