package ru.bmstu.edu.objects;

import java.sql.Date;

public class CV {
  private long id;
  private Date creationdate;
  private String candidateid;
  private String locality;
  private String positionname;
  private String idowner;
  private String drivelicences;
  private String countryName;
  private Date publishdate;
  private String scheduletype;
  private Integer experience;
  private Integer salary;
  private String skills;
  private String additionalskills;
  private String busytype;
  private String relocation;
  private String businesstrips;
  private String addcertificates;
  private String retrainingcapability;
  private String iduser;
  private String status;
  private String visibility;
  private Date datemodify;
  private String deleted;
  private String fullnessrate;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Date getCreationdate() {
    return creationdate;
  }

  public void setCreationdate(Date creationdate) {
    this.creationdate = creationdate;
  }

  public String getCandidateid() {
    return candidateid;
  }

  public void setCandidateid(String candidateid) {
    this.candidateid = candidateid;
  }

  public String getLocality() {
    return locality;
  }

  public void setLocality(String locality) {
    this.locality = locality;
  }

  public String getPositionname() {
    return positionname;
  }

  public void setPositionname(String positionname) {
    this.positionname = positionname;
  }

  public String getIdowner() {
    return idowner;
  }

  public void setIdowner(String idowner) {
    this.idowner = idowner;
  }

  public String getDrivelicences() {
    return drivelicences;
  }

  public void setDrivelicences(String drivelicences) {
    this.drivelicences = drivelicences;
  }

  public String getCountryName() {
    return countryName;
  }

  public void setCountryName(String countryName) {
    this.countryName = countryName;
  }

  public Date getPublishdate() {
    return publishdate;
  }

  public void setPublishdate(Date publishdate) {
    this.publishdate = publishdate;
  }

  public String getScheduletype() {
    return scheduletype;
  }

  public void setScheduletype(String scheduletype) {
    this.scheduletype = scheduletype;
  }

  public Integer getExperience() {
    return experience;
  }

  public void setExperience(Integer experience) {
    this.experience = experience;
  }

  public Integer getSalary() {
    return salary;
  }

  public void setSalary(Integer salary) {
    this.salary = salary;
  }

  public String getSkills() {
    return skills;
  }

  public void setSkills(String skills) {
    this.skills = skills;
  }

  public String getAdditionalskills() {
    return additionalskills;
  }

  public void setAdditionalskills(String additionalskills) {
    this.additionalskills = additionalskills;
  }

  public String getBusytype() {
    return busytype;
  }

  public void setBusytype(String busytype) {
    this.busytype = busytype;
  }

  public String getRelocation() {
    return relocation;
  }

  public void setRelocation(String relocation) {
    this.relocation = relocation;
  }

  public String getBusinesstrips() {
    return businesstrips;
  }

  public void setBusinesstrips(String businesstrips) {
    this.businesstrips = businesstrips;
  }

  public String getAddcertificates() {
    return addcertificates;
  }

  public void setAddcertificates(String addcertificates) {
    this.addcertificates = addcertificates;
  }

  public String getRetrainingcapability() {
    return retrainingcapability;
  }

  public void setRetrainingcapability(String retrainingcapability) {
    this.retrainingcapability = retrainingcapability;
  }

  public String getIduser() {
    return iduser;
  }

