package ru.bmstu.edu.objects.enums;

public enum MFname {
  Z("Z","Zero","Положительное близкое к нулю"),
  PS ("PS","Positive Small","Положительное малое"),
  PM("PM","Positive Middle","Положительное среднее"),
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

  @Override
  public String toString()  {
    return this.name;
  }


}
