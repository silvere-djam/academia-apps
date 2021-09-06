class CentresExamen extends React.Component {

    constructor() {
        this.state = {
            listeExamens: [],
            listeAnneesScolaires: []
        }
    }

    render() {
        return (
            <div className="col-md-2" >
                <form action="/programmation/centreexamen/recherche" method="POST" >

                    <div className="position-relative form-group"  >
                        <label for="anneeScolaire" className="font-weight-bold" >Année scolaire <span className="text-danger">*</span></label>
                        <select name="anneeScolaire.id" className="multiselect-dropdown form-control" id="anneeScolaire" required >
                            {this.state.listeAnneesScolaires.map((annee, i) = > {
                                <option key = {i} value = {annee.id} > {annee.libelle}</option>
                            })}
                        </select>
                    </div>

                    <div className="position-relative form-group"  >
                        <label for="examen" className="font-weight-bold" >Examen <span className="text-danger">*</span></label>
                        <select name="examen.id" className="multiselect-dropdown form-control" id="examen" required >
                             {this.state.listeExamens.map((examen, i) = > {
                                <option key = {i} value = {examen.id} > {examen.libelle}</option>
                            })}
                        </select>
                    </div>

                    <div class="modal-footer clearfix" >
                        <div class="float-right">
                            <button className="tn-shadow btn-wide float-right btn-pill btn-hover-shine btn btn-primary" >Rechercher</button>
                        </div>
                    </div>
                </form>
            </div>
        );
    }
}

export default CentresExamen;

