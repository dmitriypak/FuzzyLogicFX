package ru.bmstu.edu.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class MainController {

  @FXML
  private Button btnLinguisticVariables;
  @FXML
  private Button btnCVWebView;
  @FXML
  private Button btnRules;
  @FXML
  private Button btnGroup;

  private Node nodesource;
  private CvWebViewController cvWebViewController = new CvWebViewController();

  public void initialize(){
    btnLinguisticVariables.setTooltip(new Tooltip("Лингвистические переменные"));
    btnRules.setTooltip(new Tooltip("Список правил"));
    btnGroup.setTooltip(new Tooltip("Настройка категорий работников"));
  }

  private Stage getStage(String url, String title) throws IOException {
    Stage stage = new Stage();
    Parent root = FXMLLoader.load(getClass().getResource(url));
    stage.setTitle(title);
    stage.setScene(new Scene(root));
    stage.initModality(Modality.WINDOW_MODAL);
    return stage;
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
          Stage stage = getStage("../fxml/linguisticVariables.fxml","Лингвистические переменные");
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
          Stage stage = getStage("../fxml/rules.fxml","Правила");
          stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
          stage.showAndWait();
        } catch (IOException e) {
          e.printStackTrace();
        }

        break;
      case "btnCV":
        try {
          Stage stage =getStage("../fxml/cv.fxml","База резюме");
          stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
          stage.showAndWait();
        } catch (IOException e) {
          e.printStackTrace();
        }
        break;
      case "btnProjects":
        try{
          Stage stage = getStage("../fxml/projects.fxml","Список проектов");
          stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
          stage.showAndWait();
        } catch (IOException e) {
          e.printStackTrace();
        }
        break;
      case "btnGroup":
        try{
          Stage stage = getStage("../fxml/category.fxml","Категории работников");
          stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
          stage.showAndWait();
        } catch (IOException e) {
          e.printStackTrace();
        }
        break;

    }


  }
}
