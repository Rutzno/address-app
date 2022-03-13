module ml.diarpy.addressapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens ml.diarpy.addressapp to javafx.fxml;
    exports ml.diarpy.addressapp;
    exports ml.diarpy.addressapp.controller;
    opens ml.diarpy.addressapp.controller to javafx.fxml;
}