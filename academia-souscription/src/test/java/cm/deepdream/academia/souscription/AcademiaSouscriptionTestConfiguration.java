package cm.deepdream.academia.souscription;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class AcademiaSouscriptionTestConfiguration {
	
	@Bean
	public TestRestTemplate restTemplate() {
		return new TestRestTemplate() ; 
	}
}
