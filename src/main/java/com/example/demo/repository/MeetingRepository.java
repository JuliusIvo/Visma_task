package com.example.demo.repository;

import com.example.demo.api.model.Meeting;
import com.example.demo.api.model.Person;

import java.util.List;

public interface MeetingRepository {
    List<Meeting> findAll();
    Meeting save(Meeting meeting);

    Meeting findById(String id);

    Meeting addPersonById(String id, Person person);
    void deleteByIdAndResponsiblePersonEmail(String email, String id);
}
