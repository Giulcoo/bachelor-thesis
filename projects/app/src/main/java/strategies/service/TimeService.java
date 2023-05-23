package strategies.service;

import java.util.HashMap;

public class TimeService {
    private HashMap<String, Long> timeMap = new HashMap<>();

    public void start(String key) {
        timeMap.put(key, System.nanoTime());
    }

    public long stop(String key) {
        long time = System.nanoTime() - timeMap.get(key);
        timeMap.remove(key);
        System.out.println("[" + key + "]: " + ((double) time / 1_000_000_000) + "s");
        return time;
    }

    //TODO: Library for time measurement (JMH)
}
