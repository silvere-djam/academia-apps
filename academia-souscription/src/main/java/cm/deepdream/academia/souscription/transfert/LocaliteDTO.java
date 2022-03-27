package cm.deepdream.academia.souscription.transfert;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LocaliteDTO implements Serializable{
	private Long id ;

	private String libelle ;
	
	private Long idRegion ;

	private String libelleRegion ;
		
}
