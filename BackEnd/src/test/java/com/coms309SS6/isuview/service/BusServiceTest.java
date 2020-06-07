package com.coms309SS6.isuview.service;

import com.coms309SS6.isuview.models.BusRoute;
import com.coms309SS6.isuview.models.BusStop;
import com.coms309SS6.isuview.models.BusStoppage;
import com.coms309SS6.isuview.repository.BusRouteRepository;
import com.coms309SS6.isuview.repository.BusStopRepository;
import com.coms309SS6.isuview.repository.BusStoppageRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Time;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class BusServiceTest {
    @Mock
    private BusRouteRepository busRouteRepository;

    @Mock
    private BusStopRepository busStopRepository;

    @Mock
    private BusStoppageRepository busStoppageRepository;

    @InjectMocks
    private BusService busService;

    private BusRoute busRoute;
    private BusStop busStop1;
    private BusStop busStop2;
    private BusStop busStop3;
    private BusStoppage busStoppage1;
    private BusStoppage busStoppage2;

    @Before
    public void setUp() {
        busRoute = new BusRoute();
        busRoute.setId(1001);
        busRoute.setName("busRoute");
        when(busRouteRepository.findById(busRoute.getId())).thenReturn(Optional.of(busRoute));

        busStop1 = new BusStop();
        busStop1.setId(2001);
        busStop1.setName("busStop1");
        when(busStopRepository.findById(busStop1.getId())).thenReturn(Optional.of(busStop1));
        busStop2 = new BusStop();
        busStop2.setId(2002);
        busStop2.setName("busStop2");
        when(busStopRepository.findById(busStop2.getId())).thenReturn(Optional.of(busStop2));
        busStop3 = new BusStop();
        busStop3.setId(2003);
        busStop3.setName("busStop3");
        when(busStopRepository.findById(busStop3.getId())).thenReturn(Optional.of(busStop3));

        busStoppage1 = new BusStoppage();
        busStoppage1.setId(3001);
        busStoppage1.setBusRouteId(busRoute.getId());
        busStoppage1.setBusStopId(busStop1.getId());
        busStoppage1.setTime(new Time(0, 0, 0));
        when(busStoppageRepository.findById(busStoppage1.getId())).thenReturn(Optional.of(busStoppage1));
        busStoppage2 = new BusStoppage();
        busStoppage2.setId(3002);
        busStoppage2.setBusRouteId(busRoute.getId());
        busStoppage2.setBusStopId(busStop2.getId());
        busStoppage2.setTime(new Time(23, 59, 59));
        when(busStoppageRepository.findById(busStoppage2.getId())).thenReturn(Optional.of(busStoppage2));

        when(busStoppageRepository.findByBusRouteIdAndBusStopIdOrderByTime(busRoute.getId(), busStop1.getId())).thenReturn(Collections.singletonList(busStoppage1));
        when(busStoppageRepository.findByBusRouteIdAndBusStopIdOrderByTime(busRoute.getId(), busStop2.getId())).thenReturn(Collections.singletonList(busStoppage2));
        when(busStoppageRepository.findByBusRouteIdAndBusStopIdOrderByTime(busRoute.getId(), busStop3.getId())).thenReturn(Collections.emptyList());
    }

    @Test
    public void testGetAllStopsForRoute() {
        when(busStoppageRepository.findByBusRouteId(1001)).thenReturn(Arrays.asList(busStoppage1, busStoppage2));
        busService.getAllStopsForRoute(1001);
        verify(busStopRepository).findAllById(new HashSet<>(Arrays.asList(2001, 2002)));

        busService.getAllStopsForRoute(1002);
        verify(busStopRepository).findAllById(Collections.emptySet());
    }

    @Test
    public void testGetTimesForRouteAndStop() {
        List<Time> foundTimes = busService.getTimesForRouteAndStop(1001, 2001);
        assertThat(foundTimes).isNotEmpty();
        assertThat(foundTimes.get(0).getHours()).isEqualTo(0);
        assertThat(foundTimes.get(0).getMinutes()).isEqualTo(0);

        foundTimes = busService.getTimesForRouteAndStop(1001, 2002);
        assertThat(foundTimes).isNotEmpty();
        assertThat(foundTimes.get(0).getHours()).isEqualTo(23);
        assertThat(foundTimes.get(0).getMinutes()).isEqualTo(59);

        foundTimes = busService.getTimesForRouteAndStop(1001, 2003);
        assertThat(foundTimes).isEmpty();
    }

    @Test
    public void testGetEarliestArrivableTime() {
        Time foundTime = busService.getEarliestArrivableTime(1001, 2001);
        assertThat(foundTime).isNull();

        foundTime = busService.getEarliestArrivableTime(1001, 2002);
        assertThat(foundTime).isNotNull();
        assertThat(foundTime.getHours()).isEqualTo(23);
        assertThat(foundTime.getMinutes()).isEqualTo(59);

        foundTime = busService.getEarliestArrivableTime(1001, 2003);
        assertThat(foundTime).isNull();
    }

    @Test
    public void testGetReadableTime() {
        assertThat(BusService.getReadableTime(new Time(13, 14, 15))).isEqualTo("1:14pm");
        assertThat(BusService.getReadableTime(new Time(0, 0, 0))).isEqualTo("12:00am");
        assertThat(BusService.getReadableTime(new Time(7, 7, 7))).isEqualTo("7:07am");
        assertThat(BusService.getReadableTime(new Time(12, 0, 15))).isEqualTo("12:00pm");
    }

    @Test
    public void testGetTimeFromString() {
        assertThat(BusService.getTimeFromString("1:14pm")).isEqualTo(new Time(13, 14, 0));
        assertThat(BusService.getTimeFromString("12:00am")).isEqualTo(new Time(0, 0, 0));
        assertThat(BusService.getTimeFromString("7:07am")).isEqualTo(new Time(7, 7, 0));
        assertThat(BusService.getTimeFromString("12:00pm")).isEqualTo(new Time(12, 0, 0));
    }
}
