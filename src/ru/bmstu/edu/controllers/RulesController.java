package ru.bmstu.edu.controllers;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeItem;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.CheckTreeView;
import ru.bmstu.edu.objects.LinguisticVariable;
import ru.bmstu.edu.objects.utils.DaoUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class RulesController {

  @FXML
  private CheckTreeView treeViewRules;
  private Node nodesource;

  @FXML
  private void initialize(){
    fillData();

  }

  private void fillData() {
    ArrayList<LinguisticVariable> listVariables = DaoUtils.getVariables();
    CheckBoxTreeItem<String> root = new CheckBoxTreeItem<String>("Переменные");
    root.setExpanded(true);
    for(LinguisticVariable linguisticVariable:listVariables){
      CheckBoxTreeItem<String> variable = new CheckBoxTreeItem<String>(linguisticVariable.getName());
      variable.setExpanded(true);

      variable.getChildren().addAll(
          new CheckBoxTreeItem<String>("Jonathan"),
          new CheckBoxTreeItem<String>("Eugene"),
          new CheckBoxTreeItem<String>("Henri"),
          new CheckBoxTreeItem<String>("Samir"));
      root.getChildren().add(variable);
    }

    treeViewRules.setRoot(root);
    treeViewRules.getCheckModel().getCheckedItems().addListener(new ListChangeListener<TreeItem<String>>() {
      public void onChanged(ListChangeListener.Change<? extends TreeItem<String>> c) {
        //System.out.println(treeViewRules.getCheckModel().getCheckedItems());
      }
    });


  }

  public void actionButtonPressed(ActionEvent actionEvent) throws SQLException {
    Object source = actionEvent.getSource();
    if (!(source instanceof Button)) {
      return;
    }
    nodesource = (Node) actionEvent.getSource();
    Button clickedButton = (Button) source;

    switch (clickedButton.getId()) {
      case "btnAddRule":
        try {
          Stage stage = new Stage();
          Parent root = FXMLLoader.load(getClass().getResource("../fxml/editRule.fxml"));
          stage.setTitle("Добавить правило");
          stage.setScene(new Scene(root));
          stage.initModality(Modality.WINDOW_MODAL);
          stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
          stage.show();
        } catch (IOException e) {
          e.printStackTrace();
        }
        break;
      case "btnEditRule":
        break;
      case "btnDeleteRule":
        break;
    }
  }
}
