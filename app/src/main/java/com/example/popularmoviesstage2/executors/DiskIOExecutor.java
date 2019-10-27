package com.example.popularmoviesstage2.executors;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DiskIOExecutor {
    private static DiskIOExecutor ourInstance;
    private Executor diskIoExecutor;

    public static DiskIOExecutor getInstance() {
        if (ourInstance == null) {
            ourInstance = new DiskIOExecutor();
        }
        return ourInstance;
    }

    private DiskIOExecutor() {
        diskIoExecutor = Executors.newSingleThreadExecutor();
    }

    public void execute(Runnable runnable) {
        diskIoExecutor.execute(runnable);
    }
}
