package cm.deepdream.academia.integration.model;
import java.io.Serializable;
import cm.deepdream.academia.programmation.data.Eleve;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class SimpleReponse implements Serializable{
	private Long id ;
	private Boolean recognized ;
	private Eleve eleve ;
	private Float confidence ;
}
