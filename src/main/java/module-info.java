module lawson.lonchi.morpion {
    requires javafx.controls;
    requires javafx.fxml;

    opens lawson.lonchi.morpion to javafx.fxml;
    exports lawson.lonchi.morpion;

     // Exportez le package contenant le contrôleur
     exports lawson.lonchi.morpion.controller to javafx.fxml;
     opens lawson.lonchi.morpion.controller to javafx.fxml;


     // Exportez les autres packages si nécessaire
     exports lawson.lonchi.morpion.model;
    //  exports lawson.lonchi.morpion.view;
}
