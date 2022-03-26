package cm.deepdream.academia.souscription.exceptions;

public class ResponsableNotFoundException extends RuntimeException{
	
	public ResponsableNotFoundException() {
		super() ;
	}
	
	public ResponsableNotFoundException(String message) {
		super(message) ;
	}
	
	
	public ResponsableNotFoundException(String message, Throwable t) {
		super(message, t) ;
	}
	
}
