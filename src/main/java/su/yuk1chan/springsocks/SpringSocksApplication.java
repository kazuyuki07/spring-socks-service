package su.yuk1chan.springsocks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SpringSocksApplication {
    static void main(String[] args) {
        SpringApplication.run(SpringSocksApplication.class, args);
    }
}
