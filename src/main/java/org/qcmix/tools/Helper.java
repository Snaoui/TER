package org.qcmix.tools;
import org.qcmix.tools.DesktopApiMultiplatform;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import java.awt.datatransfer.*;
import java.awt.Toolkit;

/**
 * <b>Classe Helper qui recense toutes les fonctions utilitaires générales au projet; </b>
 *
 * @author Simon et Dioulde
 */
public class Helper {


	public static String convertStreamToString(java.io.InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is);
		s.useDelimiter("\\A");
		String result = s.hasNext() ? s.next() : "";
		s.close();
		return result;
	}


	public static String getFileExtention(File file) {
		String fileName = file.getName();
		String extension = "";

		int i = fileName.lastIndexOf('.');
		if (i > 0) {
		    extension = fileName.substring(i+1);
		}
		return extension;
	}


	/**
	 * <b> Renvoie le fichier de src/test/ressources/org/qcmix/. </b>
	 *
	 * @param fileName : nom du fichier
	 * @return File : fichier correspondant
	 */
	public static File getResource(String fileName) {
		final String completeFileName = "src/test/resources/org/qcmix/" + fileName;
		File file = new File(completeFileName);
		return file;
	}

	/**
	 * <b> Renvoie vrai si la String représente un numérique. </b>
	 *
	 * @param str : chaine de caractères à tester
	 * @return boolean
	 */
	public static boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	/**
	 * <b> Renvoie vrai si la String représente une lettre. </b>
	 *
	 * @param str : chaine de caractères à tester
	 * @return boolean
	 */
	public static boolean isLetter(String str) {
		return str.matches("^[a-z]$");
	}



	/**
	 * <b> Récupère la stacktrace d'une erreur spécifique à QCMix. </b>
	 *
	 * @param throwable
	 * @return String
	 */
	public static String getStackTrace(final Throwable throwable) {
		 final StringWriter sw = new StringWriter();
		 final PrintWriter pw = new PrintWriter(sw, true);
		 throwable.printStackTrace(pw);
		 final String fullStackTrace = sw.getBuffer().toString();
		 return getQCMixTrace(fullStackTrace);
	}


	/**
	 * <b> A partir d'une stacktrace, renvoie les lignes concernant QCMix. </b>
	 *
	 * @param pFullStackTrace
	 * @return String
	 */
	private static String getQCMixTrace(String pFullStackTrace) {
		String returnedStringQCMix = "";
		boolean isQCMixPassed = false;
		String[] lines = pFullStackTrace.split("\n");
		for (String line: lines) {
			 if (! isQCMixPassed || line.contains("qcmix")) {
				 if (line.contains("qcmix")) {
					 isQCMixPassed = true;
				 }
				 returnedStringQCMix += line + "\n";
			 }
		}
		return returnedStringQCMix;
	}


	public static void openWebPage(String urlString) {
		Boolean success = null;
		try {
			success = DesktopApiMultiplatform.browse(new URL(urlString).toURI());
			System.out.println("Link openned sucessfully. ");
		} catch (MalformedURLException | URISyntaxException e) {
			System.err.println("There is problem with the url:" + urlString);
			e.printStackTrace();
		}
		if (!success) {
			throw new UnsupportedOperationException("Failure when openning web page");
		}
	}


	public static void copyToClipBoard(String myString) {
		StringSelection stringSelection = new StringSelection(myString);
		Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		clpbrd.setContents(stringSelection, null);
	}

	public static String removeExtentionFromFileName(String name) {
		return name.replaceFirst("[.][^.]+$", "");
	}
}
