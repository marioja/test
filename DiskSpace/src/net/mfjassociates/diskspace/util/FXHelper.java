package net.mfjassociates.diskspace.util;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.concurrent.Task;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;

public class FXHelper {

	/**
	 * This class will perform automatic behaviour depending on the following usage.
	 * If you call bindScene, the cursor for the scene will bet set to wait when the
	 * task is scheduled and will be set back to the default when the task succeds.
	 * 
	 * If the call bindProgressBar, the progress bar will become visible when the task is
	 * schedule and become invisible when it is finished.
	 * @author mxj037
	 *
	 * @param <T>
	 */
	public static abstract class ResponsiveTask<T> extends Task<T> {
		
		private boolean handlerActivated=false;

		public Scene getScene() {
			return scene == null ? null : scene.get();
		}

		public void setScene(Scene scene) {
			sceneProperty().set(scene);
		}

		public ProgressBar getProgressBar() {
			return progressBar == null ? null : progressBar.get();
		}

		public void setProgressBar(ProgressBar aProgressBar) {
			progressBarProperty().set(aProgressBar);
		}

		public ResponsiveTask<T> bindScene(ReadOnlyObjectProperty<Scene> aSceneProperty) {
			aSceneProperty.addListener((obs, oldv, newv) -> setCursorEventHandler());
			if (aSceneProperty.get() != null) {
				setCursorEventHandler();
			}
			sceneProperty().bind(aSceneProperty);
			return ResponsiveTask.this;
		}

		public ResponsiveTask<T> bindProgressBar(ReadOnlyObjectProperty<ProgressBar> aProgressBarProperty) {
			aProgressBarProperty.addListener((obs, oldv, newv) -> setCursorEventHandler());
			if (aProgressBarProperty.get() != null) {
				setCursorEventHandler();
			}
			progressBarProperty().bind(aProgressBarProperty);
			return ResponsiveTask.this;
		}

		private void setCursorEventHandler() {
			if (!handlerActivated) {
				setOnScheduled(event -> {
					if (sceneProperty().get()!=null) sceneProperty().get().setCursor(Cursor.WAIT);
					ProgressBar pb = progressBarProperty().get();
					if (pb!=null) {
						pb.setProgress(0d);
						pb.setVisible(true);
					}
				});
				setOnSucceeded(event -> {
					if (sceneProperty().get()!=null) sceneProperty().get().setCursor(Cursor.DEFAULT);
					ProgressBar pb = progressBarProperty().get();
					if (pb!=null) {
						pb.setVisible(false);
					}
				});
			}
			handlerActivated=true;
		}

		private ObjectProperty<Scene> sceneProperty() {
			if (scene == null) {
				scene = new ObjectPropertyBase<Scene>(null) {

					@Override
					public Object getBean() {
						return ResponsiveTask.this;
					}

					@Override
					public String getName() {
						return "sceneProperty";
					}
				};
			}
			return scene;
		}

		private ObjectProperty<Scene> scene;

		private ObjectProperty<ProgressBar> progressBarProperty() {
			if (progressBar == null) {
				progressBar = new ObjectPropertyBase<ProgressBar>(null) {

					@Override
					public Object getBean() {
						return ResponsiveTask.this;
					}

					@Override
					public String getName() {
						return "progressBarProperty";
					}
				};
			}
			return progressBar;
		}

		private ObjectProperty<ProgressBar> progressBar;
	}
}
