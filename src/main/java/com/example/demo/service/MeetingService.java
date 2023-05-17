package com.example.demo.service;

import com.example.demo.api.model.Category;
import com.example.demo.api.model.Meeting;
import com.example.demo.api.model.Person;
import com.example.demo.api.model.Type;
import com.example.demo.repository.MeetingRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;



@Service
public class MeetingService {

    private final MeetingRepository meetingRepository;

    public MeetingService(MeetingRepository meetingRepository){
        this.meetingRepository = meetingRepository;
    }

    public List<Meeting> findAll(MeetingSearchFilters meetingSearchFilters){
        var meetings = this.meetingRepository.findAll();
        if(meetingSearchFilters.getDescription()!=null){
            meetings = filterByDescription(meetings, meetingSearchFilters.getDescription());
        }
        if(meetingSearchFilters.getType()!=null){
            meetings = filterByType(meetings, meetingSearchFilters.getType());
        }
        if(meetingSearchFilters.getCategory()!=null){
            meetings = filterByCategory(meetings, meetingSearchFilters.getCategory());
        }
        if(meetingSearchFilters.getPerson()!=null){
            meetings = filterByResponsiblePerson(meetings, meetingSearchFilters.getPerson());
        }
        if(meetingSearchFilters.getDateFrom()!=null){
            meetings = filterByStartDate(meetings, meetingSearchFilters.getDateFrom());
        }
        if(meetingSearchFilters.getDateTo()!=null){
            meetings = filterByEndDate(meetings, meetingSearchFilters.getDateTo());
        }
        if(meetingSearchFilters.getAttendees()!=null){
            meetings = filterByNumberOfAttendees(meetings, meetingSearchFilters.getAttendees());
        }
        return meetings;
    }

    public List<Meeting> filterByResponsiblePerson(List<Meeting> meetings, String responsiblePerson){
        return meetings.stream()
                .filter(meeting -> personIsResponsible(meeting, responsiblePerson))
                .toList();
    }

    public List<Meeting> filterByCategory(List<Meeting> meetings, Category category){
        return meetings.stream()
                .filter(meeting -> meeting.getCategory().equals(category))
                .toList();
    }

    public List<Meeting> filterByType(List<Meeting> meetings, Type type){
        return meetings.stream()
                .filter(meeting -> meeting.getType().equals(type))
                .toList();
    }

    public List<Meeting> filterByStartDate(List<Meeting> meetings, LocalDateTime date){
        return meetings.stream()
                .filter(meeting -> meeting.getStartDate().isAfter(date))
                .toList();
    }

    public List<Meeting> filterByEndDate(List<Meeting> meetings, LocalDateTime date){
        return meetings.stream()
                .filter(meeting -> meeting.getEndDate().isBefore(date))
                .toList();
    }

    public List<Meeting> filterByNumberOfAttendees(List<Meeting> meetings, int amount){
        return meetings.stream()
                .filter(meeting -> meeting.getAttendees().size() == amount)
                .toList();
    }

    public List<Meeting> filterByDescription(List<Meeting> meetings, String description){
        return meetings.stream()
                .filter(meeting -> StringUtils.containsAny(meeting.getDescription(), description))
                .toList();
    }

    public Meeting save(Meeting meeting) {
        return meetingRepository.save(meeting);
    }

    public Meeting addPersonToMeeting(String meetingId, Person person){
        var meeting = meetingRepository.findById(meetingId);
        var emailExists = meeting.getAttendees().stream()
                .anyMatch(p -> p.getEmail().equals(person.getEmail()));
        if(emailExists) {
            throw new RuntimeException("Person with this email already exists in this meeting");
        }

        return meetingRepository.addPersonById(meetingId, person);
    }

    public void delete(String email, String id) {
        meetingRepository.deleteByIdAndResponsiblePersonEmail(email, id);
    }

    public boolean personIsInMeeting(Person person, List<Meeting> meetings) {
        var meetingsNotEnded = meetingsNotEnded(meetings);
        var personIsInMeeting = false;
        for (Meeting meeting : meetingsNotEnded) {
            personIsInMeeting = meeting.getAttendees().stream()
                    .anyMatch(p -> p.getEmail().equals(person.getEmail()));
        }
        return personIsInMeeting;
    }

    private List<Meeting> meetingsNotEnded(List<Meeting> meetings){
        return meetings.stream()
                .filter(m -> m.getEndDate().isAfter(LocalDateTime.now()))
                .toList();
    }

    private boolean personIsResponsible(Meeting meeting, String responsiblePerson) {
        var person = meeting.getResponsiblePerson();
        return person.getLastName().equals(responsiblePerson) || person.getFirstName().equals(responsiblePerson);
    }
}
