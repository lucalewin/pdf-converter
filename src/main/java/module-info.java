module lucraft.converter {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.web;
	requires javafx.swing;

	requires java.desktop;

	requires org.controlsfx.controls;
	requires com.dlsc.formsfx;
	requires validatorfx;
	requires org.kordamp.ikonli.javafx;
	requires org.kordamp.bootstrapfx.core;
	requires eu.hansolo.tilesfx;
	requires kernel;
	requires layout;
	requires io;
	requires com.google.gson;
	requires org.jetbrains.annotations;
	requires org.apache.commons.io;

	opens net.lucraft.converter to javafx.fxml;
	opens net.lucraft.converter.ui to javafx.fxml;
	opens net.lucraft.converter.config to com.google.gson, javafx.fxml;

	exports net.lucraft.converter to javafx.graphics;
	exports net.lucraft.converter.config to javafx.graphics, com.google.gson;

}