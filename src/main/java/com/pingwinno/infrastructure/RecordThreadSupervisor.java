package com.pingwinno.infrastructure;

import java.util.HashMap;
import java.util.UUID;

public class RecordThreadSupervisor {
    private static volatile HashMap<UUID, Boolean> threadsList = new HashMap<>();

    private RecordThreadSupervisor() {
    }

    public static boolean isRunning(UUID uuid) {
        return threadsList.get(uuid);
    }

    public static void addFlag(UUID uuid) {
        threadsList.put(uuid, true);
    }

    public static void changeFlag(UUID uuid, boolean flag) {
        threadsList.replace(uuid, flag);
    }

}
