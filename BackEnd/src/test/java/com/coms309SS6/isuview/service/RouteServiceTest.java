package com.coms309SS6.isuview.service;

import com.coms309SS6.isuview.models.Edge;
import com.coms309SS6.isuview.models.Intersection;
import com.coms309SS6.isuview.repository.EdgeRepository;
import com.coms309SS6.isuview.repository.IntersectionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class RouteServiceTest {
    @Mock
    private IntersectionRepository intersectionRepository;

    @Mock
    private EdgeRepository edgeRepository;

    @InjectMocks
    private RouteService routeService;

    private Intersection intersection1;
    private Intersection intersection2;
    private Intersection intersection3;
    private Intersection intersection4;
    private Intersection intersection5;

    @Before
    public void setUp() {
        intersection1 = new Intersection("intersection1", 0, 0, "floorId");
        intersection2 = new Intersection("intersection2", 0, 1, "floorId");
        intersection3 = new Intersection("intersection3", 0, 2, "floorId");
        intersection4 = new Intersection("intersection4", 0, 3, "floorId");
        intersection5 = new Intersection("intersection5", 0, 4, "floorId2");
        when(intersectionRepository.findByFloorId("floorId")).thenReturn(Arrays.asList(intersection1, intersection2, intersection3, intersection4));
        when(intersectionRepository.findByFloorId("floorId2")).thenReturn(Collections.singletonList(intersection5));
    }

    @Test
    public void testGetLowestIntersection() {
        assertThat(routeService.getLowestIntersection("floorId")).isEqualTo(intersection4);
        assertThat(routeService.getLowestIntersection("floorId2")).isEqualTo(intersection5);
        assertThat(routeService.getLowestIntersection("floorId3")).isEqualTo(null);
    }

    @Test
    public void testGetPathFromGraph() {
        Map<Intersection, Intersection> prevMap = new HashMap<>();
        prevMap.put(intersection1, intersection2);
        prevMap.put(intersection2, intersection3);
        prevMap.put(intersection3, intersection4);

        assertThat(RouteService.getPathFromGraph(prevMap, intersection4, intersection1)).isEqualTo(Arrays.asList(intersection1, intersection2, intersection3, intersection4));
        assertThat(RouteService.getPathFromGraph(prevMap, intersection4, intersection2)).isEqualTo(Arrays.asList(intersection2, intersection3, intersection4));
        assertThat(RouteService.getPathFromGraph(prevMap, intersection1, intersection4)).isEqualTo(null);
        assertThat(RouteService.getPathFromGraph(prevMap, intersection5, intersection1)).isEqualTo(null);
    }

    @Test
    public void testFloorDjikstra() {
        Edge edge1 = new Edge();
        edge1.setIntersection2Id(intersection2.getId());
        when(edgeRepository.findByIntersection1Id(intersection1.getId())).thenReturn(Collections.singletonList(edge1));
        when(intersectionRepository.findById(intersection2.getId())).thenReturn(Optional.of(intersection2));

        Map<Intersection, Intersection> prevMap = routeService.floorDjikstra(intersection1);
        assertThat(prevMap.size()).isEqualTo(1);
        assertThat(prevMap.get(intersection2)).isEqualTo(intersection1);
    }
}
