import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CloudComputingAv1Application {

    public static void main(String[] args) {
        // Prints para debug
        System.out.println("DB User: " + System.getenv("DB_USERNAME"));
        System.out.println("DB Password: " + System.getenv("DB_PASSWORD"));

        SpringApplication.run(CloudComputingAv1Application.class, args);
    }
}
