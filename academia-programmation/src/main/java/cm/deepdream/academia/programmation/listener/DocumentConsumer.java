package cm.deepdream.academia.programmation.listener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.transaction.annotation.Transactional;

import cm.deepdream.academia.programmation.dao.SequenceDAO;
import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.programmation.data.Fonction;
import cm.deepdream.academia.programmation.repository.AnneeScolaireRepository;
import cm.deepdream.academia.programmation.repository.EtablissementRepository;
import cm.deepdream.academia.programmation.service.AnneeScolaireService;
import cm.deepdream.academia.programmation.util.DeserializerToolkit;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.enums.Action;
import cm.deepdream.academia.souscription.messages.Document;

@Transactional
@EnableBinding(Sink.class)
public class DocumentConsumer {
	private Logger logger = Logger.getLogger(DocumentConsumer.class.getName()) ;
	@Autowired
	private EtablissementRepository etablissementRepository ;
	@Autowired
	private AnneeScolaireService anneeScolaireService ;
	@Autowired
	private SequenceDAO sequenceDAO ;

	
	public DocumentConsumer() {

	}
	
	@StreamListener(Sink.INPUT)
	public void enregistrer (Document document) throws Exception{
		logger.log(Level.INFO, "Enregistrement du document "+document);
		if(document.getType().equals(Etablissement.class.getName())) {
			logger.info("Enregistrement de l'etablissement 0");
			Etablissement etablissement = (Etablissement) DeserializerToolkit.getToolkit().deserialize(document.getEntiteGenerique(), 
					Etablissement.class);
			logger.info("Enregistrement de l'etablissement "+etablissement);
			Etablissement etablissementCree = etablissementRepository.save(etablissement) ;
			if(document.getAction().equals(Action.Nouveau.name())) {
				int year =  Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy"))) ;
				int currentMonth = Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("MM"))) ;
				if(currentMonth < 9) {
					year = year - 1 ;
				}
				
				LocalDate dateDebut = LocalDate.parse("01/09/"+year, DateTimeFormatter.ofPattern("dd/MM/yyyy")) ;
				LocalDate dateFin = LocalDate.parse("30/06/"+(year+1), DateTimeFormatter.ofPattern("dd/MM/yyyy")) ;
				AnneeScolaire anneeScolaire = new AnneeScolaire() ;
				anneeScolaire.setId(sequenceDAO.nextGlobalId(AnneeScolaire.class.getName()));
				anneeScolaire.setNum(sequenceDAO.nextId(etablissementCree, AnneeScolaire.class.getName()));
				anneeScolaire.setLibelle(year+"/"+(year+1));
				anneeScolaire.setDateDebut(dateDebut);
				anneeScolaire.setDateFin(dateFin);
				anneeScolaire.setCourante(1);
				anneeScolaire.setEtablissement(etablissementCree);
				anneeScolaireService.creer(anneeScolaire) ;
			}
		} 
	}

}