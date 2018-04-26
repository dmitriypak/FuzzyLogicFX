package ru.bmstu.edu.objects;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Rule {

  private SimpleIntegerProperty idVariable = new SimpleIntegerProperty(0);
  private SimpleStringProperty nameVariable = new SimpleStringProperty("");
  private SimpleStringProperty valueMF = new SimpleStringProperty("");

  public int getId() {
    return idVariable.get();
  }

  public SimpleIntegerProperty idProperty() {
    return idVariable;
  }

  public void setId(int id) {
    this.idVariable.set(id);
  }

  public String getNameVariable() {
    return nameVariable.get();
  }

  public SimpleStringProperty nameVariableProperty() {
    return nameVariable;
  }

  public void setNameVariable(String nameVariable) {
    this.nameVariable.set(nameVariable);
  }

  public String getValueMF() {
    return valueMF.get();
  }

  public SimpleStringProperty valueMFProperty() {
    return valueMF;
  }

  public void setValueMF(String valueMF) {
    this.valueMF.set(valueMF);
  }

  public Rule() {

  }

  public Rule(int idVariable, String nameVariable,String valueMF) {
    this.idVariable = new SimpleIntegerProperty(idVariable);
    this.nameVariable = new SimpleStringProperty(nameVariable);
    this.valueMF = new SimpleStringProperty(valueMF);

  }

}
