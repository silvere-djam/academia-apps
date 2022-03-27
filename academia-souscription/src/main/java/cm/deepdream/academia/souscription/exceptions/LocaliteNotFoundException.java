package cm.deepdream.academia.souscription.exceptions;

public class LocaliteNotFoundException extends RuntimeException{
	
	public LocaliteNotFoundException() {
		super() ;
	}
	
	public LocaliteNotFoundException(String message) {
		super(message) ;
	}
	
	
	public LocaliteNotFoundException(String message, Throwable t) {
		super(message, t) ;
	}
	
	
}
