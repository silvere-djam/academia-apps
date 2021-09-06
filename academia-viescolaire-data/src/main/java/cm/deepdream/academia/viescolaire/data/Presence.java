package cm.deepdream.academia.viescolaire.data;
import java.time.LocalDate;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.springframework.format.annotation.DateTimeFormat;
import cm.deepdream.academia.programmation.data.Eleve;
import cm.deepdream.academia.programmation.data.Photo;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Presence extends EntiteGenerique{
	@Id
	@GeneratedValue
	@Column(name="id")
	private Long id ;
	
	@ManyToOne
	@JoinColumn (name = "id_eleve")
	private Eleve eleve ;
	
	@ManyToOne
	@JoinColumn (name = "id_cours")
	private Cours cours ;
	
	@ManyToOne
	@JoinColumn (name = "id_evaluation")
	private Evaluation evaluation ;
	
	@Column (name = "date_presence")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate datePresence ;
	
	@Column(name="confidence")
	private Float confidence ;
	
	@Embedded
	@AttributeOverrides({
		 @AttributeOverride( name = "path", column = @Column(name = "photo_path")),
		 @AttributeOverride( name = "subPath1", column = @Column(name = "photo_subpath_1")),
		 @AttributeOverride( name = "subPath2", column = @Column(name = "phto_subpath_2")),
		 @AttributeOverride( name = "fileName", column = @Column(name = "photo_filename")),
		 @AttributeOverride( name = "contentType", column = @Column(name = "photo_content_type")),
		 @AttributeOverride( name = "size", column = @Column(name = "photo_size"))
	})
	private Photo photo ;
	

}
