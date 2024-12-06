module co.sun.auto.fluter.demofx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires java.smartcardio;

    opens co.sun.auto.fluter.demofx to javafx.fxml;
    exports co.sun.auto.fluter.demofx;
    exports co.sun.auto.fluter.demofx.view.viewcontroller;
    opens co.sun.auto.fluter.demofx.view.viewcontroller to javafx.fxml;
    exports co.sun.auto.fluter.demofx.model;
    opens co.sun.auto.fluter.demofx.model to javafx.fxml;
}