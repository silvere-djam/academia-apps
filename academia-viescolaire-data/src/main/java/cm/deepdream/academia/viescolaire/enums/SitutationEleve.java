package cm.deepdream.academia.viescolaire.enums;

public enum SitutationEleve {
	SUSPENDU (0, "Suspendu"), INSCRIT (1, "Inscrit"), EXCLU (2, "Exclu"), NON_INSCRIT (3, "Non inscrit") ;
	
	private int id ;
	private String libelle ;
	
	private SitutationEleve (int id, String libelle) {
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

	public static String getLibelle (int id) {
		if(INSCRIT.getId() == id) return INSCRIT.getLibelle() ;
		else if(SUSPENDU.getId() == id) return SUSPENDU.getLibelle() ;
		else if(EXCLU.getId() == id) return EXCLU.getLibelle() ;
		else if(NON_INSCRIT.getId() == id) return NON_INSCRIT.getLibelle() ;
		return null ;
	}
}
