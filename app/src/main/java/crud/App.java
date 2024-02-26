package crud;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Main class for starting the CRUD application.
 * This class contains the main method that initializes and runs the Spring Boot
 * application.
 */
@SpringBootApplication
public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    /**
     * Main method to start the Spring Boot application.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        logger.info("Starting CRUD application...");
        SpringApplication.run(App.class, args);
    }

    /**
     * Configures and loads environment variables using Dotenv.
     *
     * @return Dotenv instance with loaded environment variables.
     */
    @Bean
    public Dotenv dotenv() {
        logger.info("Configuring and loading environment variables using Dotenv...");
        Dotenv dotenv = Dotenv.configure().load();
        logger.info("Dotenv configured and environment variables loaded successfully.");
        return dotenv;
    }
}
