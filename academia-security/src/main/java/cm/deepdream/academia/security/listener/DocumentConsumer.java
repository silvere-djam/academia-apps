package cm.deepdream.academia.security.listener;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import cm.deepdream.academia.programmation.util.DeserializerToolkit;
import cm.deepdream.academia.security.service.EtablissementService;
import cm.deepdream.academia.security.service.UtilisateurService;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.enums.Action;
import cm.deepdream.academia.souscription.messages.Document;

@EnableBinding(Sink.class)
public class DocumentConsumer {
	private Logger logger = Logger.getLogger(DocumentConsumer.class.getName()) ;
	@Autowired
	private UtilisateurService utilisateurService ;
	@Autowired
	private EtablissementService etablissementService ;
	

	
	@StreamListener(Sink.INPUT)
	public void enregistrer (Document document) throws Exception{
		logger.log(Level.INFO, "Enregistrement du document "+document);
		if(document.getType().equals(Etablissement.class.getName()) &&
				Action.Nouveau.name().equals(document.getAction())) {
			logger.info("Enregistrement de l'etablissement 0");
			Etablissement etablissement = (Etablissement) DeserializerToolkit.getToolkit().deserialize(document.getEntiteGenerique(), 
					Etablissement.class);
			if(Action.Nouveau.name().equals(document.getAction())) {
				Etablissement etablissementCree = etablissementService.creer(etablissement) ;
				utilisateurService.creerUtilisateurs(etablissementCree);
			}
		} 
	}

}
