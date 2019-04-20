package com.pingwinno.infrastructure;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class RecordThreadSupervisor {
    private static volatile Map<UUID, RecordThread> threadsList = new ConcurrentHashMap<>();

    private RecordThreadSupervisor() {
    }


    public synchronized static void add(UUID uuid, RecordThread recordThread) {
        threadsList.put(uuid, recordThread);
    }

    public synchronized static void stop(UUID uuid) {
        threadsList.get(uuid).stop();
        threadsList.remove(uuid);
    }

}
