package ${groupId};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication    //(scanBasePackages = "com.itar.soa.biwan")  这个玩意别瞎加，还好我找到原因了，你自己看下有biwan这个包吗？还scan，我去
public class BootLauncher {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(BootLauncher.class);
        springApplication.run(args);
    }
}
