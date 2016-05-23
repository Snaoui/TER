package org.qcmix.graphicinterface;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * <b>ConfirmBox est la classe codant les fenetres de validation.</b>
 *
 * @author luc
 *
 */
public class ConfirmBox {
	static Boolean answer = false;

	/**
	 * <b> confirm est la méthode appelée en static pour faire apparaitre la fenetre de validation </b>
	 *
	 * @param title Titre de la fenêtre de validation
	 * @param pConfirmationString Message à valider
	 * @return answer réponse de l'utilisateur
	 */
	public static Boolean confirm(String title, String pConfirmationString) {


		Stage askWindow = new Stage();

		askWindow.initModality(Modality.APPLICATION_MODAL);
		askWindow.setTitle(title);
		askWindow.setMinWidth(250);

		Label confirmation = new Label(pConfirmationString);

		Button cancelButton = new Button("Annuler");
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				answer = false;
				askWindow.close();
			}
		});

		Button validateButton = new Button("Valider");
		validateButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				answer = true;
				askWindow.close();
			}
		});

		VBox champ = new VBox(10);
		champ.getChildren().add(confirmation);
		champ.setAlignment(Pos.CENTER);

		HBox boutons = new HBox(10);
		boutons.getChildren().addAll(validateButton, cancelButton);
		boutons.setAlignment(Pos.CENTER);

		BorderPane borderPane = new BorderPane();
		borderPane.setTop(champ);
		borderPane.setBottom(boutons);

		Scene scene = new Scene(borderPane);
		askWindow.setScene(scene);
		askWindow.showAndWait();

		return answer;
	}
}
