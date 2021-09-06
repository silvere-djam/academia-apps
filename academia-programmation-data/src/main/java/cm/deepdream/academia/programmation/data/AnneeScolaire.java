package cm.deepdream.academia.programmation.data;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import cm.deepdream.academia.programmation.deserializers.AnneeScolaireDeserializer;
import cm.deepdream.academia.programmation.serializers.AnneeScolaireSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class AnneeScolaire extends EntiteGenerique{
	@Id
	@Column(name="id")
	private Long id ;
	
	@Column(name = "libelle")
	private String libelle ;
	
	@Column (name = "date_debut")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateDebut ;
	
	@Column (name = "date_fin")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateFin ;
	
	@Column(name = "courante")
	private Integer courante ;
	
}
