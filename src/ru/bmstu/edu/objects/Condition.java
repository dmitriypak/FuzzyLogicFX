package ru.bmstu.edu.objects;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Condition {

  private SimpleIntegerProperty idVariable = new SimpleIntegerProperty(0);

  public String getNameVariable() {
    return nameVariable.get();
  }

  public SimpleStringProperty nameVariableProperty() {
    return nameVariable;
  }

  public void setNameVariable(String nameVariable) {
    this.nameVariable.set(nameVariable);
  }

  private SimpleStringProperty nameVariable = new SimpleStringProperty("");
  private SimpleStringProperty value = new SimpleStringProperty("");
  private SimpleStringProperty type = new SimpleStringProperty("");

  public int getId() {
    return idVariable.get();
  }

  public SimpleIntegerProperty idProperty() {
    return idVariable;
  }

  public void setId(int id) {
    this.idVariable.set(id);
  }

  public String getType() {
    return type.get();
  }

  public SimpleStringProperty typeProperty() {
    return type;
  }

  public void setType(String type) {
    this.type.set(type);
  }

  public String getValue() {
    return value.get();
  }

  public SimpleStringProperty valueMFProperty() {
    return value;
  }

  public void setValue(String valueMF) {
    this.value.set(valueMF);
  }

  public Condition() {

  }

  public Condition(int idVariable, String nameVariable, String valueMF) {
    this.nameVariable = new SimpleStringProperty(nameVariable);
    this.idVariable = new SimpleIntegerProperty(idVariable);
    this.value = new SimpleStringProperty(valueMF);

  }

}
