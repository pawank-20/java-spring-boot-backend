
package com.example.slow;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class ConcurrencyDemoController {
    // throws InterruptedException declares that the sleep can be interrupted. Java
    // requires us to handle or declare this possibility.

    /*
     * AtomicInteger provides thread-safe operations. incrementAndGet() increments
     * the value as one atomic operation, so concurrent increments won't overwrite
     * each other.
     * 
     * For a real application, though, don't use an in-memory counter for important
     * business data. It resets when the application restarts and isn't shared
     * across multiple application instances.
     */
    @GetMapping("/debug/slow")
    public Map<String, String> slowRequest() throws InterruptedException {
        String threadName = Thread.currentThread().getName();

        Thread.sleep(3000);

        return Map.of(
                "message", "Request completed",
                "thread", threadName);
    }

    private final AtomicInteger requestCount = new AtomicInteger(0);

    @GetMapping("/debug/counter")
    public Map<String, Integer> counter() {
        int count = requestCount.incrementAndGet();
        return Map.of("count", count);
    }

}
