package cm.deepdream.academia.integration;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
@RestController
@EnableEurekaClient
@SpringBootApplication
public class AcademiaIntegrationApplication {
	private String ls = System.getProperty("line.separator") ;

	public static void main(String[] args) {
		SpringApplication.run(AcademiaIntegrationApplication.class, args);
	}
	
	@LoadBalanced
	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate() ;
		return restTemplate ;
	}
	
	@GetMapping("/info")
	public String getInfo() {
		String info = "Welcome to Academia Integration Microservice" ;
		info +="Status : UP" ;
		return info ;
	}
	
}
