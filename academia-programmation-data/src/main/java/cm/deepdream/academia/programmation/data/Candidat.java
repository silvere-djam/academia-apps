package cm.deepdream.academia.programmation.data;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Candidat extends EntiteGenerique{
	@Id
	@Column (name = "id")
	private Long id ;
	
	@Column (name = "numero")
	private String numero ;
	
	@ManyToOne
	@JoinColumn (name = "id_eleve")
	private Eleve eleve ;
	
	@ManyToOne
	@JoinColumn (name = "id_centre_examen")
	private CentreExamen centreExamen ;
	
	@ManyToOne
	@JoinColumn (name = "id_salle_examen")
	private SalleExamen salleExamen ;
	
	@ManyToOne
	@JoinColumn (name = "id_examen")
	private Examen examen ;
	
	@Column (name = "type")
	private String type ;
	
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
