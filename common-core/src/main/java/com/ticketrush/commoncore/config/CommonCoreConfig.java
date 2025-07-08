package com.ticketrush.commoncore.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.ticketrush.commoncore") // 이 패키지와 하위 패키지를 스캔하도록 설정
public class CommonCoreConfig {
}
