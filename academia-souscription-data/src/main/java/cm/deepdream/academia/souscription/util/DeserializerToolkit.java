package cm.deepdream.academia.souscription.util;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import cm.deepdream.academia.souscription.data.Abonnement;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.data.Pays;
import cm.deepdream.academia.souscription.data.Localite;
import cm.deepdream.academia.souscription.deserializers.AbonnementDeserializer;
import cm.deepdream.academia.souscription.deserializers.EtablissementDeserializer;
import cm.deepdream.academia.souscription.deserializers.PaysDeserializer;
import cm.deepdream.academia.souscription.deserializers.LocaliteDeserializer;

public class DeserializerToolkit {
	private Logger logger = Logger.getLogger(DeserializerToolkit.class.getName()) ;
	private static DeserializerToolkit toolkit ;
	private ObjectMapper mapper = new ObjectMapper();
	
	private DeserializerToolkit() {
		mapper.configure(Feature.AUTO_CLOSE_SOURCE, true);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Pays.class, new PaysDeserializer());
        module.addDeserializer(Localite.class, new LocaliteDeserializer());
        module.addDeserializer(Etablissement.class, new EtablissementDeserializer());
        module.addDeserializer(Abonnement.class, new AbonnementDeserializer()) ;
        mapper.registerModule(module);
	}
	
	public static DeserializerToolkit getToolkit() {
		if(toolkit == null) {
			toolkit = new DeserializerToolkit() ;
		}
		return toolkit ;
	}
	
	public Object deserialize(String stringObject, Class classType) throws Exception {
		logger.log(Level.INFO, "Content to deserialize "+stringObject);
		mapper.configure(Feature.AUTO_CLOSE_SOURCE, true);
		Object object =  mapper.readValue(new StringReader(stringObject), classType); 
		return object ;
	}

}
