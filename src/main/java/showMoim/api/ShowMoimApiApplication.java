package showMoim.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ShowMoimApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShowMoimApiApplication.class, args);
	}

}
