package net.mfjassociates.ui.markers;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

public class XMLMarkersNature implements IProjectNature {
	
	private IProject project;

	@Override
	public void configure() throws CoreException {
		IProjectDescription description = this.project.getDescription();

		// Add a Java command to the build spec
		ICommand command = description.newCommand();
		command.setBuilderName("net.mfjassociates.ui.markers.xmlbookmarksbuilder");
		ICommand[] oldBuildSpec = description.getBuildSpec();
		ICommand[] newCommands;

		// Add a Java build spec before other builders (1FWJK7I)
		newCommands = new ICommand[oldBuildSpec.length + 1];
		System.arraycopy(oldBuildSpec, 0, newCommands, 1, oldBuildSpec.length);
		newCommands[0] = command;

		// Commit the spec change into the project
		description.setBuildSpec(newCommands);
		this.project.setDescription(description, null);

	}

	@Override
	public void deconfigure() throws CoreException {
		// TODO Auto-generated method stub

	}

	@Override
	public IProject getProject() {
		return project;
	}

	@Override
	public void setProject(IProject aProject) {
		this.project=aProject;

	}

}
