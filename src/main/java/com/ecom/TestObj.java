package com.ecom;

import jakarta.persistence.*;

@Entity
@Table(name = "demo")
public class TestObj {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String sname;

    @Override
    public String toString() {
        return "TestObj{" +
                "id=" + id +
                ", name='" + sname + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public String getSname() {
        return sname;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }
}
