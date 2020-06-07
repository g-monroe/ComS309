package com.coms309SS6.isuview.repository;

import com.coms309SS6.isuview.models.Intersection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class IntersectionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private IntersectionRepository intersectionRepository;

    private Intersection intersection1;

    @Before
    public void setUp() {
        intersection1 = new Intersection("id1", 0, 1, "floorId1");
    }

    @Test
    public void whenFindById_thenSuccess() {
        entityManager.persist(intersection1);

        Optional<Intersection> intersection = intersectionRepository.findById("id1");

        assertThat(intersection.isPresent()).isEqualTo(true);
        assertThat(intersection.get().getX()).isEqualTo(0);

        intersection = intersectionRepository.findById("nonexistent");

        assertThat(intersection.isPresent()).isFalse();
    }
}
