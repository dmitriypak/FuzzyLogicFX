package ru.bmstu.edu.objects;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class CV {
  private SimpleIntegerProperty id = new SimpleIntegerProperty(0);
  private SimpleStringProperty creationdate = new SimpleStringProperty("");
  private SimpleStringProperty candidateid = new SimpleStringProperty("");
  private SimpleStringProperty locality = new SimpleStringProperty("");
  private SimpleStringProperty positionname = new SimpleStringProperty("");
  private SimpleStringProperty idowner = new SimpleStringProperty("");
  private SimpleStringProperty drivelicences = new SimpleStringProperty("");
  private SimpleStringProperty countryName = new SimpleStringProperty("");
  private SimpleStringProperty publishdate = new SimpleStringProperty("");
  private SimpleStringProperty scheduletype = new SimpleStringProperty("");
  private SimpleIntegerProperty experience = new SimpleIntegerProperty(0);
  private SimpleIntegerProperty salary = new SimpleIntegerProperty(0);
  private SimpleStringProperty skills = new SimpleStringProperty("");
  private SimpleStringProperty additionalskills = new SimpleStringProperty("");
  private SimpleStringProperty busytype = new SimpleStringProperty("");
  private SimpleStringProperty relocation = new SimpleStringProperty("");
  private SimpleStringProperty businesstrips = new SimpleStringProperty("");
  private SimpleStringProperty addcertificates = new SimpleStringProperty("");
  private SimpleStringProperty retrainingcapability = new SimpleStringProperty("");
  private SimpleStringProperty iduser = new SimpleStringProperty("");
  private SimpleStringProperty status = new SimpleStringProperty("");
  private SimpleStringProperty visibility = new SimpleStringProperty("");
  private SimpleStringProperty datemodify = new SimpleStringProperty("");
  private SimpleStringProperty deleted = new SimpleStringProperty("");
  private SimpleStringProperty fullnessrate = new SimpleStringProperty("");

  public CV() {
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

  public String getCreationdate() {
    return creationdate.get();
  }

  public SimpleStringProperty creationdateProperty() {
    return creationdate;
  }

  public void setCreationdate(String creationdate) {
    this.creationdate.set(creationdate);
  }

  public String getCandidateid() {
    return candidateid.get();
  }

  public SimpleStringProperty candidateidProperty() {
    return candidateid;
  }

  public void setCandidateid(String candidateid) {
    this.candidateid.set(candidateid);
  }

  public String getLocality() {
    return locality.get();
  }

  public SimpleStringProperty localityProperty() {
    return locality;
  }

  public void setLocality(String locality) {
    this.locality.set(locality);
  }

  public String getPositionname() {
    return positionname.get();
  }

  public SimpleStringProperty positionnameProperty() {
    return positionname;
  }

  public void setPositionname(String positionname) {
    this.positionname.set(positionname);
  }

  public String getIdowner() {
    return idowner.get();
  }

  public SimpleStringProperty idownerProperty() {
    return idowner;
  }

  public void setIdowner(String idowner) {
    this.idowner.set(idowner);
  }

  public String getDrivelicences() {
    return drivelicences.get();
  }

  public SimpleStringProperty drivelicencesProperty() {
    return drivelicences;
  }

  public void setDrivelicences(String drivelicences) {
    this.drivelicences.set(drivelicences);
  }

  public String getCountryName() {
    return countryName.get();
  }

  public SimpleStringProperty countryNameProperty() {
    return countryName;
  }

  public void setCountryName(String countryName) {
    this.countryName.set(countryName);
  }

  public String getPublishdate() {
    return publishdate.get();
  }

  public SimpleStringProperty publishdateProperty() {
    return publishdate;
  }

  public void setPublishdate(String publishdate) {
    this.publishdate.set(publishdate);
  }

  public String getScheduletype() {
    return scheduletype.get();
  }

  public SimpleStringProperty scheduletypeProperty() {
    return scheduletype;
  }

  public void setScheduletype(String scheduletype) {
    this.scheduletype.set(scheduletype);
  }

  public int getExperience() {
    return experience.get();
  }

  public SimpleIntegerProperty experienceProperty() {
    return experience;
  }

  public void setExperience(int experience) {
    this.experience.set(experience);
  }

  public int getSalary() {
    return salary.get();
  }

  public SimpleIntegerProperty salaryProperty() {
    return salary;
  }

  public void setSalary(int salary) {
    this.salary.set(salary);
  }

  public String getSkills() {
    return skills.get();
  }

  public SimpleStringProperty skillsProperty() {
    return skills;
  }

  public void setSkills(String skills) {
    this.skills.set(skills);
  }

  public String getAdditionalskills() {
    return additionalskills.get();
  }

  public SimpleStringProperty additionalskillsProperty() {
    return additionalskills;
  }

  public void setAdditionalskills(String additionalskills) {
    this.additionalskills.set(additionalskills);
  }

  public String getBusytype() {
    return busytype.get();
  }

  public SimpleStringProperty busytypeProperty() {
    return busytype;
  }

  public void setBusytype(String busytype) {
    this.busytype.set(busytype);
  }

  public String getRelocation() {
    return relocation.get();
  }

  public SimpleStringProperty relocationProperty() {
    return relocation;
  }

  public void setRelocation(String relocation) {
    this.relocation.set(relocation);
  }

  public String getBusinesstrips() {
    return businesstrips.get();
  }

  public SimpleStringProperty businesstripsProperty() {
    return businesstrips;
  }

  public void setBusinesstrips(String businesstrips) {
    this.businesstrips.set(businesstrips);
  }

  public String getAddcertificates() {
    return addcertificates.get();
  }

  public SimpleStringProperty addcertificatesProperty() {
    return addcertificates;
  }

  public void setAddcertificates(String addcertificates) {
    this.addcertificates.set(addcertificates);
  }

  public String getRetrainingcapability() {
    return retrainingcapability.get();
  }

  public SimpleStringProperty retrainingcapabilityProperty() {
    return retrainingcapability;
  }

  public void setRetrainingcapability(String retrainingcapability) {
    this.retrainingcapability.set(retrainingcapability);
  }

  public String getIduser() {
    return iduser.get();
  }

  public SimpleStringProperty iduserProperty() {
    return iduser;
  }

  public void setIduser(String iduser) {
    this.iduser.set(iduser);
  }

  public String getStatus() {
    return status.get();
  }

  public SimpleStringProperty statusProperty() {
    return status;
  }

  public void setStatus(String status) {
    this.status.set(status);
  }

  public String getVisibility() {
    return visibility.get();
  }

  public SimpleStringProperty visibilityProperty() {
    return visibility;
  }

  public void setVisibility(String visibility) {
    this.visibility.set(visibility);
  }

  public String getDatemodify() {
    return datemodify.get();
  }

  public SimpleStringProperty datemodifyProperty() {
    return datemodify;
  }

  public void setDatemodify(String datemodify) {
    this.datemodify.set(datemodify);
  }

  public String getDeleted() {
    return deleted.get();
  }

  public SimpleStringProperty deletedProperty() {
    return deleted;
  }

  public void setDeleted(String deleted) {
    this.deleted.set(deleted);
  }

  public String getFullnessrate() {
    return fullnessrate.get();
  }

  public SimpleStringProperty fullnessrateProperty() {
    return fullnessrate;
  }

  public void setFullnessrate(String fullnessrate) {
    this.fullnessrate.set(fullnessrate);
  }
}
