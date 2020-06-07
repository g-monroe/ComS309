package com.example.isuview;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class Testing {

    @Test
    public void pullRoute() {
        // Given a mocked Context injected into the object under test...
        float[][] ans = {{0, 0}, {20, 20}, {20, 10}, {50, 50}};

        NavViewModel myObjectUnderTest = mock(NavViewModel.class);
        float[][] ret = {{0, 0}, {20, 20}, {20, 10}, {50, 50}};

        when(myObjectUnderTest.getRoute()).thenReturn(ret);
        // ...when the string is returned from the object under test...
        float[][] result = myObjectUnderTest.getRoute();

        // ...then the result should be the expected one.
        assertThat(result, is(ans));
    }

    @Test
    public void test2() {

    }
}
