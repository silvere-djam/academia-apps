package cm.deepdream.academia.souscription.messages;
import java.io.Serializable;
import cm.deepdream.academia.souscription.data.EntiteGenerique;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Document implements Serializable{
	private String action ;
	private String entiteGenerique ;
	private String type ;
}
