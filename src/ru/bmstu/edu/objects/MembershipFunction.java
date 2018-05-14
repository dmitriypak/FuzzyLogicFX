package ru.bmstu.edu.objects;

import javafx.beans.property.SimpleStringProperty;
import ru.bmstu.edu.objects.enums.MFname;

public class MembershipFunction {
  private SimpleStringProperty nameMF = new SimpleStringProperty("");
  private SimpleStringProperty paramValueMF = new SimpleStringProperty("");
  private SimpleStringProperty codeMF = new SimpleStringProperty("");
  private ru.bmstu.edu.objects.enums.MFname mFname;

  /////////////

  public MembershipFunction(){

  }

  public String getCodeMF() {
    return mFname.getCode();
  }

  public SimpleStringProperty codeMFProperty() {
    return codeMF;
  }


  public MembershipFunction(String nameMF, String paramValueMF, String mfCode) {
    this.nameMF = new SimpleStringProperty(nameMF);
    this.paramValueMF = new SimpleStringProperty(paramValueMF);
    this.mFname = MFname.getMFnameByCode(mfCode);
    this.codeMF = new SimpleStringProperty(mfCode);
  }
  public MembershipFunction(String nameMF) {
    this.nameMF = new SimpleStringProperty(nameMF);
  }

  public String getNameMF() {
    return nameMF.get();
  }

  public SimpleStringProperty nameMFProperty() {
    return nameMF;
  }

  public void setNameMF(String nameMF) {
    this.nameMF.set(nameMF);
  }

  public String getParamValueMF() {
    return paramValueMF.get();
  }

  public SimpleStringProperty paramValueMFProperty() {
    return paramValueMF;
  }

  public void setParamValueMF(String paramValueMF) {
    this.paramValueMF.set(paramValueMF);
  }

  public MFname getmFname() {
    return mFname;
  }

  public void setmFname(MFname mFname) {
    this.mFname = mFname;
    this.codeMF =new SimpleStringProperty(mFname.getCode());
  }

  @Override
  public String toString() {
    return nameMF.get();
  }
}
