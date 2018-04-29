package net.mfjassociates.diskspace;

import static net.mfjassociates.diskspace.FileSystemHandler.createDir;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;



import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import net.mfjassociates.diskspace.util.FXHelper.ResponsiveTask;

public class DiskSpaceController {
	
	@FXML private VBox treeViewVbox;
	@FXML private TreeView<CummulativeFile> fsTreeView;
	private CummulativeFile rootFile;
	
	public void setRootFile(CummulativeFile aRootFile) {
		this.rootFile=aRootFile;
	}
	
	@FXML private void initialize() {
		final CummulativeFile finalRootFile;
		Thread th=new Thread(new ResponsiveTask<Void>(){

			@Override
			protected Void call() throws Exception {
				Platform.runLater(() -> {
					CummulativeFile rootFile=createDir("../..");
					TreeItem<CummulativeFile> rootItem = rootFile.getTreeItem();
//					fsTreeView.setCellFactory(tv -> {return null;});
					rootFile.addEventHandler(rootItem); // add event handler only to root since all events bubble back to root
					fsTreeView.setRoot(rootItem);
					DiskSpaceController.this.setRootFile(rootFile);
				});
				return null;
			}}.bindScene(fsTreeView.sceneProperty()));
		th.start();
	}
	@FXML private void closeFired(ActionEvent event) throws IOException {
	}
	@FXML private void openFired(ActionEvent event) throws IOException {
		System.out.println(fsTreeView.getCellFactory());
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
