package ru.bmstu.edu.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.bmstu.edu.objects.LinguisticVariable;
import ru.bmstu.edu.objects.MembershipFunction;
import ru.bmstu.edu.objects.Rule;
import ru.bmstu.edu.objects.utils.DaoUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public class EditRuleController {
  private Node nodesource;
  @FXML
  private ComboBox comboIFVarName;
  @FXML
  private ComboBox comboIFMFName;
  @FXML
  private ComboBox comboAndVarName;
  @FXML
  private ComboBox comboAndMFName;
  @FXML
  private ComboBox comboThenVarName;
  @FXML
  private ComboBox comboThenMFName;
  @FXML
  private TableColumn colVariableName;
  @FXML
  private TableColumn colValueMF;
  @FXML
  private TableView tableAnd;


  ObservableList<String> variablesNameList = FXCollections.observableArrayList();
  ObservableList<Rule> rulesList = FXCollections.observableArrayList();
  //ArrayList <LinguisticVariable> variablesList = DaoUtils.getVariables();
  LinkedHashMap<String,LinguisticVariable>mapVariables = DaoUtils.getMapVariables();
  Rule rule = new Rule();
  @FXML
  private void initialize(){
    colVariableName.setCellValueFactory(new PropertyValueFactory<Rule,String>("nameVariable"));
    colValueMF.setCellValueFactory(new PropertyValueFactory<EditRuleController,String>("valueMF"));
    if(mapVariables.size()>0){
      for (Map.Entry<String, LinguisticVariable> entry : mapVariables.entrySet()) {
        variablesNameList.add(entry.getKey());
      }
      comboIFVarName.setItems(variablesNameList);
      comboAndVarName.setItems(variablesNameList);
      comboThenVarName.setItems(variablesNameList);
    }
  }


  public void selectVarName(ActionEvent actionEvent){
    Object source = actionEvent.getSource();
    if (!(source instanceof ComboBox)) {
      return;
    }

    nodesource = (Node) actionEvent.getSource();
    ComboBox comboBox = (ComboBox) source;
    System.out.println(comboBox.getValue());
    LinguisticVariable selectVariable = mapVariables.get(comboBox.getValue());
    ObservableList<String>mfNameList = FXCollections.observableArrayList();
    System.out.println(comboIFVarName.getValue());
    if(selectVariable!=null){
      ObservableList<MembershipFunction> mfList = FXCollections.observableArrayList(selectVariable.getMfList());
      for(int i = 0;i<mfList.size();i++){
        MembershipFunction mf = mfList.get(i);
        mfNameList.add(mf.getMFname());
      }
      switch (comboBox.getId()) {
        case "comboIFVarName":
          comboIFMFName.setItems(mfNameList);
          break;
        case "comboAndVarName":
          comboAndMFName.setItems(mfNameList);
          break;
        case "comboThenVarName":
          comboThenMFName.setItems(mfNameList);
          break;
      }
    }
  }
  public void addRule(){
    LinguisticVariable linguisticVariable = mapVariables.get(comboAndVarName.getValue());
    if(linguisticVariable==null) return;
    String mfName = (String) comboAndMFName.getValue();
    if(mfName.isEmpty()||mfName==null) return;
    Rule rule = new Rule(linguisticVariable.getId(),linguisticVariable.getName(),mfName);
    rulesList.add(rule);
    tableAnd.setItems(rulesList);
    comboAndVarName.getSelectionModel().clearSelection();
    comboAndMFName.getSelectionModel().clearSelection();

  }
}