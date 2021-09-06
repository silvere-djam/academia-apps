package cm.deepdream.academia.programmation.exceptions;

public class SecuriteException extends Exception{

	public SecuriteException() {
		super() ;
	}
	
	public SecuriteException(String message) {
		super(message) ;
	}
	
	public SecuriteException(String message, Throwable t) {
		super(message, t) ;
	}
}
