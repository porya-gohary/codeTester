module com.tester.codetester {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.tester.codetester to javafx.fxml;
    exports com.tester.codetester;
}