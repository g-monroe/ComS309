package com.coms309SS6.isuview.controllers;

import com.coms309SS6.isuview.models.Intersection;
import com.coms309SS6.isuview.models.Room;
import com.coms309SS6.isuview.repository.IntersectionRepository;
import com.coms309SS6.isuview.repository.RoomRepository;
import com.coms309SS6.isuview.service.RouteService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RouteControllerTest {
    @InjectMocks
    RouteController routeController;

    @Mock
    IntersectionRepository intersectionRepository;

    @Mock
    RoomRepository roomRepository;

    @Mock
    RouteService routeService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testHandleRouteData() {
        Room startRoom = new Room();
        Room endRoom = new Room();

        startRoom.setId("testId1");
        startRoom.setFloorId("startFloor");
        startRoom.setIntersectionId("testIntersection1");
        startRoom.setRoomNumber("123");

        endRoom.setId("testId2");
        endRoom.setFloorId("endFloor");
        endRoom.setIntersectionId("testIntersection2");
        endRoom.setRoomNumber("456");

        Intersection intersection1 = new Intersection("testIntersection1", 1, 1, "testFloorId");
        Intersection intersection2 = new Intersection("testIntersection2", 2, 2, "testFloorId");
        Intersection intersection3 = new Intersection("testIntersection3", 3, 3, "testFloorId");

        List<Intersection> intersectionList = new ArrayList<>();
        intersectionList.add(intersection1);
        intersectionList.add(intersection2);
        intersectionList.add(intersection3);

        assertTrue(mockingDetails(roomRepository).isMock());
        assertTrue(mockingDetails(intersectionRepository).isMock());

        // when(routingHandler.handleRouteData(anyInt(), anyInt())).thenReturn(intersectionList);
        when(roomRepository.findByRoomNumber(startRoom.getRoomNumber())).thenReturn(Collections.singletonList(startRoom));
        when(roomRepository.findByRoomNumber(endRoom.getRoomNumber())).thenReturn(Collections.singletonList(endRoom));
        // when(roomRepository.findByRoomNumber(anyInt())).thenReturn(endRoomList);
        when(intersectionRepository.findById(startRoom.getIntersectionId())).thenReturn(Optional.of(intersection1));
        when(intersectionRepository.findById(endRoom.getIntersectionId())).thenReturn(Optional.of(intersection3));
        // when(intersectionRepository.findById(anyString())).thenReturn(Optional.of(intersection3));

        when(routeService.depthFirstSearch(eq(intersection1), eq(intersection3), isNull(), anyInt(), any(HashSet.class))).thenReturn(intersectionList);

        List<Intersection> testIntersections = routeController.handleRouteData(123, 456);

        assertTrue("returned list is null", !testIntersections.isEmpty());
        assertEquals(intersectionList.get(0), testIntersections.get(0));
        assertEquals(intersectionList.get(1), testIntersections.get(1));
        assertEquals(intersectionList.get(2), testIntersections.get(2));
    }
}