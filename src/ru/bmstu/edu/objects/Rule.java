package ru.bmstu.edu.objects;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;

public class Rule {
  private SimpleIntegerProperty idRule = new SimpleIntegerProperty(0);
  private ArrayList<Condition> conditionsList = new ArrayList<>();
  private SimpleStringProperty value = new SimpleStringProperty();

  public int getIdRule() {
    return idRule.get();
  }

  public SimpleIntegerProperty idRuleProperty() {
    return idRule;
  }

  public void setIdRule(int idRule) {
    this.idRule.set(idRule);
  }



  public String getValue() {
    return value.get();
  }

  public SimpleStringProperty valueProperty() {
    return value;
  }

  public void setValue(String value) {
    this.value.set(value);
  }

  public Rule(ArrayList<Condition> conditionsList) {

    this.conditionsList = conditionsList;
  }

  public Rule() {
  }

  public Rule(int idRule, String value) {
    this.idRule = new SimpleIntegerProperty(idRule);
    this.value = new SimpleStringProperty(value);
  }
}
