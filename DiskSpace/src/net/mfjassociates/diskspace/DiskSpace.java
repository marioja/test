package net.mfjassociates.diskspace;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
//import javafx.scene.control.ContextMenu;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class DiskSpace extends Application {
	
	DiskSpaceController diskSpaceController;
	
	private Object createControllerForType(Class<?> type) {
		if (diskSpaceController==null) {
			diskSpaceController = new DiskSpaceController();
		}
		return diskSpaceController;
	}
	@Override
	public void start(Stage primaryStage) {
		try {
//			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("ImageUtil.fxml"));
			FXMLLoader loader=new FXMLLoader(getClass().getResource("DiskSpace.fxml"));
			loader.setControllerFactory(this::createControllerForType);
			BorderPane root = loader.load();
			DiskSpaceController iuc=(DiskSpaceController) loader.getController();
//			loader=new FXMLLoader(getClass().getResource("LeftContextMenu.fxml"));
//			loader.setControllerFactory(this::createControllerForType);
//			ContextMenu leftContextMenu = loader.load();
//			loader=new FXMLLoader(getClass().getResource("RightContextMenu.fxml"));
//			loader.setControllerFactory(this::createControllerForType);
//			ContextMenu rightContextMenu = loader.load();
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Java Advanced Imaging Utility");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
