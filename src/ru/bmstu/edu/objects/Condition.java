package ru.bmstu.edu.objects;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Condition {

  private MembershipFunction membershipFunction;
  private SimpleIntegerProperty idVariable = new SimpleIntegerProperty(0);
  private SimpleStringProperty nameVariable = new SimpleStringProperty("");
  private SimpleStringProperty valueMF = new SimpleStringProperty("");
  private SimpleStringProperty type = new SimpleStringProperty("");

  /////////////////////////

  public MembershipFunction getMembershipFunction() {
    return membershipFunction;
  }

  public void setMembershipFunction(MembershipFunction membershipFunction) {
    this.membershipFunction = membershipFunction;
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

  public String getValueMF() {
    return valueMF.get();
  }

  public SimpleStringProperty valueMFProperty() {
    return valueMF;
  }

  public void setValueMF(String valueMF) {
    this.valueMF.set(valueMF);
  }

  public String getNameVariable() {
    return nameVariable.get();
  }

  public SimpleStringProperty nameVariableProperty() {
    return nameVariable;
  }

  public Condition() {

  }


  public Condition(int idVariable, String nameVariable) {
    this.nameVariable = new SimpleStringProperty(nameVariable);
    this.idVariable = new SimpleIntegerProperty(idVariable);
  }

}
