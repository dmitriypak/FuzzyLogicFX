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

public class MainController {

  @FXML
  private Button btnLinguisticVariables;

  @FXML
  public void initialize(){

  }

  public void showVariables(ActionEvent actionEvent){
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

  }

}
