module sims.hodaksims {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.logging;
    requires org.slf4j;

    opens sims.hodaksims to javafx.fxml;
    exports sims.hodaksims;
    exports sims.hodaksims.controller;
    opens sims.hodaksims.controller to javafx.fxml;
    exports sims.hodaksims.model;
    opens sims.hodaksims.model to javafx.fxml;
    exports sims.hodaksims.main;
    opens sims.hodaksims.main to javafx.fxml;
}