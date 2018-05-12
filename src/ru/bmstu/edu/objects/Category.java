package ru.bmstu.edu.objects;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Map;

public class Category {
  private SimpleIntegerProperty id = new SimpleIntegerProperty(0);
  private SimpleStringProperty name = new SimpleStringProperty("");
  private SimpleStringProperty value = new SimpleStringProperty("");

  public String getValue() {
    return value.get();
  }

  public SimpleStringProperty valueProperty() {
    return value;
  }

  public void setValue(String value) {
    this.value.set(value);
  }

  public Map<LinguisticVariable, String> getLinguisticVariableStringMap() {
    return linguisticVariableStringMap;
  }

  public void setLinguisticVariableStringMap(Map<LinguisticVariable, String> linguisticVariableStringMap) {
    this.linguisticVariableStringMap = linguisticVariableStringMap;
  }

  private Map<LinguisticVariable,String> linguisticVariableStringMap;


  public Category() {

  }

  public Category(int id, String name, String value) {
    this.id = new SimpleIntegerProperty(id);
    this.name = new SimpleStringProperty(name);
    this.value = new SimpleStringProperty(value);
  }


  public int getId() {
    return id.get();
  }

  public SimpleIntegerProperty idProperty() {
    return id;
  }

  public void setId(int id) {
    this.id.set(id);
  }

  public String getName() {
    return name.get();
  }

  public SimpleStringProperty nameProperty() {
    return name;
  }

  public void setName(String name) {
    this.name.set(name);
  }


}
