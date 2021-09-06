package cm.deepdream.academia.eureka;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
@EnableEurekaServer
@SpringBootApplication
public class AcademiaEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AcademiaEurekaApplication.class, args);
	}

}
