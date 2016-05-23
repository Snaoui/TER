package org.qcmix.convert;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;
import org.qcmix.exceptions.ConversionException;
import org.qcmix.tools.TestHelpers;

public class UnoconvTest {

	@Test
	public void testConvertOdsToCsv() throws InterruptedException, ConversionException, FileNotFoundException {
		File inputOdsFile = TestHelpers.getRessourceFile("input - L2 bio.ods");
		File expectedOutputFile = TestHelpers.getRessourceFile("input - L2 bio_expected.csv");
		String convertedFilePath = Unoconv.convert(inputOdsFile, "csv", ".testConvertOdsToCsv_TEMP");
		File convertedFile = new File(convertedFilePath);

		TestHelpers.AssertTextFilesContentIsEqual(convertedFile, expectedOutputFile);
		TestHelpers.deleteFileFromPath(convertedFilePath);
	}
}
