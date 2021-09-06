package cm.deepdream.academia.support.data;
import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import cm.deepdream.academia.souscription.data.Etablissement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@MappedSuperclass
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntiteGenerique implements Serializable{
	@Id
	@Column(name="id")
	private Long id ;
	
	@Column(name="num")
	private Long num ;
	
	@Column(name = "date_creation")
	private LocalDateTime dateCreation ;
	
	@Column(name = "date_dern_maj")
	private LocalDateTime dateDernMaj ;
	
	@Column(name = "createur")
	private String createur ;
	
	@Column(name = "modificateur")
	private String modificateur ;
	
	@ManyToOne
	@JoinColumn(name = "id_etablissement")
	private Etablissement etablissement ;
}
