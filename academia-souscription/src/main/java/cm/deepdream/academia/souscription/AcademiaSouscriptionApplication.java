package cm.deepdream.academia.souscription;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@EnableCaching
@EntityScan({"cm.deepdream.academia.souscription.model"}) 
@EnableJpaRepositories(basePackages = "cm.deepdream.academia.souscription.repository")
@SpringBootApplication
public class AcademiaSouscriptionApplication {
	@Value("${app.souscription.aws.accessKey}")
	private String accessKey ;
	@Value("${app.souscription.aws.secretKey}")
	private String secretKey ;
	@Value("${app.souscription.aws.regionCode}")
	private String regionCode ;
	
	public static void main(String[] args) {
		SpringApplication.run(AcademiaSouscriptionApplication.class, args);
	}
	
	@Bean
    public AmazonS3 s3() {
        AWSCredentials awsCredentials =
                new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder
                .standard()
                .withRegion(regionCode)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();

    }

}
