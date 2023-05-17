package com.example.demo.service;

import com.example.demo.api.model.Category;
import com.example.demo.api.model.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MeetingSearchFilters {
    private String description;
    private Category category;
    private Type type;
    private String person;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private Integer attendees;
}
