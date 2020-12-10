import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "mapper")
public class SplitDbApplication {
    public static void main(String[] args) {
        SpringApplication.run(SplitDbApplication.class, args);
    }
}
