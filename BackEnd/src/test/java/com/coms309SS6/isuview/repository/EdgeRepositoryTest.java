package com.coms309SS6.isuview.repository;

import com.coms309SS6.isuview.models.Edge;
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
public class EdgeRepositoryTest {
    @Mock
    EdgeRepository mockedER;

    Edge edge;

    @Before
    public void setUp() {
        edge = new Edge();
        edge.setId("test");
        edge.setIntersection1Id("testIntersection1Id");
        edge.setIntersection2Id("testIntersection2Id");
    }

    @Test
    public void testSave() {
        when(mockedER.save(Mockito.any(Edge.class))).thenReturn(edge);

        mockedER.save(edge);

        verify(mockedER, times(1)).save(Mockito.any(Edge.class));
    }

    @Test
    public void testFindByID() {
        when(mockedER.findById("test")).thenReturn(Optional.of(edge));

        Optional<Edge> testEdge = mockedER.findById("test");

        assertTrue("edge is not present", testEdge.isPresent());

        assertTrue("edge intersection 1 is incorrect", testEdge.get().getIntersection1Id().equals("testIntersection1Id"));
        assertTrue("edge intersection 2 is incorrect", testEdge.get().getIntersection2Id().equals("testIntersection2Id"));
    }
}