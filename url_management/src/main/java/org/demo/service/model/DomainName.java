package org.demo.service.model;


import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class DomainName {
  public static final Integer LOCALHOST_IP = (127 << 24 | 1);
  public static final String LOCALHOST_DN = "localhost";

  @Id
  private final String domainName;
  private final Integer ipv4Address;

  public DomainName() {
    this(LOCALHOST_DN, LOCALHOST_IP);
  }

  public DomainName(String domainName, Integer ipv4Address) {
    this.domainName = domainName;
    this.ipv4Address = ipv4Address;
  }

  public String getDomainName() {
    return this.domainName;
  }

  public Integer getIpv4Address() {
    return this.ipv4Address;
  }

  public static Integer computeIpv4Address(int addr1, int addr2, int addr3, int addr4) {
    return (addr1 << 24) | (addr2 << 16) | (addr3 << 8) | (addr4);
  }
}


