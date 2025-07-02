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
    requires java.sql;
    requires com.fasterxml.jackson.databind;
    requires ch.qos.logback.classic;
    requires java.management;

    opens sims.hodaksims to javafx.fxml;
    exports sims.hodaksims;
    exports sims.hodaksims.controller;
    opens sims.hodaksims.controller to javafx.fxml;
    exports sims.hodaksims.model;
    opens sims.hodaksims.model to javafx.fxml;
    exports sims.hodaksims.main;
    opens sims.hodaksims.main to javafx.fxml;
    exports sims.hodaksims.interfaces;
    exports sims.hodaksims.controller.update;
    opens sims.hodaksims.controller.update to javafx.fxml;
    exports sims.hodaksims.controller.list;
    opens sims.hodaksims.controller.list to javafx.fxml;
    exports sims.hodaksims.controller.add;
    opens sims.hodaksims.controller.add to javafx.fxml;

}