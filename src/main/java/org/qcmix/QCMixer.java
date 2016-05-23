package org.qcmix;

import java.io.File;
import java.io.IOException;

import org.qcmix.convert.Unoconv;
import org.qcmix.exceptions.BadFormatException;
import org.qcmix.exceptions.ConversionException;
import org.qcmix.tools.Helper;

public class QCMixer {

	/**
	 * Mix a QCM independently of the file format. (It must be a spreadsheet of course!)
	 * @param inputFilePath
	 * @param outputDirPath
	 */
	public static void mix(String inputFilePath, String outputDirPath) throws InterruptedException, ConversionException, IOException, BadFormatException {
		File inputFile = new File(inputFilePath);
		String extention = Helper.getFileExtention(inputFile);
		File fileToMix = inputFile;
		if (!extention.equals("ods")) {
			String convertedFilePath = Unoconv.convert(inputFile, "ods", ".converti");
			fileToMix = new File(convertedFilePath);
		}
		OdsFileMixer.readFile(fileToMix, outputDirPath);
	}
}
