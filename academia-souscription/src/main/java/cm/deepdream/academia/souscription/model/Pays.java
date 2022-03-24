package cm.deepdream.academia.souscription.model;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Pays implements Serializable{
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id ;
	
	@NotBlank
	@Column(name = "code")
	private String code ;
	
	@NotBlank
	@Column(name = "libelle")
	private String libelle ;
	
	@Column(name = "code_tel")
	private String codeTel ;
	
	@Column(name = "monnaie")
	private String monnaie ;
	
	
}