  public void setIduser(String iduser) {
    this.iduser = iduser;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getVisibility() {
    return visibility;
  }

  public void setVisibility(String visibility) {
    this.visibility = visibility;
  }

  public Date getDatemodify() {
    return datemodify;
  }

  public void setDatemodify(Date datemodify) {
    this.datemodify = datemodify;
  }

  public String getDeleted() {
    return deleted;
  }

  public void setDeleted(String deleted) {
    this.deleted = deleted;
  }

  public String getFullnessrate() {
    return fullnessrate;
  }

  public void setFullnessrate(String fullnessrate) {
    this.fullnessrate = fullnessrate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    CV cv = (CV) o;

    if (id != cv.id) return false;
    if (creationdate != null ? !creationdate.equals(cv.creationdate) : cv.creationdate != null) return false;
    if (candidateid != null ? !candidateid.equals(cv.candidateid) : cv.candidateid != null) return false;
    if (locality != null ? !locality.equals(cv.locality) : cv.locality != null) return false;
    if (positionname != null ? !positionname.equals(cv.positionname) : cv.positionname != null) return false;
    if (idowner != null ? !idowner.equals(cv.idowner) : cv.idowner != null) return false;
    if (drivelicences != null ? !drivelicences.equals(cv.drivelicences) : cv.drivelicences != null) return false;
    if (countryName != null ? !countryName.equals(cv.countryName) : cv.countryName != null) return false;
    if (publishdate != null ? !publishdate.equals(cv.publishdate) : cv.publishdate != null) return false;
    if (scheduletype != null ? !scheduletype.equals(cv.scheduletype) : cv.scheduletype != null) return false;
    if (experience != null ? !experience.equals(cv.experience) : cv.experience != null) return false;
    if (salary != null ? !salary.equals(cv.salary) : cv.salary != null) return false;
    if (skills != null ? !skills.equals(cv.skills) : cv.skills != null) return false;
    if (additionalskills != null ? !additionalskills.equals(cv.additionalskills) : cv.additionalskills != null)
      return false;
    if (busytype != null ? !busytype.equals(cv.busytype) : cv.busytype != null) return false;
    if (relocation != null ? !relocation.equals(cv.relocation) : cv.relocation != null) return false;
    if (businesstrips != null ? !businesstrips.equals(cv.businesstrips) : cv.businesstrips != null) return false;
    if (addcertificates != null ? !addcertificates.equals(cv.addcertificates) : cv.addcertificates != null)
      return false;
    if (retrainingcapability != null ? !retrainingcapability.equals(cv.retrainingcapability) : cv.retrainingcapability != null)
      return false;
    if (iduser != null ? !iduser.equals(cv.iduser) : cv.iduser != null) return false;
    if (status != null ? !status.equals(cv.status) : cv.status != null) return false;
    if (visibility != null ? !visibility.equals(cv.visibility) : cv.visibility != null) return false;
    if (datemodify != null ? !datemodify.equals(cv.datemodify) : cv.datemodify != null) return false;
    if (deleted != null ? !deleted.equals(cv.deleted) : cv.deleted != null) return false;
    if (fullnessrate != null ? !fullnessrate.equals(cv.fullnessrate) : cv.fullnessrate != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (creationdate != null ? creationdate.hashCode() : 0);
    result = 31 * result + (candidateid != null ? candidateid.hashCode() : 0);
    result = 31 * result + (locality != null ? locality.hashCode() : 0);
    result = 31 * result + (positionname != null ? positionname.hashCode() : 0);
    result = 31 * result + (idowner != null ? idowner.hashCode() : 0);
    result = 31 * result + (drivelicences != null ? drivelicences.hashCode() : 0);
    result = 31 * result + (countryName != null ? countryName.hashCode() : 0);
    result = 31 * result + (publishdate != null ? publishdate.hashCode() : 0);
    result = 31 * result + (scheduletype != null ? scheduletype.hashCode() : 0);
    result = 31 * result + (experience != null ? experience.hashCode() : 0);
    result = 31 * result + (salary != null ? salary.hashCode() : 0);
    result = 31 * result + (skills != null ? skills.hashCode() : 0);
    result = 31 * result + (additionalskills != null ? additionalskills.hashCode() : 0);
    result = 31 * result + (busytype != null ? busytype.hashCode() : 0);
    result = 31 * result + (relocation != null ? relocation.hashCode() : 0);
    result = 31 * result + (businesstrips != null ? businesstrips.hashCode() : 0);
    result = 31 * result + (addcertificates != null ? addcertificates.hashCode() : 0);
    result = 31 * result + (retrainingcapability != null ? retrainingcapability.hashCode() : 0);
    result = 31 * result + (iduser != null ? iduser.hashCode() : 0);
    result = 31 * result + (status != null ? status.hashCode() : 0);
    result = 31 * result + (visibility != null ? visibility.hashCode() : 0);
    result = 31 * result + (datemodify != null ? datemodify.hashCode() : 0);
    result = 31 * result + (deleted != null ? deleted.hashCode() : 0);
    result = 31 * result + (fullnessrate != null ? fullnessrate.hashCode() : 0);
    return result;
  }
}
