import React from 'react'
import ReactDOM from 'react-dom'

class CentresExamen extends React.Component {
		
	constructor(props) {
		super(props);
	
	}

		
	
	
    render() {
		return (
				<span className="text-danger">Hello {this.props.name} !</span>
		) ;
    }
}
export default CentresExamen ;
ReactDOM.render(<CentresExamen name="David Hermann" />, document.getElementById('form-row')) ;