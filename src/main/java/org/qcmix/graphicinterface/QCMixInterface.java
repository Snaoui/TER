package org.qcmix.graphicinterface;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.qcmix.QCMixer;
import org.qcmix.exceptions.BadFormatException;
import org.qcmix.exceptions.ConversionException;
import org.qcmix.tools.DesktopApiMultiplatform;
import org.qcmix.tools.Helper;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * <b>QCMIXInterface est la classe codant l'interface de QCMIX.</b>
 * <p>
 * QCMIXInterface étend la classe abstraite "Application" de JavaFX</p>
 * <p>
 * L'interface est constituée de deux boutons:
 * <ul>
 * <li>"Importer QCMSource" permettant d'ouvrir un filePicker pour importer le
 * QCM source.</li>
 * <li>"Lancer Mélange QCM" permettant d'ouvrir un directoryPicker pour choisir
 * où stocker les QCM mélangés et lancer le mélange</li>
 * </ul>
 * Des labels sont associés à chacun de ces boutons
 * <ul>
 * <li>Le premier pour nommer le QCM importé</li>
 * <li>Le second pour spécifier l'état de l'analyse</li>
 * </ul>
 * Une variable privée "processing" permet de déterminer si un mélange est en
 * cours ou pas, afin d'éviter la fermeture prématurée du programme.
 *
 * @see ConfirmBox
 * @see AlertBox
 * @see HelperBox
 *
 * @author Manel
 *
 */
public class QCMixInterface extends Application {

	private String qcmSourcePath;
	private String qcmDestinationPath;

	private Label lblPathImport = new Label("");
	private Label lblTraitement = new Label("");

	private Button importFileButton = new Button();
	private Button launchMixButton = new Button();

	private Boolean processing = false;
	private boolean navigationEnabled = false;
	final DirectoryChooser destChooser = new DirectoryChooser();
	final FileChooser qcmChooser = new FileChooser();

