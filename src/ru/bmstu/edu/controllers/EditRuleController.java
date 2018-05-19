package ru.bmstu.edu.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
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
  @FXML
  private Button btnCancel;
  @FXML
  private Button btnRemoveFromList;


  private Rule rule;

  ObservableList<String> variablesNameList = FXCollections.observableArrayList();
  ObservableList<String> variablesNameListOutput = FXCollections.observableArrayList();
  ObservableList<Condition> conditionList = FXCollections.observableArrayList();
  //ArrayList <LinguisticVariable> variablesList = DaoUtils.getVariables();
  //Получение карты правил
  LinkedHashMap<String,LinguisticVariable>mapInputVariables = DaoUtils.getMapInputVariables();
  LinkedHashMap<String,LinguisticVariable>mapOutputVariables = DaoUtils.getMapOutputVariables();
  //Condition condition = new Condition();
  @FXML
  private void initialize(){
    colVariableName.setCellValueFactory(new PropertyValueFactory<Condition,String>("nameVariable"));
    colValueMF.setCellValueFactory(new PropertyValueFactory<Condition,String>("valueMF"));
    if(mapInputVariables.size()>0){
      for (Map.Entry<String, LinguisticVariable> entry : mapInputVariables.entrySet()) {
        variablesNameList.add(entry.getKey());
      }
      comboIFVarName.setItems(variablesNameList);
      comboAndVarName.setItems(variablesNameList);
    }

    if(mapOutputVariables.size()>0){
      for (Map.Entry<String, LinguisticVariable> entry : mapOutputVariables.entrySet()) {
        variablesNameListOutput.add(entry.getKey());
      }
      comboThenVarName.setItems(variablesNameListOutput);
    }
  }

  public Rule getRule(){
    return rule;
  }

  public void setRule(Rule rule){
    if(rule==null) return;
    this.rule = rule;
    System.out.println("Получено правило id: " + rule.getIdRule());
    if(rule.getIdRule()==0){
      comboIFVarName.getSelectionModel().clearSelection();
      comboIFMFName.setValue("");
      comboAndVarName.getSelectionModel().clearSelection();
      comboAndMFName.setValue("");
      comboThenVarName.getSelectionModel().clearSelection();
      comboThenMFName.setValue("");
      conditionList.clear();
    }else{
      conditionList.clear();
      //IF
      Map<String,Condition> mapIF = rule.getIFConditionMap();
      for(Map.Entry<String, Condition> m:mapIF.entrySet()){
        comboIFVarName.getSelectionModel().select(m.getKey());
        System.out.println("mapIF " + m.getKey());
        Condition condition = m.getValue();
        selectVarName(m.getKey());
        comboIFMFName.setValue(condition.getMembershipFunction());
      }
      //AND
      Map<String,Condition> mapAND = rule.getANDConditionMap();
      for(Map.Entry<String, Condition> m:mapAND.entrySet()){
        System.out.println("mapAND " + m.getKey());
        Condition condition = m.getValue();
        System.out.println("CodeMF " + m.getValue().getMembershipFunction().getCodeMF());
        conditionList.add(condition);
      }
      tableAnd.setItems(conditionList);

      //THEN
      Map<String,Condition> mapTHEN = rule.getTHENConditionMap();
      for(Map.Entry<String, Condition> m:mapTHEN.entrySet()){
        comboThenVarName.getSelectionModel().select(m.getKey());
        System.out.println("mapTHEN " + m.getKey());
        Condition condition = m.getValue();
        selectVarName(m.getKey());
        comboThenMFName.setValue(condition.getMembershipFunction());
      }

    }

  }


  public void selectVarOutputName(ActionEvent actionEvent) {
    Object source = actionEvent.getSource();
    if (!(source instanceof ComboBox)) {
      return;
    }

    nodesource = (Node) actionEvent.getSource();
    ComboBox comboBox = (ComboBox) source;
    System.out.println(comboBox.getValue());
    LinguisticVariable selectVariable = mapOutputVariables.get(comboBox.getValue());
    ObservableList<MembershipFunction>mfNameList = FXCollections.observableArrayList();
    if(selectVariable!=null){
      ObservableList<MembershipFunction> mfList = FXCollections.observableArrayList(selectVariable.getMfList());
      for(int i = 0;i<mfList.size();i++){
        MembershipFunction mf = mfList.get(i);
        mfNameList.add(mf);
      }
      switch (comboBox.getId()) {
        case "comboThenVarName":
          comboThenMFName.setItems(mfNameList);
          break;
      }
    }
  }
  public void selectVarName(String variableName){
    LinguisticVariable selectVariable = mapInputVariables.get(variableName);
    ObservableList<MembershipFunction>mfNameList = FXCollections.observableArrayList();

    if(selectVariable!=null){
      ObservableList<MembershipFunction> mfList = FXCollections.observableArrayList(selectVariable.getMfList());
      for(int i = 0;i<mfList.size();i++){
        MembershipFunction mf = mfList.get(i);
        mfNameList.add(mf);
      }
      comboIFMFName.setItems(mfNameList);
    }
  }

  public void selectVarName(ActionEvent actionEvent){
    Object source = actionEvent.getSource();
    if (!(source instanceof ComboBox)) {
      return;
    }

    nodesource = (Node) actionEvent.getSource();
    ComboBox comboBox = (ComboBox) source;
    LinguisticVariable selectVariable = mapInputVariables.get(comboBox.getValue());
    ObservableList<MembershipFunction>mfNameList = FXCollections.observableArrayList();

    if(selectVariable!=null){
      ObservableList<MembershipFunction> mfList = FXCollections.observableArrayList(selectVariable.getMfList());
      for(int i = 0;i<mfList.size();i++){
        MembershipFunction mf = mfList.get(i);
        mfNameList.add(mf);
      }
      switch (comboBox.getId()) {
        case "comboIFVarName":
          comboIFMFName.setItems(mfNameList);
          //mfNameList.remove(comboBox.getSelectionModel().getSelectedIndex());
          break;
        case "comboAndVarName":
          comboAndMFName.setItems(mfNameList);
          break;
      }
    }
  }
  public void addCondition(){
    LinguisticVariable linguisticVariable = mapInputVariables.get(comboAndVarName.getValue());
    if(linguisticVariable==null) return;
    MembershipFunction m = (MembershipFunction) comboAndMFName.getSelectionModel().getSelectedItem();
    String mfName = m.getNameMF();
    if(mfName.isEmpty()||mfName==null) return;
    Condition condition = new Condition(linguisticVariable.getId(),linguisticVariable.getName());
    condition.setMembershipFunction(m);
    conditionList.add(condition);
    tableAnd.setItems(conditionList);
    comboAndVarName.getSelectionModel().clearSelection();
    comboAndMFName.getSelectionModel().clearSelection();
  }

  public void removeCondition(){
    Condition delCondition = (Condition) tableAnd.getSelectionModel().getSelectedItem();
    conditionList.remove(delCondition);
    tableAnd.setItems(conditionList);
  }

  public JSONObject getJSONCondition(int id, String nameVariable, MembershipFunction mf){
    Map<String,String> map = new LinkedHashMap<>();
    map.put("idvariable",String.valueOf(id));
    map.put("nameVariable",nameVariable);
    map.put("nameMF",mf.getNameMF());
    map.put("codeMF", mf.getCodeMF());
    JSONObject obj = new JSONObject(map);
    return obj;
  }


  public void saveRule() throws SQLException {
    Map<String,Condition> ifMap = new LinkedHashMap<>();
    Map<String,Condition> andMap = new LinkedHashMap<>();
    Map<String,Condition> thenMap = new LinkedHashMap<>();

    JSONObject obj = new JSONObject();
    JSONArray arAND = new JSONArray();
    int idVariableIF = mapInputVariables.get(comboIFVarName.getValue()).getId();

    JSONArray arIF = new JSONArray();
    MembershipFunction mfIF = (MembershipFunction) comboIFMFName.getSelectionModel().getSelectedItem();
    arIF.add(getJSONCondition(idVariableIF,comboIFVarName.getValue().toString(), mfIF));
    Condition condIF = new Condition(idVariableIF,comboIFMFName.getValue().toString());
    condIF.setMembershipFunction(mfIF);
    ifMap.put(comboIFVarName.getValue().toString(),condIF);


    JSONArray arThen = new JSONArray();
    MembershipFunction mfThen = (MembershipFunction) comboThenMFName.getSelectionModel().getSelectedItem();
    int idVariableTHEN = mapOutputVariables.get(comboThenVarName.getValue()).getId();
    arThen.add(getJSONCondition(idVariableTHEN,comboThenVarName.getValue().toString(),mfThen));

    Condition condTHEN = new Condition(idVariableTHEN,comboThenMFName.getValue().toString());
    condTHEN.setMembershipFunction(mfThen);
    thenMap.put(comboThenVarName.getValue().toString(),condTHEN);


    arAND = new JSONArray();
    if(conditionList.size()==0) {
      if(comboAndVarName.getValue().toString().isEmpty()) return;
      int idVariableAND = mapInputVariables.get(comboAndVarName.getValue()).getId();
      MembershipFunction mfAnd = (MembershipFunction) comboAndMFName.getSelectionModel().getSelectedItem();
      arAND.add(getJSONCondition(idVariableAND,comboAndVarName.getValue().toString(),mfAnd));

      Condition condAND = new Condition(idVariableAND,comboAndVarName.getValue().toString());
      condAND.setMembershipFunction(mfAnd);
      andMap.put(comboAndVarName.getValue().toString(),condAND);


    }else{
      JSONObject object = new JSONObject();
      for(int i = 0;i<conditionList.size();i++){
        Condition condition = conditionList.get(i);
        int idVariable = mapInputVariables.get(condition.getNameVariable()).getId();
        object = getJSONCondition(idVariable,condition.getNameVariable(),condition.getMembershipFunction());
        arAND.add(object);
        andMap.put(condition.getNameVariable(),condition);
      }
    }

    obj.put("AND",arAND);

    obj.put("IF",arIF);

    obj.put("THEN",arThen);

    rule.setValue(obj.toString());
    rule.setIFConditionMap(ifMap);
    rule.setANDConditionMap(andMap);
    rule.setTHENConditionMap(andMap);
    StringBuilder builder = new StringBuilder();
    builder.append(DaoUtils.getRuleDescr(ifMap,"Если "));
    builder.append(DaoUtils.getRuleDescr(andMap, " И "));
    builder.append(DaoUtils.getRuleDescr(thenMap," ТОГДА "));
    rule.setDescr(builder.toString());


    if(rule.getIdRule()==0){
      DaoUtils.insertRule(rule,idVariableIF);
    }else{
      DaoUtils.updateRule(rule,idVariableIF,rule.getIdRule());
    }


    System.out.println(obj.toString());
    btnCancel.fire();
  }

  public void actionClose(ActionEvent actionEvent) {
    Node source = (Node) actionEvent.getSource();
    Stage stage = (Stage) source.getScene().getWindow();
    stage.hide();
  }
}