package cm.deepdream.academia.souscription.listener;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import cm.deepdream.academia.souscription.messages.Document;

@EnableBinding(Sink.class)
public class DocumentConsumer {
	private Logger logger = Logger.getLogger(DocumentConsumer.class.getName()) ;

	
	@StreamListener(Sink.INPUT)
	public void consommer (Document document) {
		logger.log(Level.INFO, "Receiving message document "+document);
	}

}
