package org.qcmix.graphicinterface;

import org.qcmix.tools.Helper;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * <b>AlertBox est la classe codant les fenetres d'alertes.</b>
 *
 * @author luc
 *
 */
public class AlertBox {
	/**
	 * <b> alert est la méthode appelée en static pour faire apparaitre la fenetre d'alerte </b>
	 *
	 * @param title Titre de la fenêtre d'alerte
	 * @param pAlertString Message d'alerte
	 */
	public static void alert(String title, String pAlertString) {

		Stage alertWindow = new Stage();

		alertWindow.initModality(Modality.APPLICATION_MODAL);
		alertWindow.setTitle(title);
		alertWindow.setMinWidth(250);

		Label alert = new Label(pAlertString);
		alert.setTextAlignment(TextAlignment.CENTER);

		Button closeButton = new Button("Fermer");
		closeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				alertWindow.close();
			}
		});

		Button copyButton = new Button("Copier le message");
		copyButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Helper.copyToClipBoard(pAlertString);
			}
		});

		VBox champ = new VBox(10);
		champ.getChildren().add(alert);
		champ.getChildren().add(copyButton);
		champ.getChildren().add(closeButton);
		champ.setAlignment(Pos.CENTER);
		champ.setPadding(new Insets(10));

		Scene scene = new Scene(champ);
		alertWindow.setScene(scene);
		alertWindow.showAndWait();
	}


}
