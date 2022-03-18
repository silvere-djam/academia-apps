package cm.deepdream.academia.viescolaire.util;
import java.text.Normalizer;

import java.util.Arrays;
import java.util.stream.Collectors;


public class StringToolkit {
	private static StringToolkit stringToolkit ;

	
	private StringToolkit() {
		
	}
	
	public static StringToolkit getToolkit() {
		if(stringToolkit == null) {
			stringToolkit = new StringToolkit() ;
		}
		return stringToolkit ;
	}
	
	public synchronized String normalizePath(String text) throws Exception {
		String normalized  = Normalizer.normalize(text, Normalizer.Form.NFD); 
		String replaced = normalized.replaceAll("[^\\p{ASCII}]", "").replaceAll(" ", "_");
		return replaced ;
	}
	
	public synchronized String capitalize(String text) throws Exception {
		String capitalized = Arrays.asList(text.split(" "))
			.stream()
			.map(str -> str.trim())
			.map(str -> str.substring(0, 1).toUpperCase()+str.substring(1).toLowerCase())
			.collect(Collectors.joining(" "));
		return capitalized ;
	}
	
	public static void main(String[] args) throws Exception{
		int text = 12 ;
		System.out.println(String.format("%02d" , text));
	}

}
