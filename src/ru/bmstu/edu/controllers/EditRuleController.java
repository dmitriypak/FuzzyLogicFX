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
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.bmstu.edu.objects.Condition;
import ru.bmstu.edu.objects.LinguisticVariable;
import ru.bmstu.edu.objects.MembershipFunction;
import ru.bmstu.edu.objects.Rule;
import ru.bmstu.edu.objects.utils.DaoUtils;

import java.sql.SQLException;
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
  ObservableList<String> variablesNameListRank = FXCollections.observableArrayList();
  ObservableList<Condition> conditionList = FXCollections.observableArrayList();
  //ArrayList <LinguisticVariable> variablesList = DaoUtils.getVariables();
  LinkedHashMap<String,LinguisticVariable>mapVariables = DaoUtils.getMapVariables();
  //Condition condition = new Condition();
  @FXML
  private void initialize(){
    colVariableName.setCellValueFactory(new PropertyValueFactory<Condition,String>("nameVariable"));
    colValueMF.setCellValueFactory(new PropertyValueFactory<EditRuleController,String>("valueMF"));
    variablesNameListRank.add(mapVariables.get("Ранг").getName());
    if(mapVariables.size()>0){
      for (Map.Entry<String, LinguisticVariable> entry : mapVariables.entrySet()) {
        variablesNameList.add(entry.getKey());
      }
      comboIFVarName.setItems(variablesNameList);
      comboAndVarName.setItems(variablesNameList);
      comboThenVarName.setItems(variablesNameListRank);
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
          //mfNameList.remove(comboBox.getSelectionModel().getSelectedIndex());
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
  public void addCondition(){
    LinguisticVariable linguisticVariable = mapVariables.get(comboAndVarName.getValue());
    if(linguisticVariable==null) return;
    String mfName = (String) comboAndMFName.getValue();
    if(mfName.isEmpty()||mfName==null) return;
    Condition condition = new Condition(linguisticVariable.getId(),linguisticVariable.getName(),mfName);
    conditionList.add(condition);
    tableAnd.setItems(conditionList);
    comboAndVarName.getSelectionModel().clearSelection();
    comboAndMFName.getSelectionModel().clearSelection();

  }
  public JSONArray getJSONCondition(int id, String nameVar, String nameMF){
    Map<String,String> map = new LinkedHashMap<>();
    map.put("idvariable",String.valueOf(id));
    map.put("nameVariable",nameVar);
    map.put("nameMF",nameMF);
    JSONObject obj = new JSONObject(map);
    JSONArray array = new JSONArray();
    array.add(obj);
    return array;
  }


  public void saveRule() throws SQLException {

    JSONObject obj = new JSONObject();
    JSONArray arAND = new JSONArray();
    int idVariableIF = mapVariables.get(comboIFVarName.getValue()).getId();
    JSONArray arIF = getJSONCondition(idVariableIF,comboIFVarName.getValue().toString(),comboIFMFName.getValue().toString());
    JSONArray arThen = getJSONCondition(mapVariables.get(comboThenVarName.getValue()).getId(),comboThenVarName.getValue().toString(),comboThenMFName.getValue().toString());
    if(conditionList.size()==0) {
      arAND = getJSONCondition(mapVariables.get(comboAndVarName.getValue()).getId(),comboAndVarName.getValue().toString(),comboAndMFName.getValue().toString());
    }else{
      for(int i = 0;i<conditionList.size();i++){
        JSONArray array;
        Condition condition = conditionList.get(i);
        int idVariable = mapVariables.get(condition.getNameVariable()).getId();
        array = getJSONCondition(idVariable,condition.getNameVariable(),condition.getValue());
        arAND.add(array);
      }

    }

    obj.put("AND",arAND);

    obj.put("IF",arIF);

    obj.put("THEN",arThen);

    Rule rule = new Rule();
    rule.setValue(obj.toString());
    DaoUtils.insertRule(rule,idVariableIF);

    System.out.println(obj.toString());
  }

  public void actionClose(ActionEvent actionEvent) {
    Node source = (Node) actionEvent.getSource();
    Stage stage = (Stage) source.getScene().getWindow();
    stage.hide();
  }
}