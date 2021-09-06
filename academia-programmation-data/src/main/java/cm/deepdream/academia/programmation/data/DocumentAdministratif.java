package cm.deepdream.academia.programmation.data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentAdministratif extends EntiteGenerique{
	@Id
	@Column(name="id")
	private Long id ;
	
	@Column(name = "path")
	private String path ;
	
	@Column(name = "sub_path_1")
	private String subPath1 ;
	
	@Column(name = "sub_path_2")
	private String subPath2 ;
	
	@Column(name = "file_name")
	private String fileName ;
	
	@Column(name = "content_type")
	private String contentType ;
	
	@Transient
	private byte[] bytes ;
	
	@Transient
	private boolean modified ;
	
	@Column(name = "size")
	private Long size ;
	
}
