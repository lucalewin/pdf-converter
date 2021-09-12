package net.lucraft.converter;

import javafx.application.Application;
import net.lucraft.converter.gui.ConverterApplication;

public class Main {

	public static void main(String[] args) {
		Config.load();
		Application.launch(ConverterApplication.class, args);
	}
}
