package cm.deepdream.academia.configsrv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableConfigServer
@EnableEurekaClient
public class AcademiaConfigsrvApplication {

	public static void main(String[] args) {
		SpringApplication.run(AcademiaConfigsrvApplication.class, args);
	}

}
