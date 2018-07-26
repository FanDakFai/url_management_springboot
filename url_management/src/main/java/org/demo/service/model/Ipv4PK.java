package org.demo.service.model;


import java.io.Serializable;


public class Ipv4PK implements Serializable {
  protected Integer value;
  protected DomainName domainName;

  public Ipv4PK() {
  }

  public Ipv4PK(Integer value, DomainName domainName) {
    this.value = value;
    this.domainName = domainName;
  }
}

