package ru.bmstu.edu.objects;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Rule {
  private SimpleIntegerProperty idRule = new SimpleIntegerProperty(0);

  private Condition condition;

  private SimpleStringProperty value = new SimpleStringProperty("");
  private SimpleStringProperty variableName = new SimpleStringProperty("");
  private Boolean isactive;

  public Boolean getIsactive() {
    return isactive;
  }

  public void setIsactive(Boolean isactive) {
    this.isactive = isactive;
  }

  public String getVariableName() {
    return variableName.get();
  }

  public SimpleStringProperty variableNameProperty() {
    return variableName;
  }

  public void setVariableName(String variableName) {
    this.variableName.set(variableName);
  }

  public Rule(String variableName) {
    this.variableName = new SimpleStringProperty(variableName);
  }





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


  public Rule() {
  }

  public Rule(int idRule, String value, Boolean state) {
    this.idRule = new SimpleIntegerProperty(idRule);
    this.value = new SimpleStringProperty(value);
    this.isactive = state;
  }
}
