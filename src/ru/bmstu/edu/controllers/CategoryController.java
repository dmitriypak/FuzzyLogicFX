package ru.bmstu.edu.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import ru.bmstu.edu.objects.Category;

import java.io.IOException;

public class CategoryController {
  @FXML
  private TableView tableCategory;
  private Parent fxmlEdit;
  private FXMLLoader fxmlLoader = new FXMLLoader();
  private Node nodesource;


  @FXML
  private void initialize(){
    try {
      fxmlLoader.setLocation(getClass().getResource("../fxml/viewCategory.fxml"));
      fxmlEdit = fxmlLoader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void showDialog(Category category) {

    if (category == null) return;
    Stage viewCategoryStage = new Stage();
    GridPane root = new GridPane();
    viewCategoryStage.setTitle("Редактирование категорий");
    viewCategoryStage.setMinHeight(600);
    viewCategoryStage.setMinWidth(800);
    viewCategoryStage.setResizable(true);
    root.setMinWidth(300);
    ScrollPane scrollPane = new ScrollPane();
  }

  public void actionButtonPressed(ActionEvent actionEvent) {
    Object source = actionEvent.getSource();
    if (!(source instanceof Button)) {
      return;
    }
    nodesource = (Node) actionEvent.getSource();
    Button clickedButton = (Button) source;

    switch (clickedButton.getId()) {
      case "btnEditCategory":
        Category category = (Category) tableCategory.getSelectionModel().getSelectedItem();
        showDialog(category);
        break;
    }
  }

}
