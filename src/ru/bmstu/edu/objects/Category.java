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



}
