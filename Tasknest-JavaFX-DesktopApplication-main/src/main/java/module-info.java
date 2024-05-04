module Test{
    requires javafx.controls;
    requires javafx.fxml;
    requires  javafx.graphics;
    requires java.sql;
    requires com.google.gson;
    requires spring.security.core;
    requires java.mail;
    requires com.google.protobuf;
    opens tasknest.controllers;
    opens tasknest.models;
    opens tasknest.services;
    opens tasknest.tests;

    //erijjj
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires itextpdf;
    requires javafx.swing;
    //requires activation;
    requires twilio;
    requires java.activation;
    //requires activation;
    opens tasknest.controllers.CV;
    opens tasknest.controllers.Skill;
    opens dashboard.controllers;

    //yasssssss
    //requires twilio;
    opens tasknest.controllers.offer;
    exports tasknest.controllers.applications;
    opens tasknest.controllers.applications to javafx.fxml;
}