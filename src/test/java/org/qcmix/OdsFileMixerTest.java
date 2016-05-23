package org.qcmix;


import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Test;
import org.qcmix.exceptions.BadFormatException;
import org.qcmix.tools.TestHelpers;

public class OdsFileMixerTest {

	@Test
	public void testMainWithValidFilePathShouldSucceed() {
		String inputFilePath = TestHelpers.getAbsolutePathOfResource("input - L2 bio.ods");
		String outputDirPath = TestHelpers.getTmpDir();

		File inputFile = new File(inputFilePath);
		try {
			OdsFileMixer.readFile(inputFile, outputDirPath);
		} catch (IOException | BadFormatException e) {
			fail();
		}
	}


	@After
	public void tearDown() throws IOException {
		TestHelpers.deleteFolderContent(new File("tmp"));
	}
}
