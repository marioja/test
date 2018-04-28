package net.mfjassociates.diskspace;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class contains helper functions to navigate the filesystem tree and calculate
 * cummulative disk space used.
 * @author mario
 *
 */
public class FileSystemHandler {
	public static void main(String[] args) throws IOException {
		CummulativeFile a=createDir(args[0]);
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
	public static CummulativeFile createDir(String path) {
		return createDir(null, Paths.get(path));
	}
	public static CummulativeFile createDir(final CummulativeFile parent, Path dir) {
		final CummulativeFile myDir;
		CummulativeFile reportDir=null;
		try {
//			System.out.println("d:"+dir);
			myDir=new CummulativeFile(parent, dir);
			reportDir=myDir;
			Files.newDirectoryStream(dir).forEach(p -> {if (p.toFile().isDirectory()) {createDir(myDir, p);} else { createFile(myDir, p);}});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return reportDir;
	}
	public static void createFile(CummulativeFile parent, Path file) {
		CummulativeFile myFile=new CummulativeFile(parent, file);
//		System.out.println("f:"+file);
	}

}
