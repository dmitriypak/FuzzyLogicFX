package ru.bmstu.edu.controllers;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeItem;
import org.controlsfx.control.CheckTreeView;
import ru.bmstu.edu.objects.LinguisticVariable;
import ru.bmstu.edu.objects.utils.DaoUtils;

import java.util.ArrayList;

public class RulesController {

  @FXML
  private CheckTreeView treeViewRules;

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
        System.out.println(treeViewRules.getCheckModel().getCheckedItems());
      }
    });


  }


}
