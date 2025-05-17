package com.crepestrips.userservice;

import com.crepestrips.userservice.repository.*;
import com.crepestrips.userservice.service.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableCaching
@EnableFeignClients
public class UserServiceApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(UserServiceApplication.class, args);

            UserService userService = new UserService(
                context.getBean(UserRepository.class),
                context.getBean(ReportRepository.class),
                context.getBean(PasswordEncoder.class),
                context.getBean(CartRepository.class),
                context.getBean(ReportProducer.class)
        );
        UserServiceSingleton.getInstance(userService);
    }
}
