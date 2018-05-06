package ru.bmstu.edu.objects;

import javafx.beans.property.SimpleStringProperty;

public class MembershipFunction {
  private SimpleStringProperty MFname = new SimpleStringProperty("");
  private SimpleStringProperty MFParamValue = new SimpleStringProperty("");

  public MembershipFunction(){

  }

  public MembershipFunction(String MFname, String MFParamValue) {
    this.MFname = new SimpleStringProperty(MFname);
    this.MFParamValue = new SimpleStringProperty(MFParamValue);
  }
  public MembershipFunction(String MFname) {
    this.MFname = new SimpleStringProperty(MFname);
  }



  public String getMFname() {

    return MFname.get();
  }

  public SimpleStringProperty MFnameProperty() {
    return MFname;
  }

  public void setMFname(String MFname) {
    this.MFname.set(MFname);
  }

  public String getMFParamValue() {
    return MFParamValue.get();
  }

  public SimpleStringProperty MFParamValueProperty() {
    return MFParamValue;
  }

  public void setMFParamValue(String MFParamValue) {
    this.MFParamValue.set(MFParamValue);
  }




}
