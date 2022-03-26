package cm.deepdream.academia.souscription.exceptions;

public class OffreNotFoundException extends RuntimeException{
	
	public OffreNotFoundException() {
		super() ;
	}
	
	public OffreNotFoundException(String message) {
		super(message) ;
	}
	
	
	public OffreNotFoundException(String message, Throwable t) {
		super(message, t) ;
	}
	
}
