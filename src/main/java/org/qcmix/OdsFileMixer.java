package org.qcmix;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Pattern;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;
import org.omg.CORBA.Environment;
import org.qcmix.exceptions.BadFormatException;
import org.qcmix.mixer.Mixer;
import org.qcmix.post_processing.PostProcessing;
import org.qcmix.post_processing.PostProcessingOptions;
import org.qcmix.tools.Helper;

import java.awt.Color;

/**
 * <b>Classe qui s'occupe des fichiers de format .ODS</b>
 *
 * <p>
 * La classe se divise en 3 parties :
 * <ul>
 * <li>Ouvrir le fichier .ODS</li>
 * <li>Lire le fichier .ODS pour recenser les réponses de chaque questions du
 * QCM.</li>
 * <li>Pour chaque question lue on va mélanger les réponses associées et
 * produire plusieurs versions différentes.</li>
 * <li>On écrit au fur et à mesure dans les différentes versions pour ne pas
 * avoir à reparcourir le fichier une seconde fois pour l'écriture.</li>
 * <li>Après avoir lu toutes les questions du QCM, on sauvegarde les différentes
 * versions produites par le mélange.</li>
 * </ul>
 *
 * <p>
 * Pour paramétrer le nombre de versions du QCM que l'on produit, il faut
 * modifier la variable static nb_copies.
 * </p>
 *
 * @author Simon et Dioulde
 */
public class OdsFileMixer {

	private static final int MAX_ROWS_IN_QCM = 1000;

	// Liste des réponses de la question en cours de lecture
	private static ArrayList<Object> reponses_input;

	// Liste des versions du QCM que l'on produit
	private static ArrayList<Sheet> sheets;

	// Indice des questions
	private static int indice;

	// Boolean qui indique si l'on se trouve dans une question
	private static boolean inQuestion;

	// Nombre de copies différentes du QCM que l'on veut produire
	private static int nb_copies = 4;

