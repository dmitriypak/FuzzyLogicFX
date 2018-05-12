package ru.bmstu.edu.objects;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Category {
  private SimpleIntegerProperty id = new SimpleIntegerProperty(0);
  private SimpleStringProperty name = new SimpleStringProperty("");
  private SimpleStringProperty value = new SimpleStringProperty("");

  public Category() {

  }

  public Category(SimpleStringProperty name, SimpleStringProperty value) {
    this.name = name;
    this.value = value;
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

  public String getValue() {
    return value.get();
  }

  public SimpleStringProperty valueProperty() {
    return value;
  }

  public void setValue(String value) {
    this.value.set(value);
  }
}
