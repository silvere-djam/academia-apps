package cm.deepdream.academia.integration.viescolaire;
import java.text.Normalizer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cours")
public class CoursAPI {
	
	
	public static void main(String[] arrgs) {
		String text = "Ecole Nationale Supérieure Polytechnique de Yaoundé" ;
		String normalized  = Normalizer.normalize(text, Normalizer.Form.NFD); 
		System.out.println("normalized="+normalized);
		String replaced = normalized.replaceAll("[^\\p{ASCII}]", "").replaceAll(" ", "_");
		System.out.println("replaced="+replaced);
	}

}




