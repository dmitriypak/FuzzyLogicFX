package ru.bmstu.edu.objects;

public class Worldskills {
  private long id;
  private String internationalname;
  private String russianname;
  private String skillabbreviation;
  private String isinternational;
  private String type;
  private String idowner;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getInternationalname() {
    return internationalname;
  }

  public void setInternationalname(String internationalname) {
    this.internationalname = internationalname;
  }

  public String getRussianname() {
    return russianname;
  }

  public void setRussianname(String russianname) {
    this.russianname = russianname;
  }

  public String getSkillabbreviation() {
    return skillabbreviation;
  }

  public void setSkillabbreviation(String skillabbreviation) {
    this.skillabbreviation = skillabbreviation;
  }

  public String getIsinternational() {
    return isinternational;
  }

  public void setIsinternational(String isinternational) {
    this.isinternational = isinternational;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getIdowner() {
    return idowner;
  }

  public void setIdowner(String idowner) {
    this.idowner = idowner;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Worldskills that = (Worldskills) o;

    if (id != that.id) return false;
    if (internationalname != null ? !internationalname.equals(that.internationalname) : that.internationalname != null)
      return false;
    if (russianname != null ? !russianname.equals(that.russianname) : that.russianname != null) return false;
    if (skillabbreviation != null ? !skillabbreviation.equals(that.skillabbreviation) : that.skillabbreviation != null)
      return false;
    if (isinternational != null ? !isinternational.equals(that.isinternational) : that.isinternational != null)
      return false;
    if (type != null ? !type.equals(that.type) : that.type != null) return false;
    if (idowner != null ? !idowner.equals(that.idowner) : that.idowner != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (internationalname != null ? internationalname.hashCode() : 0);
    result = 31 * result + (russianname != null ? russianname.hashCode() : 0);
    result = 31 * result + (skillabbreviation != null ? skillabbreviation.hashCode() : 0);
    result = 31 * result + (isinternational != null ? isinternational.hashCode() : 0);
    result = 31 * result + (type != null ? type.hashCode() : 0);
    result = 31 * result + (idowner != null ? idowner.hashCode() : 0);
    return result;
  }
}
