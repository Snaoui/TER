package org.qcmix.convert;

import java.io.File;
import org.qcmix.exceptions.ConversionException;
import org.qcmix.exceptions.ExternalCommandErrorException;
import org.qcmix.tools.CommandRunner;

public class Unoconv {

	/**
	 * Wrapper for Unoconv: a python program which uses LibreOffice/OpenOffice to
	 * convert any common spreadsheet format to ODS.
	 * @param fileToConvert
	 * @param format (output), see <a href="https://github.com/dagwieers/unoconv/blob/2f1c63b2df03376b1ac9d3ed9eb4adec621d6b6e/unoconv#L400">unoconv format list</a>
	 * @param nameSuffix , something to add after the file name, usefull for temporary files or test files(to know which test generated a file)
	 * @throws InterruptedException
	 * @throws ConversionException
	 */
	public static String convert(File fileToConvert, String format, String nameSuffix) throws InterruptedException, ConversionException {
		String filePath = fileToConvert.getAbsolutePath();
		String convertedFilePath = filePath + nameSuffix + "." + format;
		String pathPython;
		if(System.getProperty("os.name").toLowerCase().contains("win")){
			pathPython = "C:\\Program Files (x86)\\OpenOffice 4\\program\\python";
		}else{
			pathPython = "python3";
		}
		String[] unoconv_command = {pathPython, "unoconv.py", "-o", convertedFilePath, "-f", format, filePath};
		try {
			CommandRunner.run(unoconv_command);
		} catch (ExternalCommandErrorException e) {
			throw new ConversionException(e.getMessage());
		}
		System.out.println("fichier converti");
		return convertedFilePath;
	}



}
