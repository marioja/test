package net.mfjassociates.diskspace;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.scene.control.TreeView;
import net.mfjassociates.diskspace.util.FXHelper.ResponsiveTask;

/**
 * This class contains helper functions to navigate the filesystem tree and calculate
 * cummulative disk space used.
 * @author mario
 *
 */
public class FileSystemHandler {
	public static void main(String[] args) throws IOException {
		TreeView<CummulativeFile> fsTreeView=new TreeView<CummulativeFile>();
		CummulativeFile a=createDir(args[0], fsTreeView);
		displayMyFile(a);
	}
	/**
	 * Display filesystem tree aFile
	 * @param aFile
	 */
	protected static void displayMyFile(CummulativeFile aFile) {
		System.out.print(aFile.getFileType()+aFile.getFile().getPath());
		if (aFile.getFile().isDirectory()) {
			System.out.println(",l:"+aFile.getLength());
			aFile.getFiles().stream().forEach(FileSystemHandler::displayMyFile);
		} else System.out.println();
	}
	public static CummulativeFile createDir(Path path, TreeView<CummulativeFile> fsTreeView) {
		return createDir(path.toString(), fsTreeView);
	}
	public static CummulativeFile createDir(String path, TreeView<CummulativeFile> fsTreeView) {
		CummulativeFile myDir = new CummulativeFile(null, Paths.get(path));
		Thread th=new Thread(new ResponsiveTask<Void>(){

			@Override
			protected Void call() throws Exception {
				handleDir(myDir);
				return null;
			}}.bindScene(fsTreeView.sceneProperty()));
		th.start();
		return myDir;
	}
	

	public static CummulativeFile handleDir(final CummulativeFile myDir) {
		try {
//			System.out.println("d:"+myDir.getFile().toPath());
			Files.newDirectoryStream(myDir.getFile().toPath()).forEach(p -> {
				if (p.toFile().isDirectory()) {
					// will connect treeItem in CummulativeFile constructor
					handleDir(new CummulativeFile(myDir, p));
				} else {
					// will connect treeItem in CummulativeFile constructor
					new CummulativeFile(myDir, p);
//					System.out.println("f:"+p);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return myDir;
	}

}
