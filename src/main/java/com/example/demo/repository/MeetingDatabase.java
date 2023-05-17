package com.example.demo.repository;

import com.example.demo.api.model.Meeting;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MeetingDatabase {
    private List<Meeting> meetings = new ArrayList<>();

}