	/**
	 * <b> Fonction principale qui va lire, mélanger et produire les différentes
	 * versions du QCM </b>
	 *
	 * @param file
	 *            : Fichier qui contient le QCM au format .ODS
	 * @param outputDirPath
	 *            : Chemin du dossier de destination des QCMs
	 * @throws IOException
	 * @throws BadFormatException
	 */
	public static void readFile(File file, String outputDirPath) throws IOException, BadFormatException {
		reponses_input = new ArrayList<Object>();

		indice = 0;

		inQuestion = false;

		Sheet sheet;

		// On charge le fichier, et on prend la première feuille de calcul
		try {
			sheet = SpreadSheet.createFromFile(file).getSheet(0);
			checkSourceSheetFormat(sheet);
		} catch (NullPointerException | ClassCastException e) {
			throw new NullPointerException(
					"Le fichier demandé n'est pas une feuille de calcul!\n\n" + Helper.getStackTrace(e));
		}
		File f = new File(outputDirPath, "simple-txt.txt");
		try {
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f)));

			// on crée 4 copies (par défaut)
			sheets = genereSheets(file, nb_copies);

			int cpt_ligne = 0; // compteur de lignes du fichier ods

			int cpt_ligne_b = 0; // compteur de lignes du fichier ods "Contenant
									// 2 colonnes"

			// nombre de ligne de la feuille de calcul
			int rowCount = sheet.getRowCount();

			// Tant qu'on a pas tout parcouru le fichier
			// Première boucle pour le premier bloc de questions dans une
			// première colonne

			while (cpt_ligne < rowCount && cpt_ligne < MAX_ROWS_IN_QCM) {

				// on lit la valeur de la cellule à la colonne 0 et à la ligne
				// cpt_ligne
				String cell_text = sheet.getCellAt(0, cpt_ligne).getTextValue();

				// si on trouve un numero de question
				if (checkQuestion(cell_text, indice, inQuestion)) {

					if (sheet.getCellAt(1, cpt_ligne).getTextValue().equals(""))
						throw new BadFormatException("Veuillez vérifier votre modèle de questionnaire");

					else {
						// On écrit la question dans le fichier AMC-TXT
						pw.println();
						pw.println("* " + sheet.getCellAt(1, cpt_ligne).getTextValue());
					}
				}

				// si on trouve une lettre de réponse

				if (checkReponse(sheet, cell_text, reponses_input, cpt_ligne, inQuestion)) {

					// On vérifie que la réponse s'elle est rouge = correcte
					if (sheet.getStyleAt(1, cpt_ligne).getTextProperties().getColor().equals(Color.red)) {
						// On écrit la reponse dans le fichier AMC-TXT

						pw.println("+ " + sheet.getCellAt(1, cpt_ligne).getTextValue());
					} else
						pw.println("- " + sheet.getCellAt(1, cpt_ligne).getTextValue());

				}

				// si on trouve une cellule vide
				try {
					checkEmpty(cell_text, reponses_input, cpt_ligne, inQuestion);
				} catch (IllegalArgumentException e) {
					throw new BadFormatException(
							"Le nombre de réponses possibles à cette question est insuffisante : ligne ~ " + cpt_ligne);
				}

				// increment
				cpt_ligne++;
			}
			// Une autre boucle pour le deuxième bloc de questions dans une
			// deuxieme colonne

			while (cpt_ligne_b < rowCount && cpt_ligne_b < MAX_ROWS_IN_QCM) {

				String cell_text2 = sheet.getCellAt(2, cpt_ligne_b).getTextValue();

				// si on trouve un numero de question
				if (checkQuestion(cell_text2, indice, inQuestion)) {

					// On écrit la question dans le fichier AMC-TXT

					if (sheet.getCellAt(3, cpt_ligne_b).getTextValue().equals(""))
						throw new BadFormatException("Veuillez vérifier votre modèle de questionnaire");
					else {
						pw.println();
						pw.println("* " + sheet.getCellAt(3, cpt_ligne_b).getTextValue());
					}
				}
				// si on trouve une lettre de réponse

				if (checkReponse(sheet, cell_text2, reponses_input, cpt_ligne_b, inQuestion)) {
					// On vérifie que la réponse s'elle est rouge = correcte
					if (sheet.getStyleAt(3, cpt_ligne_b).getTextProperties().getColor().equals(Color.red)) {
						// On écrit la reponse dans le fichier AMC-TXT
						pw.println("+ " + sheet.getCellAt(3, cpt_ligne_b).getTextValue());
					} else
						pw.println("- " + sheet.getCellAt(3, cpt_ligne_b).getTextValue());

				}

				// si on trouve une cellule vide
				try {
					checkEmpty(cell_text2, reponses_input, cpt_ligne_b, inQuestion);
				} catch (IllegalArgumentException e) {
					throw new BadFormatException(
							"Le nombre de réponses possibles à cette question est insuffisante : ligne ~ "
									+ cpt_ligne_b);
				}

				cpt_ligne_b++;
			}

			pw.close();
		} catch (IOException exception) {
			System.out.println("Erreur lors de la lecture : " + exception.getMessage());
		}

	}

	/**
	 * Fonction qui gère la lecture d'une cellule question (chiffre)
	 * 
	 * @param cell_text
	 *            : la cellule du tableur
	 * @param indice
	 *            : indice de la question
	 * @param inQuestion
	 */
	public static boolean checkQuestion(String cell_text, int indice, boolean inQuestion) {
		// si c'est un numéro de question et que l'on est pas déjà dans une
		// question
		if (Helper.isNumeric(cell_text) && !inQuestion) {
			// si le numéro est consécutif à celui de la question précédente
			if (Integer.parseInt(cell_text) == indice + 1) {
				// on notifie que l'on a trouvé une question
				setInQuestion(true);
				incIndice();
				return true;
			}
		}
		return false;
	}

	/**
	 * Fonction qui gère la lecture d'une cellule réponse (lettre)
	 * 
	 * @param sheet
	 * @param cell_text
	 * @param reponses_input
	 * @param cpt_ligne
	 * @param inQuestion
	 */
	public static boolean checkReponse(Sheet sheet, String cell_text, ArrayList<Object> reponses_input, int cpt_ligne,
			boolean inQuestion) {
		boolean flag = false;
		// si c'est une lettre de réponse et que l'on est déjà dans une question
		if (Helper.isLetter(cell_text) && inQuestion) {
			// on ajoute à la liste des réponses de la question
			reponses_input.add(sheet.getCellAt(1, cpt_ligne));
			flag = true;

		}
		return flag;
	}

	/**
	 * Fonction qui gère la lecture d'une cellule vide
	 * 
	 * @param cell_text
	 *            : la cellule qu'on lit
	 * @param reponses_input
	 *            : la liste de réponses de la question
	 * @param cpt_ligne
	 *            : ligne de la feuille de calcul
	 * @param inQuestion
	 */
	private static void checkEmpty(String cell_text, ArrayList<Object> reponses_input, int cpt_ligne,
			boolean inQuestion) {
		// une cellule vide indique que l'on passe à une question différente
		if (cell_text == "" && inQuestion) {
			// on génère nb_copie de QCMs
			ArrayList<ArrayList<Object>> reponses_output = genereMix(reponses_input, nb_copies);

			// on écrit le résultat du mixer dans les nb_copies versions du QCMs
			write(reponses_output, cpt_ligne);

			// on reset la liste des réponses pour la question suivante
			reset_reponses_input();

			setInQuestion(false);
		}
	}

	/**
	 * <b>Fonction qui, à partir d'une liste de réponses de QCM (String), va
	 * faire nb_version mélanges différents.</b>
	 * 
	 * @see Mixer
	 * @param list
	 *            : liste des réponses d'une question
	 * @param nb_version
	 *            : nombre de mélanges de réponses que l'on souhaite
	 * @return ArrayList<ArrayList<Object>> : liste de listes de réponses
	 *         mélangées
	 */
	private static ArrayList<ArrayList<Object>> genereMix(ArrayList<Object> list, int nb_version) {
		// si la liste de réponses n'est pas vide
		if (list.size() != 0) {
			// on génère nb_version mélanges différents
			return Mixer.generateSheets(list, nb_version);
		}
		return null;
	}

	/**
	 * <b> Fonction qui écrit sur les nb_copies versions les différents ordres
	 * de réponses d'une question. </b>
	 * 
	 * @param resultat
	 *            : Le résultat de genereMix()
	 * @param cpt_ligne
	 *            : l'indice de la ligne où l'on se trouve dans la feuille de
	 *            calcul
	 */
	private static void write(ArrayList<ArrayList<Object>> resultat, int cpt_ligne) {
		if (resultat != null) {
			for (int i = 0; i < nb_copies; i++) {
				// on écrit sur la i ème feuille de calcul que l'on a générée
				Sheet sheet = sheets.get(i);
				ArrayList<Object> reponses = resultat.get(i);
				for (int j = 0; j < reponses.size(); j++) {
					sheet.getCellAt(1, cpt_ligne - reponses.size() + j).setValue(reponses.get(j));
					// System.out.println(reponses.get(j));
				}
			}
		}
	}

	/**
	 * <b> Fonction qui génère nb_copie de la feuille de calcul originale </b>
	 * 
	 * @param sheet
	 *            : feuille de calcul originale
	 * @param nb
	 *            : nombre de versions que l'on crée
	 * @return ArrayList des nb feuilles de calcul
	 * @throws IOException
	 */
	private static ArrayList<Sheet> genereSheets(File file, int nb) throws IOException {
		ArrayList<Sheet> res = new ArrayList<Sheet>();
		for (int i = 0; i < nb; i++) {
			res.add(SpreadSheet.createFromFile(file).getSheet(0));
		}
		return res;
	}

	/**
	 * <b> Fonction qui sauvegarde les nb_copies feuilles de calcul </b>
	 * 
	 * @param sheets
	 *            : les nb_copies feuilles de calcul que l'on veut génerer
	 * @param nb
	 *            : nb_copies
	 * @param outputDirPath
	 *            : répertoire destination
	 * @param inputName
	 *            : nom du fichier source
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void saveSheets(ArrayList<Sheet> sheets, int nb, String outputDirPath, String inputName)
			throws FileNotFoundException, IOException {

		@SuppressWarnings("unused")
		File file = new File(outputDirPath, "simple-txt.txt");

		if (inputName.lastIndexOf('.') > 0) {
			inputName = inputName.substring(0, inputName.lastIndexOf('.'));
		}
		File outputDir = new File(outputDirPath + inputName);

		// if the directory does not exist, create it
		if (!outputDir.exists()) {
			System.out.println("creating directory: " + outputDir.getName());
		}
		try {
			outputDir.mkdir();
		} catch (SecurityException se) {

			throw new SecurityException("Erreur: impossible de créer le répertoire cible. Permission non accordée."
					+ se.getMessage() + "\n" + se.getStackTrace());
		}
		try {
			outputDir.mkdir();
		} catch (SecurityException se) {

			throw new SecurityException("Erreur: impossible de créer le répertoire cible. Permission non accordée."
					+ se.getMessage() + "\n" + se.getStackTrace());
		}

		// System.out.println("outputDir " + outputDir.getName() + "created");

		File outputFile;
		String name;
		File outputFileMaster;
		String nameMaster;

		for (int i = 0; i < nb; i++) {
			name = outputDir.getAbsolutePath() + '/' + inputName + '_' + (i + 1) + ".ods";
			outputFile = new File(name);

			nameMaster = outputDir.getAbsolutePath() + '/' + inputName + '_' + (i + 1) + "_master.ods";
			outputFileMaster = new File(nameMaster);

			sheets.get(i).getSpreadSheet().saveAs(outputFile);
			// System.out.println("generate " + outputFile.getName());

			sheets.get(i).getSpreadSheet().saveAs(outputFileMaster);
			// System.out.println("generate " + outputFileMaster.getName());

			PostProcessing postProcessing = new PostProcessing(name);
			postProcessing.process(PostProcessingOptions.REMOVE_ALL_COLORS);

			PostProcessing postProcessingMaster = new PostProcessing(nameMaster);
			postProcessingMaster.process(0);
		}
	}

	/**
	 * <b> Fonction qui incrémente la variable statique : l'indice des
	 * questions </b>
	 */
	private static void incIndice() {
		indice++;
	}

	/**
	 * Setter de inQuestion
	 * 
	 * @param b
	 *            : boolean a set
	 */
	private static void setInQuestion(boolean b) {
		inQuestion = b;
	}

	/**
	 * <b> Recrée une nouvelle liste pour la question suivante </b>
	 */
	private static void reset_reponses_input() {
		reponses_input = new ArrayList<Object>();
	}

	/**
	 * Vérifie que le format de la feuille de calcul est correct:
	 * <ul>
	 * <li>pas de texte sur la troizième colonne</li>
	 * <li>une ligne vide entre chaque question/réponses</li>
	 * <li>des réponses après chaque questions</li>
	 * <li>pas de blanc entre les réponses</li>
	 * </ul>
	 * 
	 * @param sheet
	 * @throws BadFormatException
	 */
	public static void checkSourceSheetFormat(Sheet sheet) throws BadFormatException {
		int cpt_ligne = 0; // compteur de lignes du fichier ods
		int indice_question = 0;
		boolean question = false; // boolean qui vérifie si on est en cours de
									// question

		String text = "Le fichier contient une cellule inattendue:\n";

		// nombre de ligne de la feuille de calcul
		int rowCount = sheet.getRowCount();

		// Tant qu'on a pas tout parcouru le fichier
		while (cpt_ligne < rowCount && cpt_ligne < MAX_ROWS_IN_QCM) {
			String cell_text1 = sheet.getCellAt(0, cpt_ligne).getTextValue();

			// on vérifie qu'il y a bien un espace entre chaque question/reponse
			// et que les questions sont bien précédées d'un chiffre
			if (Pattern.matches("\\d+", cell_text1)) {
				boolean isRedactionPart = ((sheet.getCellAt(0, cpt_ligne + 1).getTextValue()
						.equals("" + (Integer.parseInt(cell_text1) + 1)))
						|| sheet.getCellAt(0, cpt_ligne + 1).getTextValue().equals(""));
				if (question && !isRedactionPart) {
					text += "Un chiffre a été trouvé au mauvais endroit:\n " + "ligne: "
							+ Integer.toString(cpt_ligne + 1) + " - colonne: 1" + " -> \"" + cell_text1 + "\"";
					throw new BadFormatException(text);
				} else if (!question && (Integer.parseInt(cell_text1) != indice_question + 1) && !isRedactionPart) {
					text += "Le chiffre de cette question n'est pas bon:\n " + "ligne: "
							+ Integer.toString(cpt_ligne + 1) + " - colonne: 1" + " -> \"" + cell_text1 + "\"";
					throw new BadFormatException(text);
				}
				question = true;
				indice_question += 1;
			}
			// on vérifie qu'une réponse a bien lieu apres les questions et que
			// les réponses sont bien précédées d'une lettre
			else if (Pattern.matches("[a-zA-Z]", cell_text1)) {
				if (!question) {
					text += "Une lettre a été trouvée au mauvais endroit:\n " + "ligne: "
							+ Integer.toString(cpt_ligne + 1) + " - colonne: 1" + " -> \"" + cell_text1 + "\"";
					throw new BadFormatException(text);
				}
			} else if (cell_text1.equals("")) {
				question = false;
			}

			// increment
			cpt_ligne++;
		}
	}

}