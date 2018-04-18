package ru.bmstu.edu.objects;

import java.sql.Date;

public class Workexperience {
  private long id;
  private String idowner;
  private String companyName;
  private Date dateto;
  private String type;
  private String demands;
  private Date datefrom;
  private String jobtitle;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getIdowner() {
    return idowner;
  }

  public void setIdowner(String idowner) {
    this.idowner = idowner;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public Date getDateto() {
    return dateto;
  }

  public void setDateto(Date dateto) {
    this.dateto = dateto;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getDemands() {
    return demands;
  }

  public void setDemands(String demands) {
    this.demands = demands;
  }

  public Date getDatefrom() {
    return datefrom;
  }

  public void setDatefrom(Date datefrom) {
    this.datefrom = datefrom;
  }

  public String getJobtitle() {
    return jobtitle;
  }

  public void setJobtitle(String jobtitle) {
    this.jobtitle = jobtitle;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Workexperience that = (Workexperience) o;

    if (id != that.id) return false;
    if (idowner != null ? !idowner.equals(that.idowner) : that.idowner != null) return false;
    if (companyName != null ? !companyName.equals(that.companyName) : that.companyName != null) return false;
    if (dateto != null ? !dateto.equals(that.dateto) : that.dateto != null) return false;
    if (type != null ? !type.equals(that.type) : that.type != null) return false;
    if (demands != null ? !demands.equals(that.demands) : that.demands != null) return false;
    if (datefrom != null ? !datefrom.equals(that.datefrom) : that.datefrom != null) return false;
    if (jobtitle != null ? !jobtitle.equals(that.jobtitle) : that.jobtitle != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (idowner != null ? idowner.hashCode() : 0);
    result = 31 * result + (companyName != null ? companyName.hashCode() : 0);
    result = 31 * result + (dateto != null ? dateto.hashCode() : 0);
    result = 31 * result + (type != null ? type.hashCode() : 0);
    result = 31 * result + (demands != null ? demands.hashCode() : 0);
    result = 31 * result + (datefrom != null ? datefrom.hashCode() : 0);
    result = 31 * result + (jobtitle != null ? jobtitle.hashCode() : 0);
    return result;
  }
}
