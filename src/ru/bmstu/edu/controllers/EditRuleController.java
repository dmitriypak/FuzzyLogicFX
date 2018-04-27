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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
  ObservableList<String> variablesNameListRank = FXCollections.observableArrayList();
  ObservableList<Rule> rulesList = FXCollections.observableArrayList();
  //ArrayList <LinguisticVariable> variablesList = DaoUtils.getVariables();
  LinkedHashMap<String,LinguisticVariable>mapVariables = DaoUtils.getMapVariables();
  Rule rule = new Rule();
  @FXML
  private void initialize(){
    colVariableName.setCellValueFactory(new PropertyValueFactory<Rule,String>("nameVariable"));
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


  public void saveRule(){
    JSONObject obj = new JSONObject();
    JSONArray arAND = new JSONArray();
    JSONArray arIF = getJSONCondition(mapVariables.get(comboIFVarName.getValue()).getId(),comboIFVarName.getValue().toString(),comboIFMFName.getValue().toString());
    JSONArray arThen = getJSONCondition(mapVariables.get(comboThenVarName.getValue()).getId(),comboThenVarName.getValue().toString(),comboThenMFName.getValue().toString());


    for(int i = 0;i<rulesList.size();i++){
      JSONObject objMF = new JSONObject();
      objMF.put("idvariable",rulesList.get(i).getValueMF());
      arAND.add(objMF);
    }
    obj.put("IF",arIF);
    obj.put("AND",arAND);
    obj.put("THEN",arThen);

    System.out.println(obj.toString());
  }
}