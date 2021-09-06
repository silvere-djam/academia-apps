package cm.deepdream.academia.support.listener;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import cm.deepdream.academia.souscription.messages.Document;

@EnableBinding(Sink.class)
public class DocumentConsumer {
	private Logger logger = Logger.getLogger(DocumentConsumer.class.getName()) ;
	
	
	public DocumentConsumer() {

	}
	
	@StreamListener(Sink.INPUT)
	public void enregistrer (Document document) throws Exception{
		logger.log(Level.INFO, "Enregistrement du document "+document);
	}

}