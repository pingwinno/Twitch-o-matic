package net.streamarchive;

import net.streamarchive.infrastructure.enums.StartedBy;
import net.streamarchive.infrastructure.enums.State;
import net.streamarchive.infrastructure.models.StatusDataModel;
import net.streamarchive.repository.StatusRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
class StatusServiceTest {
    @Autowired
    StatusRepository statusService;

    StatusDataModel statusDataModel = new StatusDataModel(1111, StartedBy.VALIDATION,"kkkk", State.INITIALIZE, UUID.randomUUID(),"user" );

    @Test
    void findAll() {
        statusService.saveAndFlush(statusDataModel);
        for (StatusDataModel statusDataModel :statusService.findAll()) {
            System.out.println(statusDataModel.toString());
        }
    }
}