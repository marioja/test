package net.mfjassociates.diskspace.util;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.concurrent.Task;
import javafx.scene.Cursor;
import javafx.scene.Scene;

public class FXHelper {

	public static abstract class ResponsiveTask<T> extends Task<T> {

		public Scene getScene() {
			return scene == null ? null : scene.get();
		}

		public void setScene(Scene scene) {
			sceneProperty().set(scene);
		}

		public ResponsiveTask<T> bindScene(ReadOnlyObjectProperty<Scene> sceneProperty) {
			sceneProperty.addListener((obs, oldv, newv) -> setCursorEventHandler());
			if (sceneProperty.get() != null) {
				setCursorEventHandler();
			}
			sceneProperty().bind(sceneProperty);
			return ResponsiveTask.this;
		}

		private void setCursorEventHandler() {
			setOnScheduled(event -> {
				sceneProperty().get().setCursor(Cursor.WAIT);
			});
			setOnSucceeded(event -> {
				sceneProperty().get().setCursor(Cursor.DEFAULT);
			});
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

	}
}
