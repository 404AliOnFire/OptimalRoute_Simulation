module projectalgo.minimumcost_prj {
    requires javafx.controls;
    requires javafx.fxml;


    opens minimumcost_prj to javafx.fxml;
    exports minimumcost_prj;
    exports minimumcost_prj.ProjectCode;
    opens minimumcost_prj.ProjectCode to javafx.fxml;
}