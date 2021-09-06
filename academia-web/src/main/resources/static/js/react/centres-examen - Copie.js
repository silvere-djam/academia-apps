import React from 'react'
import ReactDOM from 'react-dom'

class CentresExamen extends React.Component {
		
	constructor(props) {
		super(props);
		this.state = {
            listeExamens: [],
            listeAnneesAcademiques: []
         }
	}

		
	componentDidMount() {
    	fetch("http://localhost:8080/api/anneesacademiques")
      		.then(res => res.json())
      		.then(
				(response) => {
						this.setState({
							listeAnneesAcademiques: response
						}) ;
				},
       
        		(error) => {
          			this.setState({
            			listeAnneesAcademiques: []
          			}) ;
        		}) ;
  	}
	
	
    render() {
		return (
			<React.Fragment>
				<div className="col-md-2" >
					<form action="/programmation/centreexamen/recherche" method="POST" >
						<div className="position-relative form-group"  >
                        	<label for="anneeAcademique" className="font-weight-bold" >Année académique <span className="text-danger">*</span></label>
                        	<select name="anneeScolaire.id" className="multiselect-dropdown form-control" id="anneeAcademique" required >
                            	{this.state.listeAnneesAcademiques.map((annee) => {
                                	<option key = {annee.i} value={annee.id} > {annee.libelle}</option>
                            	})}
                        	</select>
                    	</div>
						<div className="position-relative form-group"  >
                        	<label for="examen" className="font-weight-bold" >Examen <span className="text-danger">*</span></label>
                        	<select name="examen.id" className="multiselect-dropdown form-control" id="examen" required >
                             	{this.state.listeExamens.map((examen) => {
                                	<option key = {examen.i} value = {examen.id} > {examen.libelle}</option>
                            	})}
                        	</select>
                    	</div>
						<div className="modal-footer clearfix" >
                        	<div className="float-right" >
                            	<button className="tn-shadow btn-wide float-right btn-pill btn-hover-shine btn btn-primary" >Rechercher</button>
                        	</div>
                    	</div>
					</form>
				</div>
				<div className="col-md-10" >
				</div>
			</React.Fragment>
		) ;
    }
}
export default CentresExamen ;