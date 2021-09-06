package cm.deepdream.academia.security.enums;

public enum RoleU {
	Super_Administrateur (1, "Super Administrateur"), Administrateur (2, "Administrateur"),  
	Responsable_Etablissement (3, "Responsable d'établissement"), Responsable_Cours (4, "Responsable des cours"), 
	Responsable_Discipline (5, "Responsable de la discipline"), Responsable_finances (6, "Responsable des finances"),
	Enseignant (7, "Enseignant"), Chef_Classe (8, "Chef de classe"), Conseiller_Orientation (9, "Conseiller d'orientation"),
	Help_Desk (10, "Help Desk");
	
	private int id ;
	private String libelle ;
	
	private RoleU (int id, String libelle) {
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
		if(Super_Administrateur.getId() == id) return Super_Administrateur.getLibelle() ;
		else if(Administrateur.getId() == id) return Administrateur.getLibelle() ; 
		else if(Responsable_Etablissement.getId() == id) return Responsable_Etablissement.getLibelle() ; 
		else if(Responsable_Cours.getId() == id) return Responsable_Cours.getLibelle() ; 
		else if(Responsable_Discipline.getId() == id) return Responsable_Discipline.getLibelle() ;
		else if(Responsable_Cours.getId() == id) return Responsable_Cours.getLibelle() ;
		else if(Responsable_finances.getId() == id) return Responsable_finances.getLibelle() ;
		else if(Enseignant.getId() == id) return Chef_Classe.getLibelle() ; 
		else if(Conseiller_Orientation.getId() == id) return Conseiller_Orientation.getLibelle() ; 
		else if(Help_Desk.getId() == id) return Help_Desk.getLibelle() ; 
		return null ;
	}
}
