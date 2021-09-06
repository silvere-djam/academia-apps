package cm.deepdream.academia.souscription.data;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import lombok.Data;

@Embeddable
@Data
public class Logo implements Serializable{
	@Column(name = "path")
	private String path ;
	
	@Column(name = "sub_path")
	private String subPath ;
	
	@Column(name = "file_name")
	private String fileName ;
	
	@Column(name = "content_type")
	private String contentType ;
	
	@Transient
	private byte[] bytes ;
	
	@Column(name = "size")
	private Long size ;
}
