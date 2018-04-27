package ru.bmstu.edu.objects;

import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;

public class Rule {
  private ArrayList<Condition> conditionsList = new ArrayList<>();
  private SimpleStringProperty value = new SimpleStringProperty();

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




}
