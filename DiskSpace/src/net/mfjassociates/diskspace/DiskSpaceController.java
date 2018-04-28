package net.mfjassociates.diskspace;

import static net.mfjassociates.diskspace.FileSystemHandler.createDir;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;

public class DiskSpaceController {
	
	@FXML private VBox treeViewVbox;
	@FXML private TreeView<CummulativeFile> fsTreeView;
	private CummulativeFile rootFile=createDir(".");
	
	@FXML private void initialize() {
		fsTreeView.setRoot(rootFile.getTreeItem());
	}
	@FXML private void closeFired(ActionEvent event) throws IOException {
	}
	@FXML private void openFired(ActionEvent event) throws IOException {
	}
	@FXML private void saveFired(ActionEvent event) throws IOException {
	}
	@FXML private void saveAsFired(ActionEvent event) throws IOException {
	}
	@FXML private void preferencesFired(ActionEvent event) throws IOException {
	}
	@FXML private void copyBase64Fired(ActionEvent event) throws IOException {
	}
	@FXML private void pasteBase64Fired(ActionEvent event) throws IOException {
	}
	@FXML private void base64Fired(ActionEvent event) throws IOException {
	}
	@FXML private void metadataFired(ActionEvent event) throws IOException {
	}
	@FXML private void aboutFired(ActionEvent event) throws IOException {
	}
}
