module com.kodilla.checkers {
    requires javafx.controls;

    opens com.kodilla.checkers to javafx.fxml;
    exports com.kodilla.checkers;

}