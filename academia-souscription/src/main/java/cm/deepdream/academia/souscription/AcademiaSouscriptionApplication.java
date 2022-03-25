package cm.deepdream.academia.souscription;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableCaching
@EntityScan({"cm.deepdream.academia.souscription.model"}) 
@EnableJpaRepositories(basePackages = "cm.deepdream.academia.souscription.repository")
@SpringBootApplication
public class AcademiaSouscriptionApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(AcademiaSouscriptionApplication.class, args);
	}
	


}
