package com.example.demo.service;

import com.example.demo.api.model.Category;
import com.example.demo.api.model.Meeting;
import com.example.demo.api.model.Person;
import com.example.demo.api.model.Type;
import com.example.demo.repository.MeetingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MeetingServiceTest {
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
            new Person("id", "notFirstname", "notLastname", "email", LocalDateTime.of(2023, Month.APRIL, 20, 15, 30)),
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
    private MeetingRepository meetingRepository;

    @InjectMocks
    private MeetingService meetingService;

    @Test
    void findAll__with__all__filters() {
        MeetingSearchFilters filters = new MeetingSearchFilters();
        filters.setDescription(description);
        filters.setCategory(category);
        filters.setType(type);
        filters.setPerson(person);
        filters.setDateFrom(dateFrom);
        filters.setDateTo(dateTo);
        filters.setAttendees(attendees);
        when(meetingRepository.findAll()).thenReturn(meetings);
        List<Meeting> foundMeetings = meetingService.findAll(filters);
        assertThat(new ArrayList<>()).isEqualTo(foundMeetings);
        verify(meetingRepository, times(1)).findAll();
    }

    @Test
    void findAll__with__no__filters() {
        MeetingSearchFilters filters = new MeetingSearchFilters();
        when(meetingRepository.findAll()).thenReturn(meetings);
        List<Meeting> foundMeetings = meetingService.findAll(filters);
        assertThat(meetings).isEqualTo(foundMeetings);
        verify(meetingRepository, times(1)).findAll();
    }

    @Test
    void filter__by__responsible__person() {
        List<Meeting> filteredMeetings = meetingService.filterByResponsiblePerson(meetings, person);
        assertThat(filteredMeetings.size()).isEqualTo(1);
        assertThat(meetings.get(0)).isEqualTo(filteredMeetings.get(0));
    }

    @Test
    void filter__by__category() {
        List<Meeting> filteredMeetings = meetingService.filterByCategory(meetings, category);
        assertThat(filteredMeetings.size()).isEqualTo(1);
        assertThat(meetings.get(0)).isEqualTo(filteredMeetings.get(0));
    }

    @Test
    void filter__by__type() {
        List<Meeting> filteredMeetings = meetingService.filterByType(meetings, type);
        assertThat(filteredMeetings.size()).isEqualTo(1);
        assertThat(meetings.get(1)).isEqualTo(filteredMeetings.get(0));
    }

    @Test
    void filter__by__startDate() {
        List<Meeting> filteredMeetings = meetingService.filterByStartDate(meetings, dateFrom);
        assertThat(filteredMeetings.size()).isEqualTo(2);
        assertThat(meetings).isEqualTo(filteredMeetings);
    }

    @Test
    void filter__by__endDate() {
        List<Meeting> filteredMeetings = meetingService.filterByEndDate(meetings, dateTo);
        assertThat(filteredMeetings.size()).isEqualTo(0);
    }

    @Test
    void filter__by__number__of__attendees() {
        List<Meeting> filteredMeetings = meetingService.filterByNumberOfAttendees(meetings, attendees);
        assertThat(filteredMeetings.size()).isEqualTo(2);
        assertThat(meetings.get(0)).isEqualTo(filteredMeetings.get(0));
    }

    @Test
    void filter__by__description() {
        List<Meeting> filteredMeetings = meetingService.filterByDescription(meetings, description);
        assertThat(filteredMeetings.size()).isEqualTo(2);
        assertThat(meetings).isEqualTo(filteredMeetings);
    }

    @Test
    void save__meeting__success(){
        Meeting newMeeting = new Meeting(
                "id3",
                "name2",
                new Person("id", "notFirstname", "notLastname", "email", LocalDateTime.of(2023, Month.APRIL, 20, 15, 30)),
                List.of(new Person("id5", "firstname2", "lastname", "email2", LocalDateTime.of(2023, Month.APRIL, 20, 15, 30)),
                        new Person("id6", "firstname3", "lastname", "email3", LocalDateTime.of(2023, Month.APRIL, 20, 15, 30))),
                "test",
                Category.Hub,
                Type.Live,
                LocalDateTime.of(2023, Month.APRIL, 20, 15, 30),
                LocalDateTime.of(2023, Month.APRIL, 20, 16, 30)

        );
        when(meetingRepository.save(any(Meeting.class))).thenReturn(newMeeting);
        Meeting evenNewerMeeting = meetingRepository.save(newMeeting);
        assertThat(newMeeting).isEqualTo(evenNewerMeeting);
        verify(meetingRepository, times(1)).save(any(Meeting.class));
    }

    @Test
    void add__person__to__meeting__success(){
        String meetingId = "id1";
        Meeting meeting = new Meeting();
        Person attendee = new Person("id73", "firstname", "lastname", "emailatron",
                LocalDateTime.of(2023, Month.APRIL, 20, 15, 30));
        when(meetingRepository.findById(eq(meetingId))).thenReturn(meeting);
        when(meetingRepository.addPersonById(eq(meetingId), any(Person.class))).thenReturn(meeting);
        Meeting newMeeting = meetingService.addPersonToMeeting(meetingId, attendee);
        assertThat(meeting).isEqualTo(newMeeting);
        verify(meetingRepository, times(1)).addPersonById(eq(meetingId), any(Person.class));
    }

    @Test
    void delete__meeting__success(){
        doAnswer(invocation ->
        {
            String email = invocation.getArgument(0);
            String id = invocation.getArgument(1);
            assertThat(email).isEqualTo("email", email);
            assertThat(id).isEqualTo("id", id);
            return null;
        }).when(meetingRepository).deleteByIdAndResponsiblePersonEmail(any(String.class), any(String.class));
        meetingRepository.deleteByIdAndResponsiblePersonEmail("email", "id");
    }

}




