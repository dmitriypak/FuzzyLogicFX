package ru.bmstu.edu.objects;

public class Education {
  private String specialty;
  private String idowner;
  private String type;
  private String legalname;
  private long id;
  private int graduateyear;
  private String faculty;

  public String getSpecialty() {
    return specialty;
  }

  public void setSpecialty(String specialty) {
    this.specialty = specialty;
  }

  public String getIdowner() {
    return idowner;
  }

  public void setIdowner(String idowner) {
    this.idowner = idowner;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getLegalname() {
    return legalname;
  }

  public void setLegalname(String legalname) {
    this.legalname = legalname;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public int getGraduateyear() {
    return graduateyear;
  }

  public void setGraduateyear(int graduateyear) {
    this.graduateyear = graduateyear;
  }

  public String getFaculty() {
    return faculty;
  }

  public void setFaculty(String faculty) {
    this.faculty = faculty;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Education education = (Education) o;

    if (id != education.id) return false;
    if (graduateyear != education.graduateyear) return false;
    if (specialty != null ? !specialty.equals(education.specialty) : education.specialty != null) return false;
    if (idowner != null ? !idowner.equals(education.idowner) : education.idowner != null) return false;
    if (type != null ? !type.equals(education.type) : education.type != null) return false;
    if (legalname != null ? !legalname.equals(education.legalname) : education.legalname != null) return false;
    if (faculty != null ? !faculty.equals(education.faculty) : education.faculty != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = specialty != null ? specialty.hashCode() : 0;
    result = 31 * result + (idowner != null ? idowner.hashCode() : 0);
    result = 31 * result + (type != null ? type.hashCode() : 0);
    result = 31 * result + (legalname != null ? legalname.hashCode() : 0);
    result = 31 * result + (int) (id ^ (id >>> 32));
    result = 31 * result + graduateyear;
    result = 31 * result + (faculty != null ? faculty.hashCode() : 0);
    return result;
  }
}
