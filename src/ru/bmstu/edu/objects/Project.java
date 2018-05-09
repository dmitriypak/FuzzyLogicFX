package ru.bmstu.edu.objects;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by HP on 22.05.2017.
 */
public class Project {
    private SimpleIntegerProperty id = new SimpleIntegerProperty(0);
    private SimpleStringProperty name = new SimpleStringProperty("");
    private SimpleStringProperty descr = new SimpleStringProperty("");

    public int getId() {
        return id.get();
    }
    public void setId(int id) {
        this.id.set(id);
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }
    public SimpleStringProperty nameProperty() {
        return name;
    }
    public SimpleStringProperty descrProperty() {
        return descr;
    }


    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getDescr() {
        return descr.get();
    }

    public void setDescr(String descr) {
        this.descr.set(descr);
    }

    public Project(){

    }
    public Project(int id, String name) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
    }

    public Project(int id, String name, String descr) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.descr = new SimpleStringProperty(descr);
    }
}
