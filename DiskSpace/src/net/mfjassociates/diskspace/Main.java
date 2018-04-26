package net.mfjassociates.diskspace;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
	public static void main(String[] args) throws IOException {
		MyFile a=handleDir(args[0]);
		handleFile(a);
	}
	private static void handleFile(MyFile aFile) {
		System.out.print(aFile.getFileType()+aFile.getFile().getPath());
		if (aFile.getFile().isDirectory()) {
			System.out.println(",l:"+aFile.getLength());
			aFile.getFiles().stream().forEach(Main::handleFile);
		} else System.out.println();
	}
	private static MyFile handleDir(String path) throws IOException {
		return handleDir(null, Paths.get(path));
	}
	private static MyFile handleDir(final MyFile parent, Path dir) {
		final MyFile myDir;
		MyFile reportDir=null;
		try {
//			System.out.println("d:"+dir);
			myDir=new MyFile(parent, dir);
			reportDir=myDir;
			Files.newDirectoryStream(dir).forEach(p -> {if (p.toFile().isDirectory()) {handleDir(myDir, p);} else { handleFile(myDir, p);}});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return reportDir;
	}
	private static void handleFile(MyFile parent, Path file) {
		MyFile myFile=new MyFile(parent, file);
//		System.out.println("f:"+file);
	}

}
