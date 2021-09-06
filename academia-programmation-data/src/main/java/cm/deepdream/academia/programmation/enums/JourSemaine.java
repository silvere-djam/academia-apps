package cm.deepdream.academia.programmation.enums;

public enum JourSemaine {
	LUNDI (1, "Lundi"), MARDI (2, "Mardi"), MERCREDI (3, "Mercredi"), JEUDI (4, "Jeudi"), VENDREDI (5, "Vendredi"),
	SAMEDI (6, "Samedi"), DIMANCHE (7, "Dimanche");
	
	int id ;
	String libelle ;
	
	JourSemaine(int id, String libelle) {
		this.id = id ;
		this.libelle = libelle ;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	
	public static String getLibelle(int id) {
		if(LUNDI.getId() == id) return LUNDI.getLibelle() ;
		else if(MARDI.getId() == id) return MARDI.getLibelle() ;
		else if(MERCREDI.getId() == id) return MERCREDI.getLibelle() ;
		else if(JEUDI.getId() == id) return JEUDI.getLibelle() ;
		else if(VENDREDI.getId() == id) return VENDREDI.getLibelle() ;
		else if(SAMEDI.getId() == id) return SAMEDI.getLibelle() ;
		else if(DIMANCHE.getId() == id) return DIMANCHE.getLibelle() ;
		return null ;
	}
}
