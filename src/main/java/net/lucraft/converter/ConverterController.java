package net.lucraft.converter;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.FileChooser;
import net.lucraft.converter.ui.FileListCell;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

public class ConverterController {

	@FXML private Button btnConvert;
	@FXML private Button btnAddFiles;
	@FXML private Button btnRemoveFiles;
	@FXML private Button btnClear;
	@FXML private CheckBox cbCombine;
	@FXML private ListView<File> lwFiles;

	public ConverterController() { }

	@FXML
	private void initialize() {
		btnConvert.setOnAction(event -> convert());
		btnAddFiles.setOnAction(event -> addFiles());
		btnRemoveFiles.setOnAction(event -> removeFiles());
		btnClear.setOnAction(event -> clear());
		lwFiles.setCellFactory(param -> new FileListCell());
		lwFiles.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}

	/**
	 * Used when the user drags and drops files into the {@link ListView}
	 */
	@FXML
	private void dragAndDrop() {

	}

	/**
	 * tries to convert all files in the {@link ListView} to pdf(s)
	 */
	private void convert() {
		try {
			List<File> files = lwFiles.getItems();

			if (cbCombine.isSelected()) {
				if (FileUtil.containsOnlyImages(files)) {
					Converter.convertImagesToSinglePdf(files);
				}
			} else {
				if (FileUtil.containsOnlyImages(files)) {
					Converter.convertImagesToPdfs(files);
				} else {
					files.forEach(file -> {
						if (!FileUtil.isPdf(file)) {
							// a pdf cannot be converted to a pdf :)
							// images
							if (FileUtil.isImageFile(file)) {
								try {
									Converter.convertImageToPdf(file);
								} catch (MalformedURLException e) {
									e.printStackTrace();
								}
							}
						}
					});
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * opens the {@link FileChooser} and adds all selected files to the {@link ListView}
	 */
	private void addFiles() {
		FileChooser chooser = new FileChooser();

		List<File> files = chooser.showOpenMultipleDialog(null);
		lwFiles.getItems().addAll(files.toArray(new File[]{}));
	}

	/**
	 * removes all selected files in the {@link ListView}
	 */
	private void removeFiles() {
		System.out.println("REMOVE FILES");
		ObservableList<Integer> indices = lwFiles.getSelectionModel().getSelectedIndices();
		// maybe sort indices
		for (int i = indices.size() - 1; i >= 0; i--) {
			lwFiles.getItems().remove((int) indices.get(i));
		}
	}

	/**
	 * Clears the whole {@link ListView}
	 */
	private void clear() {
		lwFiles.getItems().clear();
	}

}
