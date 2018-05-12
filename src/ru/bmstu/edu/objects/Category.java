package ru.bmstu.edu.objects;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Map;

public class Category {
  private SimpleIntegerProperty id = new SimpleIntegerProperty(0);
  private SimpleStringProperty name = new SimpleStringProperty("");

  public Map<LinguisticVariable, String> getLinguisticVariableStringMap() {
    return linguisticVariableStringMap;
  }

  public void setLinguisticVariableStringMap(Map<LinguisticVariable, String> linguisticVariableStringMap) {
    this.linguisticVariableStringMap = linguisticVariableStringMap;
  }

  private Map<LinguisticVariable,String> linguisticVariableStringMap;


  public Category() {

  }

  public Category(SimpleStringProperty name, SimpleStringProperty value) {
    this.name = name;
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
