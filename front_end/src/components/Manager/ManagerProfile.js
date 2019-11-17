import React, {Component} from 'react';
import CreateAgentForm from "./CreateAgentForm";
import Routes from "./Routes";
import Employees from "./Employees";

class ManagerProfile extends Component{
    render() {
        return (
            <div className='container d-flex'>
                <div className="col-3 nav flex-column nav-pills" id="v-pills-tab" role="tablist" aria-orientation="vertical">
                    <a className="nav-link active" id="v-pills-home-tab" data-toggle="pill" href="#v-pills-profile"
                       role="tab" aria-controls="v-pills-home" aria-selected="true">Profile</a>
                    <a className="nav-link" id="v-pills-profile-tab" data-toggle="pill" href="#v-pills-routes"
                       role="tab" aria-controls="v-pills-profile" aria-selected="false">Routes</a>
                    <a className="nav-link" id="v-pills-messages-tab" data-toggle="pill" href="#v-pills-employees"
                       role="tab" aria-controls="v-pills-messages" aria-selected="false">Employees</a>
                    <a className="nav-link" id="v-pills-settings-tab" data-toggle="pill" href="#v-pills-agents"
                       role="tab" aria-controls="v-pills-settings" aria-selected="false">Agents</a>
                </div>
                <div className="offset-1 tab-content col-8"  id="v-pills-tabContent">
                    <div className="tab-pane fade show active" style={{backgroundColor:"#ced3f2 !important"}} id="v-pills-profile" role="tabpanel"
                         aria-labelledby="v-pills-home-tab">...
                    </div>
                    <div className="tab-pane fade" id="v-pills-routes" role="tabpanel"
                         aria-labelledby="v-pills-profile-tab">
                        <Routes></Routes>
                    </div>
                    <div className="tab-pane fade" id="v-pills-employees" role="tabpanel"
                         aria-labelledby="v-pills-messages-tab">
                        <Employees></Employees>
                    </div>
                    <div className="tab-pane fade" id="v-pills-agents" role="tabpanel"
                         aria-labelledby="v-pills-settings-tab">
                        <CreateAgentForm></CreateAgentForm>
                    </div>
                </div>
            </div>
        );
    }
}






export default ManagerProfile;