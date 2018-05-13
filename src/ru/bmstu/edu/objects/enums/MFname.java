package ru.bmstu.edu.objects.enums;

public enum MFname {
  Z("Z","Zero","Нуль, близкое к нулю"),
  PLS("PLS","Positive Lower Small", "Положительное меньше малого"),
  PS ("PS","Positive Small","Положительное малое"),
  PLM("PLM","Positive Lower Middle", "Положительное меньше среднего"),
  PM("PM","Positive Middle","Положительное среднее"),
  PLB("PLB","Positive Lower Big", "Положительное меньше большего"),
  PB("PB","Positive Big", "Положительное большое");

  private String code;
  private String nameEng;
  private String name;

  MFname(String code, String nameEng, String name) {
    this.code = code;
    this.nameEng = nameEng;
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String getCode() {
    return code;
  }

  public static MFname getMFnameByCode(String code) {
    for (MFname mFname : MFname.values()) {
      if (mFname.code.equalsIgnoreCase(code)) {
        return mFname;
      }
    }
    return null;
  }

  public static MFname getMFnameByName(String name) {
    for (MFname mFname : MFname.values()) {
      if (mFname.name.equalsIgnoreCase(name)) {
        return mFname;
      }
    }
    return null;
  }


  @Override
  public String toString()  {
    return this.name;
  }


}
