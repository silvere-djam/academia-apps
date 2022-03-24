package cm.deepdream.academia.souscription.exceptions;

public class AbonnementNotFoundException extends RuntimeException{
	
	public AbonnementNotFoundException() {
		super() ;
	}
	
	public AbonnementNotFoundException(String message) {
		super(message) ;
	}
	
	
	public AbonnementNotFoundException(String message, Throwable t) {
		super(message, t) ;
	}
	
}
