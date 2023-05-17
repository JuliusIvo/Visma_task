package com.example.demo.api.controller;

import com.example.demo.api.controller.payload.AddPersonResponse;
import com.example.demo.api.controller.payload.NewMeetingRequest;
import com.example.demo.api.controller.payload.NewPersonRequest;
import com.example.demo.api.model.Category;
import com.example.demo.api.model.Meeting;
import com.example.demo.api.model.Person;
import com.example.demo.api.model.Type;
import com.example.demo.service.MeetingSearchFilters;
import com.example.demo.service.MeetingService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/meetings")
public class MeetingController {
    private final MeetingService meetingService;
    public MeetingController(MeetingService meetingService){
        this.meetingService = meetingService;
    }

    @PostMapping
    public Meeting create(@RequestBody NewMeetingRequest request){
        var meeting = new Meeting();
        meeting.setName(request.getName());
        meeting.setResponsiblePerson(request.getResponsiblePerson());
        meeting.setAttendees(request.getAttendees());
        meeting.setDescription(request.getDescription());
        meeting.setCategory(request.getCategory());
        meeting.setType(request.getType());
        meeting.setStartDate(request.getStartDate());
        meeting.setEndDate(request.getEndDate());
        return meetingService.save(meeting);
    }

    @PostMapping("/{meetingId}/person")
    public AddPersonResponse addPerson(@RequestBody NewPersonRequest request, @PathVariable String meetingId){
        String warningMessage = null;
        var person = new Person();
        person.setFirstName(request.getFirstName());
        person.setLastName(request.getLastName());
        person.setAddedToMeetingAt(request.getAddedToMeetingAt());
        person.setEmail(request.getEmail());
        if(meetingService.personIsInMeeting(person, meetingService.findAll(new MeetingSearchFilters()))){
            warningMessage = "Person has conflicting meetings";
        }
        var meeting = meetingService.addPersonToMeeting(meetingId, person);
        return new AddPersonResponse(meeting, warningMessage);
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader(name = "X-Responsible-person") String responsiblePersonEmail, @PathVariable String id){
        meetingService.delete(responsiblePersonEmail, id);
    }

    @GetMapping
    public List<Meeting> findAll(@RequestParam(required = false) String description,
                                 @RequestParam(required = false) Category category,
                                 @RequestParam(required = false) Type type,
                                 @RequestParam(required = false) String person,
                                 @RequestParam(required = false) LocalDateTime dateFrom,
                                 @RequestParam(required = false) LocalDateTime dateTo,
                                 @RequestParam(required = false) Integer attendees){
        return meetingService.findAll(new MeetingSearchFilters(description, category, type, person, dateFrom, dateTo, attendees));
    }
}
