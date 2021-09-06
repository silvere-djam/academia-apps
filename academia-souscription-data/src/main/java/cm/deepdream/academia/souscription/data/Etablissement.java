package cm.deepdream.academia.souscription.data;
import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Etablissement implements Serializable{
	@Id
	@Column(name="id")
	private Long id ;
	
	@Column (name = "libelle")
	private String libelle ;
	
	@Column (name = "telephone")
	private String telephone ;
	
	@Column (name = "email")
	private String email ;
	
	@Column (name = "boite_postale")
	private String boitePostale ;
	
	@Column (name = "cycle")
	private String cycle ;
	
	@Column (name = "nb_eleves_approx")
	private Integer nbElevesApprox ;
	
	@ManyToOne
	@JoinColumn (name = "id_localite")
	private Localite localite ;
	
	@Embedded
	@AttributeOverrides({
		  @AttributeOverride( name = "path", column = @Column(name = "logo_path")),
		  @AttributeOverride( name = "subPath", column = @Column(name = "logo_sub_path")),
		  @AttributeOverride( name = "fileName", column = @Column(name = "logo_filename")),
		  @AttributeOverride( name = "contentType", column = @Column(name = "logo_content_type")),
		  @AttributeOverride( name = "size", column = @Column(name = "logo_size"))
		})
	private Logo logo ;
	
	@Embedded
	@AttributeOverrides({
		  @AttributeOverride( name = "nom", column = @Column(name = "nom_chef")),
		  @AttributeOverride( name = "telephone", column = @Column(name = "telephone_chef")),
		  @AttributeOverride( name = "email", column = @Column(name = "email_chef"))
		})
	private Contact contactChef ;
	
	@Embedded
	@AttributeOverrides({
		  @AttributeOverride(name = "nom", column = @Column(name = "nom_informaticien")),
		  @AttributeOverride(name = "telephone", column = @Column(name = "telephone_informaticien")),
		  @AttributeOverride(name = "email", column = @Column(name = "email_informaticien"))
		})
	private Contact contactInformaticien ;
	
	@Column(name = "date_creation")
	private LocalDateTime dateCreation ;
	
	@Column(name = "date_dern_maj")
	private LocalDateTime dateDernMaj ;
	
	@Column(name = "createur")
	private String createur ;
	
	@Column(name = "modificateur")
	private String modificateur ;
	
}
