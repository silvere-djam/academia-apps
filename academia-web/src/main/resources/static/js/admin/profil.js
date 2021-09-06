import React from 'react';
import ReactDOM from 'react-dom';

class Profil extends React.Component {
  constructor(props) {
    super(props);
    this.state = { showMatricule : 'none' };
  }
  
  handleChange = (event) => {
	var idProfil = event.target.value ;
	if(idProfil == 2 )
		this.setState({showMatricule: 'block'});
	else 
		this.state = { showMatricule : 'none' };
  }
  render() {
    return (
    	<div className="col-md-6">
            <div className="position-relative form-group" >
            	<label  className="">Profil <span className="text-danger">*</span></label>
            	<select className="mb-2 form-control" onChange = {{this.handleChange}} >
            		<option>Sélectionnez le profil</option>
                	<option value="1" >Administrateur</option>
                	<option value="2" >Chef d'établissement </option>
                	<option value="3" >Enseignant</option>
                	<option value="4" >Intendant</option>
                	<option value="5" >Parent d'élève</option>
            	</select>
            </div>
            <div className="position-relative form-group" style='display:{{showMatricule}}' >
        		<label  className="">Matricule <span className="text-danger">*</span></label>
        		<input name="matricule"  placeholder="Saisir le matricule" type="text"  className="form-control" >
        	</div>
        </div>
    );
  }
}

ReactDOM.render(<Profil />, document.getElementById('profil'))