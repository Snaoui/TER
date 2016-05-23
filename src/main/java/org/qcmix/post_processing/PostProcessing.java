package org.qcmix.post_processing;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Apply some regexes to "content.xml" to fix XML tree after jOpendocument
 * moves a cell (it can't move a cell), remove colors, etc. See public methods.
 * See the wiki for more informations.
 * @author Gabriel
 */
public class PostProcessing {

	private String contentXMLFile = new String();
	private String nameFile = new String();


	/* Constructeur - Prend le nom du fichier ODS à traiter */
	/* Remplie l'attribut contentXMLFile avec le contenu du fichier ODS (plus exactement, le contenu de content.xml de l'archive ODS */


	public PostProcessing(String odsFile) throws IOException {

		String currentLine = null;
		this.nameFile = odsFile;
		try {
			ZipFile file = new ZipFile(odsFile);
			try {
				InputStream inputStreamReader = file.getInputStream(file.getEntry("content.xml"));

				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStreamReader));

				try {
					while ((currentLine = bufferedReader.readLine()) != null) {
						contentXMLFile += currentLine;
					}

				} catch (IOException e) {
					throw new IOException("Une erreur d'entrée/sortie est survenue lors de la lecture du contenu du fichier XML"+e.getStackTrace());

				}

				bufferedReader.close();
				inputStreamReader.close();
			} catch (FileNotFoundException e) {
				throw new FileNotFoundException("Fichier XML introuvable - PostProcessing abandonné\n"+e.getStackTrace());
				
			}
			file.close();
		} catch (IOException e) {
			throw new IOException("Une erreur d'entrée/sortie est survenue lors de la lecture de l'archive ODS - PostProcessing abandonné"+e.getStackTrace());
			

		}

	}

	/**
	 * Runs all post processing options.
	 * @param optionFlags see {@link PostProcessingOptions}
	 * @throws IOException 
	 */
	public void process(int optionFlags) throws IOException {

		if ((optionFlags & PostProcessingOptions.REMOVE_ALL_COLORS) != 0) {
			contentXMLFile = removeAllColors(contentXMLFile);
		}
		if ((optionFlags & PostProcessingOptions.REMOVE_CELLS_STYLE) != 0) {
			contentXMLFile = removeCellsStyle(contentXMLFile);
		}
		contentXMLFile = removeRedundantXMLTag(contentXMLFile);
		contentXMLFile = escapeXmlSpecialChars(contentXMLFile);
		reSaveFile();
	}


	/**
     * See {@link PostProcessingOptions#REMOVE_ALL_COLORS}
	 */
	protected static String removeAllColors(String str) {
		return str.replaceAll("<style:text-properties fo:color=\"#([0-9a-fA-F]){6}\"",
				"<style:text-properties fo:color=\"#000000\"");
	}

	/* Traite le contenu du fichier ODS - Supprime les balises redondantes. */
	protected static String removeRedundantXMLTag(String str) {
		return str.replaceAll(
				"<table(?:[a-zA-Z0-9: .\\-=/\"])*><text:p>&lt;(.+?)&gt;</text:p></table(?:[a-zA-Z0-9: .\\-=/\"])*>",
				"&lt;$1&gt;");
	}

	/* Traite le contenu du fichier ODS -
	 * Remplace les caractères de balises XML tronqués précédemment lors de l'écriture du fichier ODS par des chevrons. */
	protected static String escapeXmlSpecialChars(String str) {
		String result;
		result = str.replaceAll("&lt;", "<");
		result = result.replaceAll("&gt;", ">");
		return result;

	}

	/**
	 * See {@link PostProcessingOptions#REMOVE_CELLS_STYLE}
	 */
	protected static String removeCellsStyle(String str) {
		 return str.replaceAll("table:style-name=\"ce([0-9])*\"", "");
	}

	/* Sauvegarde les modifications
	 * Crée un fichier temporaire, puis le renomme après avoir supprimé le fichier initial.
	 */

	private void reSaveFile() throws IOException {
		String tmpName = this.nameFile + ".tmpqcm";
		File tmp = new File(tmpName);
		File origin = new File(this.nameFile);

		try {
			copyFile(origin, tmp);
		} catch (IOException e) {
			throw new IOException("Erreur d'entrée/sortie sur le fichier temporaire lors du PostProcessing. Abandon.\n"+e.getStackTrace());
		} finally {
			origin.delete();
			tmp.renameTo(origin);
		}

	}

	/* Copie une archive */

	private void copyFile(File zipFile, File newFile) throws IOException {
		ZipFile zipSrc = new ZipFile(zipFile);
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(newFile));
		zos.putNextEntry(new ZipEntry("content.xml"));
		zos.write(this.contentXMLFile.getBytes());
		zos.closeEntry();

		Enumeration<? extends ZipEntry> srcEntries = zipSrc.entries();
		while (srcEntries.hasMoreElements()) {
			ZipEntry entry = srcEntries.nextElement();
			ZipEntry newEntry = null;
			if (entry.getName().equals("content.xml")) {
				continue;
			} else {
				newEntry = new ZipEntry(entry.getName());
				zos.putNextEntry(newEntry);
			}

			BufferedInputStream bis = new BufferedInputStream(zipSrc.getInputStream(entry));

			while (bis.available() > 0) {
				zos.write(bis.read());
			}
			zos.closeEntry();

			bis.close();
		}
		zos.finish();
		zos.close();
		zipSrc.close();
	}



}