	/**
	* <b>Lance l'application et initialise les différents éléments.</b>
	*
	* @param primaryStage
	*/
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("QCMix");
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			// Vérifie que la variable processing est à false avant de fermer
			@Override
			public void handle(WindowEvent event) {
				event.consume();
				closeProgram(primaryStage);
			}
		});

		// ------------ Definition des boutons ------------- //
		importFileButton.setText("Importer QCM Source");
		importFileButton.setMinWidth(200);
		importFileButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				importAction(primaryStage);
			}
		});

		launchMixButton.setText("Convertir en AMC-TXT");
		// Impossible de cliquer ce bouton si aucun fichier n'est importé
		launchMixButton.setDisable(true);
		launchMixButton.setMinWidth(200);
		launchMixButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				if (!navigationEnabled) {
					exportAction(primaryStage);
				} else {
					try {
						navigateAction(qcmDestinationPath);
					} catch (IOException | URISyntaxException e) {
						AlertBox.alert("Erreur", "La destination demandée est inconnue\n\n" + Helper.getStackTrace(e));
						launchMixButton.setDisable(true);
					}
				}
			}
		});

		Button infos1 = new Button();
		infos1.setMinWidth(10);
		infos1.setText("?");
		infos1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				HelperBox.helper();
			}
		});


		// ------------ Definition de la grille ------------- //
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10, 10, 10, 10));
		//grid.setGridLinesVisible(true);
		grid.setVgap(5);
		grid.setHgap(5);
		grid.setMinWidth(10);
		grid.getColumnConstraints().add(new ColumnConstraints(200));
		grid.getColumnConstraints().add(new ColumnConstraints(200));

		// ------------ Definition des positions ------------- //
		grid.add(importFileButton, 0, 0);
		grid.add(lblPathImport, 1, 0);
		grid.add(launchMixButton, 0, 1);
		grid.add(lblTraitement, 1, 1);
		grid.add(infos1, 2, 0);

		// ------------ Definition de la scene ------------- //
		Scene scene = new Scene(grid, 460, 80);

		primaryStage.setScene(scene);
		primaryStage.show();
	}


	/**
	* <b>Vérifie qu'aucun mélange n'est en cours avant de fermer</b>
	*
	* @param primaryStage
	*/
	private void closeProgram(Stage primaryStage) {
		if (processing) {
			Boolean answer = ConfirmBox.confirm("Quitter", "La conversion est en cours, êtes-vous sûr de vouloir quitter?");
			if (answer) {
			primaryStage.close();
			}
		} else {
		primaryStage.close();
		}

	}

	/**
	* <b>Ouvre une fenêtre de sélection de fichier</b>
	*
	* @param primaryStage
	*/
	private void importAction(Stage primaryStage) {
		System.out.println("Le bouton Importer a été cliqué");
		qcmChooser.setTitle("Sélection QCM source");
		// Ouverture du filePicker
		File fileChosen = qcmChooser.showOpenDialog(primaryStage);
		if (fileChosen != null) {
			lblTraitement.setText("");
			qcmSourcePath = fileChosen.getAbsolutePath();
			// Modification du répertoire de recherche en fonction de la recherche
			qcmChooser.setInitialDirectory(fileChosen.getParentFile());
			// Récupération du nom du fichier importé
			lblPathImport.setText(qcmSourcePath.substring(
					qcmSourcePath.lastIndexOf(File.separator) + 1));
			//pathImport.setText(QCMSourcePath);
			// Autorise le lancement du mélange
			launchMixButton.setDisable(false);
			// Le processing ne peut pas être en cours (ligne inutile)
			processing = false;
			changeNavigationMode(false);
		} else {
			System.out.println("Choix du QCM annulé");
		}
	}

	/**
	* <b>Ouvre une fenêtre de sélection de répertoire</b>
	*
	* @param primaryStage
	*/
	private void exportAction(Stage primaryStage) {
		System.out.println("Le bouton Analyse a été cliqué");
		destChooser.setTitle("Sélection répertoire destination");
		lblTraitement.setText("En cours de traitement...");
		launchMixButton.setDisable(true);

		// Ouverture du dirPicker
		File dirChosen = destChooser.showDialog(primaryStage);
		if (dirChosen != null) {
			qcmDestinationPath = dirChosen.getAbsolutePath() + "/";
			destChooser.setInitialDirectory(dirChosen);
			System.out.println("Traitement vers " + qcmDestinationPath);
			processing = true;


			try {
				System.out.println("source path: " + qcmSourcePath);
				QCMixer.mix(qcmSourcePath, qcmDestinationPath);
				lblTraitement.setText("Terminé!");
				changeNavigationMode(true);
			} catch (BadFormatException e) {
				handlerException(e, "Erreur de formatage", false);
			} catch (InterruptedException |ConversionException e) {
				handlerException(e, "Erreur à la conversion du fichier en fichier ODS", false);
			} catch (NullPointerException | IOException e) {
				handlerException(e, "Erreur", false);
			} catch (Exception e) {
				handlerException(e, "Erreur inconnue", true);
			}

			processing = false;

			//launchMixButton.setDisable(true);
			// Impossible de lancer deux fois le même mélange
			lblPathImport.setText("");
		} else {
			System.out.println("Mélange annulé");
			launchMixButton.setDisable(false);
		}
	}


	private void handlerException(Throwable e, String typeError, boolean unknown) {
		lblTraitement.setText("Opération Impossible.");
		if (unknown) {
			AlertBox.alert(typeError, "L'erreur survenue est inconnue, veuillez contacter les développeurs\n" + e.getMessage());
		}
		else {
			AlertBox.alert(typeError, e.getMessage());
		}
		launchMixButton.setDisable(true);
	}


	private void changeNavigationMode(boolean mode) {
		navigationEnabled = mode;
		if (navigationEnabled) {
			launchMixButton.setText("Ouvrir le navigateur");
			launchMixButton.setDisable(false);
		} else {
			launchMixButton.setText("Lancer la conversion");
		}
	}

	private void navigateAction(String path) throws IOException, URISyntaxException {
		File outputDir = new File(path);
			DesktopApiMultiplatform.open(outputDir);
	}

	/**
	* <b>Lance l'interface grâce à la méthode launch de application.</b>
	*
	* @param args
	*/
	public static void main(String[] args) {
		launch(args);
	}
}
