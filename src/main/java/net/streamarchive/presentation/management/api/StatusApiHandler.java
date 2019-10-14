package net.streamarchive.presentation.management.api;

import net.streamarchive.infrastructure.RecordThreadSupervisor;
import net.streamarchive.infrastructure.models.StatusDataModel;
import net.streamarchive.repository.StatusRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * This API uses for records statuses.
 * Endpoint {@code /status_list}
 */
@RestController
@RequestMapping("/api/v1/status_list")
public class StatusApiHandler {
    private final
    StatusRepository statusRepository;
    private final
    RecordThreadSupervisor recordThreadSupervisor;

    public StatusApiHandler(StatusRepository statusRepository, RecordThreadSupervisor recordThreadSupervisor) {
        this.statusRepository = statusRepository;
        this.recordThreadSupervisor = recordThreadSupervisor;
    }

    /**
     * This method returns list of record task
     * @return list of record tasks
     */
    @GetMapping()
    public List<StatusDataModel> all() {
        return statusRepository.findAll();
    }

    /**
     * This method stops stream task.
     * @param uuid UUID of stream
     */
    @DeleteMapping("/{uuid}")
    public void stopTask(@PathVariable("uuid") String uuid) {
        recordThreadSupervisor.stop(UUID.fromString(uuid));
    }
}
