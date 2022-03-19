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

    requires java.prefs;
//    requires java.xml.bind;
    requires jaxb.api;

    exports ml.diarpy.addressapp;
    exports ml.diarpy.addressapp.controller;
    exports ml.diarpy.addressapp.model;
    opens ml.diarpy.addressapp.controller to javafx.fxml;
}