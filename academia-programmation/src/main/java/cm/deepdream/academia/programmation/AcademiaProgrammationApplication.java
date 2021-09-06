package cm.deepdream.academia.programmation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
@EnableCaching
@EnableBinding(Source.class)
@EntityScan({"cm.deepdream.academia.programmation.data", "cm.deepdream.academia.souscription.data"}) 
@EnableJpaRepositories(basePackages = "cm.deepdream.academia.programmation.repository")
@EnableEurekaClient
@SpringBootApplication
public class AcademiaProgrammationApplication {
	@Autowired
	private Environment env ;
	@Value("${app.souscription.aws.accessKey}")
	private String accessKey ;
	@Value("${app.souscription.aws.secretKey}")
	private String secretKey ;
	@Value("${app.souscription.aws.regionCode}")
	private String regionCode ;

	public static void main(String[] args) {
		SpringApplication.run(AcademiaProgrammationApplication.class, args);
	}
	
	@Bean
    public AmazonS3 s3() {
        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder
                .standard()
                .withRegion(regionCode)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();

    }

}
