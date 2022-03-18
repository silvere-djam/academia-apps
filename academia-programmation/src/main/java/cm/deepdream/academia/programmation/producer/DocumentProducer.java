package cm.deepdream.academia.programmation.producer;
import java.io.Serializable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;
import cm.deepdream.academia.programmation.messages.Document;

@Component
public class DocumentProducer implements Serializable{
	private Logger logger = Logger.getLogger(DocumentProducer.class.getName()) ;
	@Autowired
	private Source source ;
	@Value("${app.academia.kafka.partition-0}")
	private String partition1 ;
	@Value("${app.academia.kafka.partition-1}")
	private String partition2 ;
	@Value("${app.academia.kafka.partition-2}")
	private String partition3 ;
	@Value("${app.academia.kafka.partition-3}")
	private String partition4 ;
	@Value("${app.academia.kafka.partition-4}")
	private String partition5 ;
	private ScheduledExecutorService quickService = Executors.newScheduledThreadPool(20) ;
	
	
	public void publier(Document document) {
		logger.info("Document to be published "+document.getEntiteGenerique());
		quickService.submit(new Runnable() {
			@Override
			public void run() {
				try{
					source.output().send(MessageBuilder.withPayload(document)
						  .setHeader("partitionKey", partition1)
						  .build()) ;
					logger.info("Document "+document+" has been published to "+partition1);
					source.output().send(MessageBuilder.withPayload(document)
		                       .setHeader("partitionKey", partition2)
						       .build()) ;
					logger.info("Document "+document+" has been published to "+partition2);
					source.output().send(MessageBuilder.withPayload(document)
		                       .setHeader("partitionKey", partition3)
						       .build()) ;
					logger.info("Document "+document+" has been published to "+partition3);
					source.output().send(MessageBuilder.withPayload(document)
		                       .setHeader("partitionKey", partition4)
						       .build()) ;
					logger.info("Document "+document+" has been published to "+partition4);
					source.output().send(MessageBuilder.withPayload(document)
		                       .setHeader("partitionKey", partition5)
						       .build()) ;
					logger.info("Document "+document+" has been published to "+partition5);
				}catch(Exception ex){
					logger.log(Level.SEVERE, "Erreur pendant la publication",ex) ;
				}
			}
		});
		
	}

}
