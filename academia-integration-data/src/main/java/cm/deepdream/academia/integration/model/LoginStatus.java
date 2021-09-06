package cm.deepdream.academia.integration.model;
import java.io.Serializable;
import java.time.LocalDateTime;

import cm.deepdream.academia.programmation.data.AnneeScolaire;
import cm.deepdream.academia.security.data.Utilisateur;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class LoginStatus implements Serializable{
	private Utilisateur utilisateur ;
	private Boolean status ;
	private LocalDateTime dateLoggedIn ;
	private AnneeScolaire anneeScolaire ;
}
