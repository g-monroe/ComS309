package com.coms309SS6.isuview.repository;

import com.coms309SS6.isuview.models.Building;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BuildingRepositoryTest {
    @Mock
    BuildingRepository mockedBR;

    Building building = new Building("test", "testName", (float) 12.3, (float) 45.6, Collections.emptyList());

    @Test
    public void testSave() {
        when(mockedBR.save(Mockito.any(Building.class))).thenReturn(building);

        mockedBR.save(building);

        verify(mockedBR, times(1)).save(Mockito.any(Building.class));
    }

    @Test
    public void testFindByID() {
        when(mockedBR.findById("test")).thenReturn(Optional.of(building));

        Optional<Building> testBuilding = mockedBR.findById("test");

        assertTrue("intersection is not present", testBuilding.isPresent());

        assertTrue("Building name is incorrect", testBuilding.get().getBuildingName().equals("testName"));

        boolean testX = testBuilding.get().getLongitude() == (float) 12.3;
        boolean testY = testBuilding.get().getLatitude() == (float) 45.6;

        assertTrue("X is incorrect", testX);
        assertTrue("Y is incorrect", testY);
    }
}