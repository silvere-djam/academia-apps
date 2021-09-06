package cm.deepdream.academia.programmation.enums;

public enum StatutE {
	Nouveau (1, "Nouveau"), Redoublant (2, "Redoublant"), Triplant (3, "Triplant"), Quadruplant (4, "Quadruplant") ;
	
	private int id ;
	private String libelle ;
	
	private StatutE (int id, String libelle) {
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
		if(Nouveau.getId() == id) return Nouveau.getLibelle() ;
		else if(Redoublant.getId() == id) return Redoublant.getLibelle() ;
		else if(Triplant.getId() == id) return Triplant.getLibelle() ;
		else if(Quadruplant.getId() == id) return Quadruplant.getLibelle() ;
		return null ;
	}
}
