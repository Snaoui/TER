package org.qcmix;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.qcmix.exceptions.BadFormatException;
import org.qcmix.exceptions.ConversionException;
import org.qcmix.mixer.Mixer;
import org.qcmix.tools.Helper;
import org.qcmix.tools.TestHelpers;

public class QCMixerTest {

	@Test
	public void testMixWithOdsFile() throws InterruptedException, ConversionException, IOException, BadFormatException {
		Mixer.seed = 42;
		String inputFileName = "input - L2 bio.ods";
		String inputOdsFilePath = TestHelpers.getAbsolutePathOfResource(inputFileName);
		File expectedFilesFolder = TestHelpers.getRessourceFile(inputFileName + "_expected_output");

		QCMixer.mix(inputOdsFilePath, TestHelpers.getTmpDir());
		File resultFolder = new File(TestHelpers.getTmpDir() + Helper.removeExtentionFromFileName(inputFileName));

		//File[] expectedFiles  = expectedFilesFolder.listFiles();
		//File[] resultFiles = resultFolder.listFiles();

		/*for(int i=0; i<expectedFiles.length; i++) {
			String expectedFileName = expectedFiles[i].getName();
			// il faut prendre le fichier qui a le même nom dans les résultats et dans les ressources de test
			int j = 0;
			while (! expectedFileName.equals(resultFiles[j].getName())) {
				j += 1;
			}
			
			TestHelpers.assertOdsContentEqual(expectedFiles[i], resultFiles[j]);
		}*/


		TestHelpers.deleteFolderContent(resultFolder);
		resultFolder.delete();

	}

}
