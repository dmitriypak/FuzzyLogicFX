package ru.bmstu.edu.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import ru.bmstu.edu.objects.CV;
import ru.bmstu.edu.objects.LinguisticVariable;
import ru.bmstu.edu.objects.utils.DaoUtils;

import java.util.ArrayList;

public class ViewRulesController {
  private CV cv;
  private ArrayList<LinguisticVariable> listVariables = DaoUtils.getVariables();
  private Stage viewRulesStage;
  @FXML
  private void initialize(){

    viewRulesStage = new Stage();
    viewRulesStage.setTitle("sdfsf");

    System.out.println("test");
  }

  public void setStage(Stage stage){
    this.viewRulesStage = stage;
    //viewRulesStage.setTitle("Мамдани2");
  }
  public void setCV(CV cv){
    if(cv==null){
      return;
    }
    System.out.println("Получено резюме id: " + cv.getId());
    this.cv=cv;

    if(cv.getId()!=0){
      for(int i = 0;i<listVariables.size();i++){
        Label label = new Label(listVariables.get(i).getName());

        FlowPane root = new FlowPane();
        root.setPadding(new Insets(10));
        root.getChildren().add(label);

        Scene scene = new Scene(root, 200, 100);



      }
    }
  }
}
