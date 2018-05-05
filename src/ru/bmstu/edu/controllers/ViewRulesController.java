package ru.bmstu.edu.controllers;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import ru.bmstu.edu.objects.CV;
import ru.bmstu.edu.objects.LinguisticVariable;
import ru.bmstu.edu.objects.utils.DaoUtils;

import java.util.ArrayList;

public class ViewRulesController {
  private CV cv;
  private ArrayList<LinguisticVariable> listVariables = DaoUtils.getInputVariables();
  private Stage viewRulesStage;
  @FXML
  private void initialize(){

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

  }
}
