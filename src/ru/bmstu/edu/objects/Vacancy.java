package ru.bmstu.edu.objects;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Vacancy {
  private SimpleIntegerProperty id = new SimpleIntegerProperty(0);
  private SimpleIntegerProperty idProject = new SimpleIntegerProperty(0);
  private SimpleStringProperty name = new SimpleStringProperty("");
  private SimpleStringProperty value = new SimpleStringProperty("");
  private SimpleIntegerProperty wages = new SimpleIntegerProperty(0);
  private SimpleIntegerProperty amountFree = new SimpleIntegerProperty(0);
  private SimpleIntegerProperty amountTotal = new SimpleIntegerProperty(0);
  private SimpleIntegerProperty amountBusy = new SimpleIntegerProperty(0);
  private SimpleStringProperty nameCategory = new SimpleStringProperty("");

  /////////////

  public int getAmountFree() {
    return amountFree.get();
  }

  public SimpleIntegerProperty amountFreeProperty() {
    return amountFree;
  }

  public void setAmountFree(int amountFree) {
    this.amountFree.set(amountFree);
  }

  public int getAmountBusy() {
    return amountBusy.get();
  }

  public SimpleIntegerProperty amountBusyProperty() {
    return amountBusy;
  }

  public void setAmountBusy(int amountBusy) {
    this.amountBusy.set(amountBusy);
  }

  public String getNameCategory() {
    return nameCategory.get();
  }

  public SimpleStringProperty nameCategoryProperty() {
    return nameCategory;
  }

  public void setNameCategory(String nameCategory) {
    this.nameCategory.set(nameCategory);
  }

  public int getAmountTotal() {
    return amountTotal.get();
  }

  public SimpleIntegerProperty amountTotalProperty() {
    return amountTotal;
  }

  public void setAmountTotal(int amountTotal) {
    this.amountTotal.set(amountTotal);
  }

  public Vacancy() {


  }
  public Vacancy(int id, int idProject, String name, int wages, String value, int amountTotal, int amountBusy) {
    this.id = new SimpleIntegerProperty(id);
    this.idProject = new SimpleIntegerProperty(idProject);
    this.name = new SimpleStringProperty(name);
    this.wages = new SimpleIntegerProperty(wages);
    this.value = new SimpleStringProperty(value);
    this.amountTotal = new SimpleIntegerProperty(amountTotal);
    this.amountBusy = new SimpleIntegerProperty(amountBusy);
    int diff = amountTotal-amountBusy;
    this.amountFree = new SimpleIntegerProperty(diff);

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
