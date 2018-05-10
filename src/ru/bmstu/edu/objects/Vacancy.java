package ru.bmstu.edu.objects;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Vacancy {
  private SimpleIntegerProperty id = new SimpleIntegerProperty(0);
  private SimpleStringProperty name = new SimpleStringProperty("");
  private SimpleIntegerProperty wages = new SimpleIntegerProperty(0);

  /////////////

  public Vacancy() {

  }
  public Vacancy(SimpleStringProperty name, SimpleIntegerProperty wages) {
    this.name = name;
    this.wages = wages;
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

  public int getWages() {
    return wages.get();
  }

  public SimpleIntegerProperty wagesProperty() {
    return wages;
  }

  public void setWages(int wages) {
    this.wages.set(wages);
  }
}
