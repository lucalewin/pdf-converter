package net.lucraft.converter;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.stage.FileChooser;
import net.lucraft.converter.config.Config;
import net.lucraft.converter.ui.FileListCell;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class ConverterController {

	@FXML private Button btnConvert;
	@FXML private Button btnAddFiles;
	@FXML private Button btnRemoveFiles;
	@FXML private Button btnClear;
	@FXML private CheckBox cbCombine;
	@FXML private ListView<File> lwFiles;

	@FXML private ContextMenu listViewContextMenu;
	@FXML private MenuItem listViewMenuItemDelete;

	public ConverterController() { }

	@FXML
	private void initialize() {
		btnConvert.setOnAction(event -> convert());
		btnAddFiles.setOnAction(event -> addFiles());
		btnRemoveFiles.setOnAction(event -> removeFiles());
		btnClear.setOnAction(event -> clear());
		lwFiles.setCellFactory(param -> new FileListCell());
		lwFiles.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		lwFiles.setOnDragOver(this::onDragOver);
		lwFiles.setOnDragDropped(this::onDragDropped);
		lwFiles.setOnDragExited(event -> lwFiles.setStyle("-fx-background-color: #fff"));
		listViewMenuItemDelete.setDisable(true);
	}

	/**
	 *
	 * @param event the {@link DragEvent}
	 */
	private void onDragOver(DragEvent event) {
		// check if files are dragged and if those are files or directories
		if (event.getDragboard().hasFiles() && event.getDragboard().getFiles().stream().allMatch(File::isFile)) {
			event.acceptTransferModes(TransferMode.COPY);
			lwFiles.setStyle("""
				-fx-border-insets: 5;
				-fx-border-width: 5px;
				-fx-border-color: #1bc124;
				-fx-border-style: dashed;
				-fx-border-radius: 7px;
				-fx-background-color: #c7f0bd;
				-fx-background-radius: 12px""");
		}
		event.consume();
	}

	private void onDragDropped(DragEvent event) {
		lwFiles.getItems().addAll(event.getDragboard().getFiles());
	}

	/**
	 * tries to convert all files in the {@link ListView} to pdf(s)
	 */
	private void convert() {
		try {
			List<File> files = lwFiles.getItems();

			if (cbCombine.isSelected()) {
				FileChooser chooser = new FileChooser();
				chooser.setInitialDirectory(new File(Config.getInstance().getDefaultFolder()));
				chooser.setInitialFileName(Config.getInstance().getDefaultFilename());
				chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF File", "*.pdf"));

				File out = chooser.showSaveDialog(null);

				if (out != null) {
					Converter.merge(files, out);
				}
			} else {
//				files.stream().filter(FileUtil::isImageFile).forEach(file -> );
				files.forEach(file -> {
					if (FileUtil.isImageFile(file)) {
						String filename = file.getAbsolutePath();
						File out = new File(filename.substring(0, filename.lastIndexOf('.')) + ".pdf");
						Converter.convertImageToPdf(Converter.convertFileToImage(file), out);
					}
				});
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
		chooser.setInitialDirectory(new File(Config.getInstance().getDefaultFolder()));
		chooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("Convertible Files", "*.png", "*.jpg", "*.jpeg", "*.pdf")
		);

		List<File> files = chooser.showOpenMultipleDialog(null);
		lwFiles.getItems().addAll(Objects.requireNonNull(files).toArray(new File[]{}));
	}

	/**
	 * removes all selected files in the {@link ListView}
	 */
	@FXML
	private void removeFiles() {
		ObservableList<Integer> indices = lwFiles.getSelectionModel().getSelectedIndices();
		// maybe sort indices
		for (int i = indices.size() - 1; i >= 0; i--) {
			lwFiles.getItems().remove((int) indices.get(i));
		}
		lwFiles.getSelectionModel().clearSelection();
	}

	/**
	 * Clears the whole {@link ListView}
	 */
	private void clear() {
		lwFiles.getItems().clear();
	}
}
