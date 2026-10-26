
package com.example.slow;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConcurrencyDemoController {
    // throws InterruptedException declares that the sleep can be interrupted. Java
    // requires us to handle or declare this possibility.

    @GetMapping("/debug/slow")
    public Map<String, String> slowRequest() throws InterruptedException {
        String threadName = Thread.currentThread().getName();

        Thread.sleep(3000);

        return Map.of(
                "message", "Request completed",
                "thread", threadName);
    }

    private int requestCount = 0;

    /*
    With ordinary sequential requests, you should see 1, 2, 3, and so on. The problem appears when requests execute concurrently: requestCount++ is not one indivisible operation. Two threads can read the same old value, increment it independently, and overwrite one another's updates.
    */
    @GetMapping("/debug/counter")
    public Map<String, Integer> counter() {
        requestCount++;
        return Map.of("count", requestCount);
    }

    

}
