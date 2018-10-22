package com.n26.infra;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        final ThreadGroup group = new ThreadGroup("Async Workers");
        return Executors.newFixedThreadPool(2,r-> {Thread t = new Thread(group,r); return t;});
    }

}
