package net.mfjassociates.diskspace.sample;

import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeItem.TreeModificationEvent;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.EventHandler;

public class FxTreeViewExample2 extends Application
{
	// Create the TextArea
	private final TextArea textArea = new TextArea();

	public static void main(String[] args)
	{
		Application.launch(args);
	}

	@Override
	public void start(Stage stage)
	{
		// Create the TreeViewHelper
		TreeViewHelper helper = new TreeViewHelper();
		// Get the Products
		ArrayList<TreeItem<String>> products = helper.getProducts();

		// Create the TreeView
		TreeView<String> treeView = new TreeView<>();
		// Create the Root TreeItem
		TreeItem<String> rootItem = new TreeItem<String>("Vehicles");
		// Add children to the root
		rootItem.getChildren().addAll(products);
		// Set the Root Node
		treeView.setRoot(rootItem);

		// Set tree modification related event handlers (branchExpandedEvent)
		rootItem.addEventHandler(TreeItem.<String>branchExpandedEvent(),new EventHandler<TreeItem.TreeModificationEvent<String>>()
		{
			@Override
			public void handle(TreeModificationEvent<String> event)
			{
				branchExpended(event);
			}
		});

		// Set tree modification related event handlers (branchCollapsedEvent)
		rootItem.addEventHandler(TreeItem.<String>branchCollapsedEvent(),new EventHandler<TreeItem.TreeModificationEvent<String>>()
		{
			@Override
			public void handle(TreeModificationEvent<String> event)
			{
				branchCollapsed(event);
			}
		});

		// Set the preferred number of text rows
		textArea.setPrefRowCount(20);
		// Set the preferred number of text columns
		textArea.setPrefColumnCount(25);

		// Create the VBox
		VBox root = new VBox();
		// Add the TreeView to the HBox
		root.getChildren().addAll(treeView,new Label("Message Log:"), textArea);

		// Create the Scene
		Scene scene = new Scene(root,400,500);
		// Add the Scene to the Stage
		stage.setScene(scene);
		// Set the Title for the Scene
		stage.setTitle("TreeView Example 2");
		// Display the stage
		stage.show();
	}

	// Helper Methods for the Event Handlers
	private void branchExpended(TreeItem.TreeModificationEvent<String> event)
	{
		String nodeValue = event.getSource().getValue().toString();
		this.writeMessage("Node " + nodeValue + " expanded.");
	}

	private void branchCollapsed(TreeItem.TreeModificationEvent<String> event)
	{
		String nodeValue = event.getSource().getValue().toString();
		this.writeMessage("Node " + nodeValue + " collapsed.");
	}

	// Method for Logging
	private void writeMessage(String msg)
	{
		this.textArea.appendText(msg + "\n");
	}

}
