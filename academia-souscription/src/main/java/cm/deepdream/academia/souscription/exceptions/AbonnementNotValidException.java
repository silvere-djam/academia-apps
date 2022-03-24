package cm.deepdream.academia.souscription.exceptions;

public class AbonnementNotValidException extends RuntimeException{
	
	public AbonnementNotValidException() {
		super() ;
	}
	
	public AbonnementNotValidException(String message) {
		super(message) ;
	}
	
	
	public AbonnementNotValidException(String message, Throwable t) {
		super(message, t) ;
	}
	
}
