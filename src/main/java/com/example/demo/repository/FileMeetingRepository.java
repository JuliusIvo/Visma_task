package com.example.demo.repository;

import com.example.demo.api.model.Meeting;
import com.example.demo.api.model.Person;
import com.example.demo.utils.JSONUtils;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Repository
public class FileMeetingRepository implements MeetingRepository{
    @Override
    public List<Meeting> findAll() {
        return readFromFile().getMeetings();
    }

    @Override
    public Meeting save(Meeting meeting) {
        var database = readFromFile();
        database.getMeetings().add(meeting);
        saveToFile(database);
        return meeting;
    }

    @Override
    public Meeting findById(String id) {
        return readFromFile().getMeetings().stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElseThrow();
    }

    @Override
    public Meeting addPersonById(String id, Person person) {
        var meetingDatabase = readFromFile();
        var meeting = findById(id);
        meetingDatabase.getMeetings().remove(meeting);
        meeting.getAttendees().add(person);
        meetingDatabase.getMeetings().add(meeting);
        saveToFile(meetingDatabase);
        return meeting;
    }


    @Override
    public void deleteByIdAndResponsiblePersonEmail(String email, String id) {
        var database = readFromFile();
        var meeting = database.getMeetings().stream()
                .filter(m -> m.getResponsiblePerson().getEmail().equals(email))
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElseThrow();
        database.getMeetings().remove(meeting);
        saveToFile(database);
    }

    @SneakyThrows
    private MeetingDatabase readFromFile(){
        try {
            var json = new String(Files.readAllBytes(Paths.get("MeetingData.json")));
            return JSONUtils.toObject(json, MeetingDatabase.class);
        } catch (Exception e) {
            return new MeetingDatabase();
        }
    }
    @SneakyThrows
    private void saveToFile(MeetingDatabase meetingDatabase){
        var json = JSONUtils.toJSON(meetingDatabase);
        FileWriter writer = new FileWriter("MeetingData.json");
        writer.write(json);
        writer.close();
    }
}
