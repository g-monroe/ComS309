package com.coms309SS6.isuview.repository;

import com.coms309SS6.isuview.models.Room;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RoomRepositoryTest {
    @Mock
    RoomRepository mockedRR;

    Room room;

    @Before
    public void setUp() {
        room = new Room();
        room.setId("test");
        room.setFloorId("testFloorId");
        room.setIntersectionId("testIntersectionId");
        room.setRoomNumber("1234");
    }

    @Test
    public void testSave() {
        when(mockedRR.save(Mockito.any(Room.class))).thenReturn(room);

        mockedRR.save(room);

        verify(mockedRR, times(1)).save(Mockito.any(Room.class));
    }

    @Test
    public void testFindByID() {
        when(mockedRR.findById("test")).thenReturn(Optional.of(room));

        Optional<Room> testRoom = mockedRR.findById("test");

        assertTrue("Room is not present", testRoom.isPresent());

        assertTrue("Building id is incorrect", room.getFloorId().equals("testFloorId"));
        assertTrue("Intersection id is incorrect", room.getIntersectionId().equals("testIntersectionId"));
        assertTrue("Room number incorrect", room.getRoomNumber().equals("1234"));
    }
}