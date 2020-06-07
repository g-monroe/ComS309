package com.coms309SS6.isuview.repository;

import com.coms309SS6.isuview.models.User;
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
public class UserRepositoryTest {
    @Mock
    UserRepository mockedUR;

    User user;

    @Before
    public void setUp() {
        user = new User();
        user.setId(1234);
        user.setName("testName");
    }

    @Test
    public void testSave() {
        when(mockedUR.save(Mockito.any(User.class))).thenReturn(user);

        mockedUR.save(user);

        verify(mockedUR, times(1)).save(Mockito.any(User.class));
    }

    @Test
    public void testFindByID() {
        when(mockedUR.findById("1234")).thenReturn(Optional.of(user));

        Optional<User> testUser = mockedUR.findById("1234");

        assertTrue("User is not present", testUser.isPresent());

        assertTrue("User name is incorrect", user.getName().equals("testName"));
    }
}