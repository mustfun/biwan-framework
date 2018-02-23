package ${package}.biz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author dengzhiyuan
 */
@SpringBootApplication
@ComponentScan(basePackages = {"${package}"})
public class BootLauncher {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(BootLauncher.class);
        springApplication.run(args);
    }
}
