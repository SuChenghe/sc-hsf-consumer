package com.aliware.edas.config;

import com.alibaba.boot.hsf.annotation.HSFConsumer;
import com.aliware.edas.service.ProvideTimeAsyncService;
import com.aliware.edas.service.ProvideTimeService;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HsfConfig {

    @HSFConsumer(serviceGroup = "SCH",clientTimeout = 3000, serviceVersion = "1.0.0")
    private ProvideTimeService provideTimeService;


    @HSFConsumer(serviceGroup = "SCH",clientTimeout = 3000, serviceVersion = "1.0.0", futureMethods = "getTimeFuture")
    private ProvideTimeAsyncService provideTimeAsyncService;
}
