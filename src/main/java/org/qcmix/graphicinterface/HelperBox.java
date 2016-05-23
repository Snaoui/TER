package org.qcmix.graphicinterface;

import org.qcmix.tools.Helper;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class HelperBox {

	public static void helper() {
		
		String phrase_informations = "Les fichiers en entrées doivent respecter les règles suivantes : \n\n"
				+ " - Format du fichier en entrée : .ODS \n\n"
				+ " - Les lettres de réponses doivent être placées  sur la seconde colonne du fichier.\n\n"
				+ " - Les questions sont numérotées par des chiffres et les réponses par des lettres. \n\n"
				+ " - Les numéros des questions doivent être consécutifs. \n\n"
				+ " - Le fichier peut éventuellement contenir un sujet d'expression écrite"
				+ " qui doit être placé à la fin et il ne doit pas être numéroté. \n ";
		
		Window primaryStage = null;

		
		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(primaryStage);
		dialog.setTitle("Informations sur les fichiers d'entrée ");

		
		Button infos2 = new Button();
		infos2.setText("Page Wiki ");
		infos2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Helper.openWebPage("https://git.framasoft.org/easyProg/QCMix/wikis/StructureFichiers");
			}
		});
		
		VBox dialogVbox = new VBox(20);		
		dialogVbox.getChildren().add(new Text(phrase_informations));
		dialogVbox.getChildren().add(infos2);
		dialogVbox.setPadding(new Insets(10));


		Scene dialogScene = new Scene(dialogVbox);
		
		
		dialog.setScene(dialogScene);
		dialog.show();

	}
	
}
