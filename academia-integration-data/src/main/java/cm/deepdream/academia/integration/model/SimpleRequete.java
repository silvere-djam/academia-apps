package cm.deepdream.academia.integration.model;
import java.io.Serializable;

import cm.deepdream.academia.programmation.data.Classe;
import cm.deepdream.academia.programmation.data.SalleExamen;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class SimpleRequete implements Serializable{
	private Long id ;
	private String photo ;
	private String typeActivite ;
	private Long idActivite ;
	private Long idClasse ;
	private Long idSalleExamen ;

}
