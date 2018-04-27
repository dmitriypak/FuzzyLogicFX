package ru.bmstu.edu.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class MainController {

  @FXML
  private Button btnLinguisticVariables;
  @FXML
  private Button btnCVWebView;

  private Node nodesource;
  private CvWebViewController cvWebViewController = new CvWebViewController();

  public void initialize(){

  }

  public void actionButtonPressed(ActionEvent actionEvent) throws SQLException {
    Object source = actionEvent.getSource();
    if (!(source instanceof Button)) {
      return;
    }
    nodesource = (Node) actionEvent.getSource();
    Button clickedButton = (Button) source;

    switch (clickedButton.getId()) {
      case "btnLinguisticVariables":
        try{
          Stage stage = new Stage();
          Parent root = FXMLLoader.load(getClass().getResource("../fxml/linguisticVariables.fxml"));
          stage.setTitle("Лингвистические переменные");
          stage.setScene(new Scene(root));
          stage.initModality(Modality.WINDOW_MODAL);
          stage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
          stage.show();
        }catch (IOException e){
          e.printStackTrace();
        }
        break;

      case "btnCVWebView":
        try {
          Stage stage = new Stage();
          FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/cvWebView.fxml"));
          Parent root = loader.load();
          cvWebViewController = loader.getController();
          cvWebViewController.setURL("https://eclipse.org");
          stage.setTitle("Web View");
          stage.setScene(new Scene(root));
          stage.initModality(Modality.WINDOW_MODAL);
          stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
          stage.showAndWait();

        } catch (IOException e) {
          e.printStackTrace();
        }
        break;
      case "btnRules":
        try {
          Stage stage = new Stage();
          FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/rules.fxml"));
          Parent root = loader.load();
          stage.setTitle("Правила");
          stage.setScene(new Scene(root));
          stage.initModality(Modality.WINDOW_MODAL);
          stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
          stage.showAndWait();
        } catch (IOException e) {
          e.printStackTrace();
        }

        break;
      case "btnCV":
        try {
          Stage stage = new Stage();
          FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/cv.fxml"));
          Parent root = loader.load();
          stage.setTitle("База резюме");
          stage.setScene(new Scene(root));
          stage.initModality(Modality.WINDOW_MODAL);
          stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
          stage.showAndWait();
        } catch (IOException e) {
          e.printStackTrace();
        }
        break;

    }


  }
}
