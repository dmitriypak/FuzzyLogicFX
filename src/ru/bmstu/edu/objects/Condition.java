package ru.bmstu.edu.objects;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Map;

public class Condition {

  private MembershipFunction IFmf;
  private Map<String,MembershipFunction> ANDmfList;
  private Map<String,MembershipFunction> THENmfList;

  private SimpleIntegerProperty idVariable = new SimpleIntegerProperty(0);
  private SimpleStringProperty nameVariable = new SimpleStringProperty("");
  private SimpleStringProperty value = new SimpleStringProperty("");
  private SimpleStringProperty type = new SimpleStringProperty("");

  /////////////////////////


  public MembershipFunction getIFmf() {
    return IFmf;
  }

  public void setIFmf(MembershipFunction IFmf) {
    this.IFmf = IFmf;
  }


  public int getId() {
    return idVariable.get();
  }

  public void setNameVariable(String nameVariable) {
    this.nameVariable.set(nameVariable);
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

  public String getNameVariable() {
    return nameVariable.get();
  }

  public SimpleStringProperty nameVariableProperty() {
    return nameVariable;
  }

  public Condition() {

  }

  public Condition(int idVariable, String nameVariable, String valueMF) {
    this.nameVariable = new SimpleStringProperty(nameVariable);
    this.idVariable = new SimpleIntegerProperty(idVariable);
    this.value = new SimpleStringProperty(valueMF);
  }

}
