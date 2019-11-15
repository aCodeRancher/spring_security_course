package io.baselogic.springsecurity.dao;

import io.baselogic.springsecurity.domain.Event;
import io.baselogic.springsecurity.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

// Junit 5: -----------------------------------------------------------------//

// Assert-J
// --> assertThat(result.size()).isGreaterThan(0);
// http://joel-costigliola.github.io/assertj/index.html


@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JdbcEventDaoTests {

    private static final Logger log = LoggerFactory.getLogger(JdbcEventDaoTests.class);

    @Autowired
    private EventDao eventDao;

    private User owner = new User();
    private User attendee = new User();

    @BeforeEach
    public void beforeEachTest() {
        owner.setId(1);
        attendee.setId(0);
    }


    @Test
    public void initJdbcOperations() {
        assertThat(eventDao).isNotNull();
    }

    @Test
    public void find() {
        Event event = eventDao.findById(100);
        log.info(event.toString());

        assertThat(event).isNotNull();
        assertThat(event.equals(event)).isTrue();
        assertThat(event.equals(new Object())).isFalse();
        assertThat(event.equals(new Event())).isFalse();
        assertThat(event.hashCode()).isNotEqualTo(0);

        assertThat(event.getSummary()).isEqualTo("Birthday Party");
        assertThat(event.getOwner().getId()).isEqualTo(0);
        assertThat(event.getAttendee().getId()).isEqualTo(1);
    }


    @Test
    public void createEvent() {
        log.debug("******************************");
        List<Event> events = eventDao.findByUser(owner.getId());
        assertThat(events.size()).isEqualTo(2);

        Event event = TestUtils.createMockEvent(owner, attendee, "Testing Event");
        int eventId = eventDao.save(event);

        List<Event> newEvents = eventDao.findByUser(owner.getId());
        assertThat(newEvents.size()).isEqualTo(3);
        // find eventId in List...
//        assertThat(newEvents.get(3)).isEqualTo(3);
    }

    @Test
    public void createEvent_null_event() {
        assertThrows(ConstraintViolationException.class, () -> {
            eventDao.save(null);
        });
    }

    @Test
    public void createEvent_with_event_id() {
        assertThrows(IllegalArgumentException.class, () -> {
            Event event = TestUtils.createMockEvent(owner, attendee, "Testing Event");
            event.setId(12345);
            eventDao.save(event);
        });

    }

    @Test
    public void createEvent_null_event_owner() {
        assertThrows(ConstraintViolationException.class, () -> {
            Event event = TestUtils.createMockEvent(owner, attendee, "Testing Event");
            event.setOwner(null);
            eventDao.save(event);
        });

    }

    @Test
    public void createEvent_null_event_attendee() {
        assertThrows(ConstraintViolationException.class, () -> {
            Event event = TestUtils.createMockEvent(owner, attendee, "Testing Event");
            event.setAttendee(null);
            eventDao.save(event);
        });

    }

    @Test
    public void createEvent_null_event_when() {
        assertThrows(ConstraintViolationException.class, () -> {
            Event event = TestUtils.createMockEvent(owner, attendee, "Testing Event");
            event.setWhen(null);
            eventDao.save(event);
        });

    }


    @Test
    public void findAll() {
        List<Event> events = eventDao.findAll();
        assertThat(events.size()).isGreaterThanOrEqualTo(3);
    }

} // The End...