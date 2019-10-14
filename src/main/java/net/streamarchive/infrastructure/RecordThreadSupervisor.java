package net.streamarchive.infrastructure;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
@Service
public class RecordThreadSupervisor {
    private static volatile Map<UUID, RecordThread> threadsList = new ConcurrentHashMap<>();

    private RecordThreadSupervisor() {
    }

    public synchronized void add(UUID uuid, RecordThread recordThread) {
        threadsList.put(uuid, recordThread);
    }

    public synchronized void stop(UUID uuid) {
        threadsList.get(uuid).stop();
        threadsList.remove(uuid);
    }
}
