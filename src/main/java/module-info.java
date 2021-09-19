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
	requires org.slf4j;

	opens net.lucraft.converter to com.google.gson;
	opens net.lucraft.converter.gui.controller to javafx.fxml;
	opens net.lucraft.converter.gui.fragment to javafx.fxml;

	exports net.lucraft.converter;

	exports net.lucraft.converter.gui to javafx.graphics;
	exports net.lucraft.converter.gui.controller to javafx.graphics, javafx.fxml;
}