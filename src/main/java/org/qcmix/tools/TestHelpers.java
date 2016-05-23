package org.qcmix.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;


public class TestHelpers {


	private static final String RESOURCES_DIRECTORY = "src/test/resources/org/qcmix/";


	public static File getRessourceFile(String ressourcePath) {
		return new File(getAbsolutePathOfResource(ressourcePath));
	}

	public static String getAbsolutePathOfResource(String ressourcePath) {
		String workingDirectory = System.getProperty("user.dir") + "/";
		String absolutePath = workingDirectory + RESOURCES_DIRECTORY + ressourcePath;
		return absolutePath;
	}

	public static void deleteFileFromPath(String path) {
		new File(path).delete();
	}

	public static String getTmpDir() {
		String workingDirectory = System.getProperty("user.dir");
		return workingDirectory + "/tmp/";
	}


	public static void deleteFolderContent(File folder) {
		File[] files = folder.listFiles();
		if(files!=null) { //some JVMs return null for empty dirs
			for(File f: files) {
				if(f.isDirectory()) {
					deleteFolderContent(f);
					f.delete();
				} else {
					if(!f.getName().equals(".gitkeep")) {
						f.delete();
					}
				}
			}
		}
	}

	public static void AssertTextFilesContentIsEqual(File file1,
			File file2) throws FileNotFoundException {
		String file1Content = Helper.convertStreamToString(new FileInputStream(file1));
		String file2Content = Helper.convertStreamToString(new FileInputStream(file2));
		boolean contentsAreEquals = file1Content.equals(file2Content);

		if(!contentsAreEquals) {
			throw new AssertionError("Expected files to have same content:\n"
					+ file1 + "\nand\n" + file2);
		}
	}

	public static void assertOdsContentEqual(File file1, File file2) throws ZipException, IOException {
		ZipFile zfile1 = new ZipFile(file1);
		ZipFile zfile2 = new ZipFile(file2);

		InputStream inputStreamReader1 = zfile1.getInputStream(zfile1.getEntry("content.xml"));
		InputStream inputStreamReader2 = zfile2.getInputStream(zfile2.getEntry("content.xml"));

		String file1Content = Helper.convertStreamToString(inputStreamReader1);
		String file2Content = Helper.convertStreamToString(inputStreamReader2);

		inputStreamReader1.close();
		inputStreamReader2.close();
		zfile1.close();
		zfile2.close();

		if(!file1Content.equals(file2Content)) {
			throw new AssertionError("Expected files to have same content:\n"
					+ file1 + "\nand\n" + file2);
		}

	}

}
