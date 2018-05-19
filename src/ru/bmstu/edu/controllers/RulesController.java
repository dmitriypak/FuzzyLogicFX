package ru.bmstu.edu.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.CheckTreeView;
import org.json.simple.parser.ParseException;
import ru.bmstu.edu.objects.Condition;
import ru.bmstu.edu.objects.Rule;
import ru.bmstu.edu.objects.utils.DaoUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public class RulesController {

  @FXML
  private CheckTreeView treeViewRules;
  @FXML
  private TableView tableRules;
  @FXML
  private TableColumn colRule;
  @FXML
  private Button btnEditRule;



  private Node nodesource;
  private EditRuleController editRuleController;

  private Parent fxmlEdit;
  private FXMLLoader fxmlLoader = new FXMLLoader();
  private Stage editRuleControllerStage;


  private ObservableList<Rule> rulesList;

  @FXML
  private void initialize() throws ParseException {
    colRule.setCellValueFactory(new PropertyValueFactory<Rule, String>("descr"));

    try {
      fxmlLoader.setLocation(getClass().getResource("../fxml/editRule.fxml"));
      fxmlEdit = fxmlLoader.load();
      editRuleController = fxmlLoader.getController();
    } catch (IOException e) {
      e.printStackTrace();
    }
    tableRules.setOnMouseClicked( event -> {
      if( event.getClickCount() == 2 ) {
        btnEditRule.fire();
      }});

    fillData();
  }


  private void fillData() throws ParseException {
    StringBuilder stringBuilder = new StringBuilder();
    rulesList = FXCollections.observableArrayList(DaoUtils.getRules());
    StringBuilder builder = new StringBuilder();
    if(rulesList.size()>0){
      for(int i = 0;i<rulesList.size();i++){
        Rule rule = rulesList.get(i);
        Map<String, Condition> mapIF = rule.getIFConditionMap();
        Map<String, Condition> mapAND = rule.getANDConditionMap();
        Map<String, Condition> mapTHEN = rule.getTHENConditionMap();

        //IF
        for(Map.Entry<String, Condition> entry:mapIF.entrySet()){
          String condition = entry.getKey();
          Condition cond = entry.getValue();
          builder.append("Если ").append(condition).append(" ").append(cond.getMembershipFunction().getNameMF());
        }
        //AND
        for(Map.Entry<String, Condition> entry:mapAND.entrySet()){
          String condition = entry.getKey();
          Condition cond = entry.getValue();
          builder.append(" И ").append(condition).append(" ").append(cond.getMembershipFunction().getNameMF());
        }

        //THEN
        for(Map.Entry<String, Condition> entry:mapTHEN.entrySet()){
          String condition = entry.getKey();
          Condition cond = entry.getValue();
          builder.append(" ТОГДА ").append(condition).append(" ").append(cond.getMembershipFunction().getNameMF());
        }


        rule.setDescr(builder.toString());
        builder.setLength(0);
      }

      tableRules.setItems(rulesList);
    }

  }

  //Для CheckTreeView

//  private void fillData() {
//    ArrayList<LinguisticVariable> listVariables = DaoUtils.getInputVariables();
//
//    CheckBoxTreeItem<String> root = new CheckBoxTreeItem<String>("Переменные");
//    root.setExpanded(true);
//    for(LinguisticVariable linguisticVariable:listVariables){
//      ArrayList<Rule> listRules = DaoUtils.getRulesByVariable(linguisticVariable.getId()) ;
//      CheckBoxTreeItem<String> variable = new CheckBoxTreeItem<String>(linguisticVariable.getName());
//      variable.setExpanded(true);
//      for(Rule r:listRules){
//        //variable.getChildren().add(new CheckBoxTreeItem<String>(r.getValue()));
//        variable.getChildren().add(new CheckBoxTreeItem<String>(r.getValue()));
//      }
//      root.getChildren().add(variable);
//    }
//
//    treeViewRules.setRoot(root);
//    treeViewRules.getCheckModel().getCheckedItems().addListener(new ListChangeListener<TreeItem<String>>() {
//      public void onChanged(ListChangeListener.Change<? extends TreeItem<String>> c) {
//        System.out.println(treeViewRules.getCheckModel().getCheckedItems());
//      }
//    });
//  }

//    Построение TreeTableView

//  private void fillData() {
//    ArrayList<LinguisticVariable> listVariables = DaoUtils.getInputVariables();
//    Rule rootRule = new Rule("Переменные");
//
//    TreeItem<Rule> itemRoot = new TreeItem<>(new Rule());
//
//    for(LinguisticVariable linguisticVariable:listVariables){
//      ArrayList<Rule> listRules = DaoUtils.getRulesByVariable(linguisticVariable.getId());
//      if(listRules.size()>0){
//        //TreeItem<LinguisticVariable> itemRoot = new TreeItem<>(linguisticVariable);
//        Rule rule1 = new Rule(linguisticVariable.getName());
//        TreeItem<Rule> itemRoot1 = new TreeItem<>(rule1);
//
//        for(Rule rule:listRules){
//          TreeItem<Rule> itemRule = new TreeItem<>(rule);
//          itemRoot1.getChildren().add(itemRule);
//        }
//        itemRoot.getChildren().add((itemRoot1));
//      }
//    }
//    tableRules.setRoot(itemRoot);
//  }



  private void showDialog() {
    if (editRuleControllerStage==null) {
      editRuleControllerStage = new Stage();
      editRuleControllerStage.setTitle("Конструктор правил");
      editRuleControllerStage.setMinHeight(150);
      editRuleControllerStage.setMinWidth(300);
      editRuleControllerStage.setResizable(false);
      editRuleControllerStage.setScene(new Scene(fxmlEdit));
      editRuleControllerStage.initModality(Modality.WINDOW_MODAL);
      editRuleControllerStage.initOwner((Stage) nodesource.getScene().getWindow());
    }
    editRuleControllerStage.showAndWait();

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
        Rule rule = new Rule();
        rule.setIdRule(0);
        editRuleController.setRule(rule);
        showDialog();
        rule = editRuleController.getRule();
        rulesList.add(rule);
        break;
      case "btnEditRule":
        editRuleController.setRule((Rule) tableRules.getSelectionModel().getSelectedItem());
        showDialog();

        break;
      case "btnDeleteRule":
        Rule delRule;
        delRule = (Rule)tableRules.getSelectionModel().getSelectedItem();
        DaoUtils.deleteRule(delRule.getIdRule());
        rulesList.remove(delRule);
        break;
    }
  }
}
