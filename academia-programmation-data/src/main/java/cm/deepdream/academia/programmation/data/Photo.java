package cm.deepdream.academia.programmation.data;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Photo implements Serializable{
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
	private String bytesStr ;
	
	@Transient
	private boolean modified ;
	
	@Column(name = "size")
	private Long size ;
}
