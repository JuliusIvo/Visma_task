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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MeetingControllerTest {


    private static final List<Meeting> meetings = List.of(new Meeting(
            "id1",
            "name",
            new Person("id", "firstname", "lastname", "email", LocalDateTime.of(2023, Month.APRIL, 20, 15, 30)),
            List.of(new Person("id2", "firstname", "lastname", "email2", LocalDateTime.of(2023, Month.APRIL, 20, 15, 30)),
                    new Person("id3", "firstname1", "lastname", "email3", LocalDateTime.of(2023, Month.APRIL, 20, 15, 30))),
            "description",
            Category.CodeMonkey,
            Type.InPerson,
            LocalDateTime.of(2023, Month.APRIL, 20, 15, 30),
            LocalDateTime.of(2023, Month.APRIL, 20, 16, 30)

    ), new Meeting(
            "id2",
            "name1",
            new Person("id", "firstname", "lastname", "email", LocalDateTime.of(2023, Month.APRIL, 20, 15, 30)),
            List.of(new Person("id3", "firstname2", "lastname", "email2", LocalDateTime.of(2023, Month.APRIL, 20, 15, 30)),
                    new Person("id4", "firstname3", "lastname", "email3", LocalDateTime.of(2023, Month.APRIL, 20, 15, 30))),
            "test",
            Category.Hub,
            Type.Live,
            LocalDateTime.of(2023, Month.APRIL, 20, 15, 30),
            LocalDateTime.of(2023, Month.APRIL, 20, 16, 30)

    ));

    private static final String description = "description";
    private static final Category category = Category.CodeMonkey;
    private static final Type type = Type.Live;
    private static final String person = "firstname";
    private static final LocalDateTime dateFrom = LocalDateTime.of(2023, Month.APRIL, 19, 16, 30);
    private static final LocalDateTime dateTo = LocalDateTime.of(2023, Month.APRIL, 15, 12, 0);
    private static final int attendees = 2;
    @Mock
    private MeetingService meetingService;

    @InjectMocks
    private MeetingController meetingController;

    @Test
    void create__success() {
        NewMeetingRequest request = new NewMeetingRequest("name",
                new Person("id", "firstname", "lastname", "email", LocalDateTime.of(2023, Month.APRIL, 20, 15, 30)),
                List.of(new Person("id2", "firstname", "lastname", "email2", LocalDateTime.of(2023, Month.APRIL, 20, 15, 30)),
                        new Person("id3", "firstname", "lastname", "email3", LocalDateTime.of(2023, Month.APRIL, 20, 15, 30))),
                "description",
                Category.Hub,
                Type.InPerson,
                LocalDateTime.of(2023, Month.APRIL, 20, 15, 30),
                LocalDateTime.of(2023, Month.APRIL, 20, 16, 30)
        );
        Meeting meeting = new Meeting();
        meeting.setName(request.getName());
        meeting.setResponsiblePerson(request.getResponsiblePerson());
        meeting.setAttendees(request.getAttendees());
        meeting.setCategory(request.getCategory());
        meeting.setType(request.getType());
        meeting.setStartDate(request.getStartDate());
        meeting.setEndDate(request.getEndDate());
        when(meetingService.save(any(Meeting.class))).thenReturn(meeting);
        Meeting newMeeting = meetingController.create(request);
        assertThat(meeting).isEqualTo(newMeeting);
        verify(meetingService, times(1)).save(any(Meeting.class));
    }

    @Test
    void add__person__to__meeting__success() {
        String meetingId = "id";
        NewPersonRequest request = new NewPersonRequest("firstname",
                "lastname",
                "email",
                LocalDateTime.of(2023, Month.APRIL, 20, 15, 30));
        Person person = new Person();
        person.setFirstName(request.getFirstName());
        person.setLastName(request.getLastName());
        person.setEmail(request.getEmail());
        person.setAddedToMeetingAt(request.getAddedToMeetingAt());
        Meeting meeting = new Meeting();
        String warningMessage = "Person has conflicting meetings";
        when(meetingService.personIsInMeeting(any(Person.class), anyList())).thenReturn(true);
        when(meetingService.addPersonToMeeting(eq(meetingId), any(Person.class))).thenReturn(meeting);
        AddPersonResponse response = meetingController.addPerson(request, meetingId);
        assertThat(meeting).isEqualTo(response.getMeeting());
        assertThat(warningMessage).isEqualTo(response.getWarning());
        verify(meetingService, times(1)).personIsInMeeting(any(Person.class), anyList());
        verify(meetingService, times(1)).addPersonToMeeting(eq(meetingId), any(Person.class));
    }

    @Test
    void delete__meeting__by__id__and__email__success() {
        String responsiblePersonEmail = "some@email.com";
        String meetingId = "id";
        meetingController.delete(responsiblePersonEmail, meetingId);
        verify(meetingService, times(1)).delete(responsiblePersonEmail, meetingId);
    }

    @Test
    void findAll__without__filters() {
        MeetingSearchFilters searchFilters = new MeetingSearchFilters();
        when(meetingService.findAll(eq(searchFilters))).thenReturn(meetings);
        List<Meeting> meetingsFound = meetingController.findAll(searchFilters.getDescription(),
                searchFilters.getCategory(),
                searchFilters.getType(),
                searchFilters.getPerson(),
                searchFilters.getDateFrom(),
                searchFilters.getDateTo(),
                searchFilters.getAttendees());
        assertThat(meetings).isEqualTo(meetingsFound);
        verify(meetingService, times(1)).findAll(eq(searchFilters));
    }

    @Test
    void findAll__with__description__filter() {
        MeetingSearchFilters searchFilters = new MeetingSearchFilters();
        searchFilters.setDescription(description);
        when(meetingService.findAll(eq(searchFilters))).thenReturn(meetings);
        List<Meeting> meetingsFound = meetingController.findAll(searchFilters.getDescription(),
                searchFilters.getCategory(),
                searchFilters.getType(),
                searchFilters.getPerson(),
                searchFilters.getDateFrom(),
                searchFilters.getDateTo(),
                searchFilters.getAttendees());
        assertThat(meetings).isEqualTo(meetingsFound);
        verify(meetingService, times(1)).findAll(eq(searchFilters));
    }

    @Test
    void findAll__with__all__filters() {
        MeetingSearchFilters searchFilters = new MeetingSearchFilters();
        searchFilters.setDescription(description);
        searchFilters.setPerson(person);
        searchFilters.setType(type);
        searchFilters.setCategory(category);
        searchFilters.setAttendees(attendees);
        searchFilters.setDateFrom(dateFrom);
        searchFilters.setDateTo(dateTo);
        when(meetingService.findAll(eq(searchFilters))).thenReturn(meetings);
        List<Meeting> meetingsFound = meetingController.findAll(searchFilters.getDescription(),
                searchFilters.getCategory(),
                searchFilters.getType(),
                searchFilters.getPerson(),
                searchFilters.getDateFrom(),
                searchFilters.getDateTo(),
                searchFilters.getAttendees());
        assertThat(meetings).isEqualTo(meetingsFound);
        verify(meetingService, times(1)).findAll(eq(searchFilters));
    }

}