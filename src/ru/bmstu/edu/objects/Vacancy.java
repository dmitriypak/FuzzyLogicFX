package ru.bmstu.edu.objects;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Vacancy {
  private SimpleIntegerProperty id = new SimpleIntegerProperty(0);
  private SimpleIntegerProperty idProject = new SimpleIntegerProperty(0);
  private SimpleStringProperty name = new SimpleStringProperty("");
  private SimpleStringProperty value = new SimpleStringProperty("");
  private SimpleIntegerProperty wages = new SimpleIntegerProperty(0);

  /////////////

  public Vacancy() {

  }
  public Vacancy(int id, int idProject, String name, int wages, String value) {
    this.id = new SimpleIntegerProperty(id);
    this.idProject = new SimpleIntegerProperty(idProject);
    this.name = new SimpleStringProperty(name);
    this.wages = new SimpleIntegerProperty(wages);
    this.value = new SimpleStringProperty(value);
  }


  public int getIdProject() {
    return idProject.get();
  }

  public SimpleIntegerProperty idProjectProperty() {
    return idProject;
  }

  public void setIdProject(int idProject) {
    this.idProject.set(idProject);
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
