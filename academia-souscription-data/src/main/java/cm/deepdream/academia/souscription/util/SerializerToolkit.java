package cm.deepdream.academia.souscription.util ;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import cm.deepdream.academia.souscription.data.Abonnement;
import cm.deepdream.academia.souscription.data.Etablissement;
import cm.deepdream.academia.souscription.data.Pays;
import cm.deepdream.academia.souscription.data.Localite;
import cm.deepdream.academia.souscription.serializers.AbonnementSerializer;
import cm.deepdream.academia.souscription.serializers.EtablissementSerializer;
import cm.deepdream.academia.souscription.serializers.PaysSerializer;
import cm.deepdream.academia.souscription.serializers.LocaliteSerializer;

public class SerializerToolkit {
	private static SerializerToolkit toolkit ;
	private ObjectMapper mapper = new ObjectMapper();
	
	private SerializerToolkit() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Pays.class, new PaysSerializer());
        module.addSerializer(Localite.class, new LocaliteSerializer());
        module.addSerializer(Etablissement.class, new EtablissementSerializer());
        module.addSerializer(Abonnement.class, new AbonnementSerializer()) ;
        mapper.registerModule(module);
	}
	
	public static SerializerToolkit getToolkit() {
		if(toolkit == null) {
			toolkit = new SerializerToolkit() ;
		}
		return toolkit ;
	}
	
	public String serialize(Object objet) throws Exception {
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objet) ; 
	}
}
