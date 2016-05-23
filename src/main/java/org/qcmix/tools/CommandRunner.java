package org.qcmix.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.qcmix.exceptions.ExternalCommandErrorException;

public class CommandRunner {

	public static void run(String[] command) throws InterruptedException, ExternalCommandErrorException {
		ProcessBuilder pb = new ProcessBuilder(command);
		Process unoconv = null;
		try {
			unoconv = pb.start();
		} catch (IOException e) {
			String errorMessage = "It seems that the program that you called was not found, check your command:\n" + Arrays.toString(command);
			throw new ExternalCommandErrorException(errorMessage);
		}

		int returnCode = unoconv.waitFor();
		if (returnCode != 0) {
			String stdErrOutput = getStdErrContentFromProcess(unoconv);
			String message = "Failed:\n" + Arrays.toString(command) + "\n" + stdErrOutput;
			throw new ExternalCommandErrorException(message);
		}
	}


	private static String getStdErrContentFromProcess(Process process) {
			InputStream stdErrStream = process.getErrorStream();
			String stdErrContent = Helper.convertStreamToString(stdErrStream);
			try {
				stdErrStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return stdErrContent;
	}

}
