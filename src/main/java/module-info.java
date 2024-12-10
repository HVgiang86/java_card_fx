module co.sun.auto.fluter.demofx {
    requires javafx.controls;
    requires javafx.fxml;
    requires io.ebean;
    requires io.ebean.api;  // Ensure the Ebean API module is required
    requires org.slf4j;     // For SLF4J logging
    requires logback.classic; // Logging implementation
    requires com.fasterxml.jackson.databind;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires java.smartcardio;

    // Open packages to JavaFX for FXML and other JavaFX integrations
    opens co.sun.auto.fluter.demofx to javafx.fxml;

    opens co.sun.auto.fluter.demofx.model to javafx.fxml;  // Open model package to FXML
    opens co.sun.auto.fluter.demofx.view.viewcontroller to javafx.fxml;

    // Export necessary packages for access
    exports co.sun.auto.fluter.demofx;
    exports co.sun.auto.fluter.demofx.model;
    exports co.sun.auto.fluter.demofx.view.viewcontroller;
}