package ru.bmstu.edu.objects;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LinguisticVariable {
  private SimpleStringProperty name = new SimpleStringProperty("");
  private SimpleIntegerProperty id = new SimpleIntegerProperty(0);
  private SimpleStringProperty description = new SimpleStringProperty("");
  private SimpleStringProperty value = new SimpleStringProperty("");
  private ObservableList<MembershipFunction> mfList = FXCollections.observableArrayList();

  public ObservableList<MembershipFunction> getMfList() {
    return mfList;
  }

  public void setMfList(ObservableList<MembershipFunction> mfList) {
    this.mfList = mfList;
  }

  public LinguisticVariable(){

  }

  public LinguisticVariable(int id, String name, String value){
    this.id = new SimpleIntegerProperty(id);
    this.name = new SimpleStringProperty(name);
    this.value = new SimpleStringProperty(value);
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

  public int getId() {
    return id.get();
  }

  public SimpleIntegerProperty idProperty() {
    return id;
  }

  public void setId(int id) {
    this.id.set(id);
  }

  public String getDescription() {
    return description.get();
  }

  public SimpleStringProperty descriptionProperty() {
    return description;
  }

  public void setDescription(String description) {
    this.description.set(description);
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
