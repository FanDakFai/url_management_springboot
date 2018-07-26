package org.demo.service.model;


import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@IdClass(Ipv4PK.class)
public class Ipv4 {
  @Id
  private Integer value;

  @Id
  @ManyToOne
  @JoinColumn(name = "ref_id", nullable = false)
  private DomainName domainName;

  public Ipv4(Integer value, DomainName domainName) {
    this.value = value;
    this.domainName = domainName;
  }

  public Ipv4() {
    this(null, null);
  }

  public Integer getValue() {
    return this.value;
  }

  @JsonIgnore
  public DomainName getDomainName() {
    return this.domainName;
  }

  public void setValue(Integer value) {
    this.value = value;
  }

  public void setDomainName(DomainName domainName) {
    this.domainName = domainName;
  }

  public boolean equals(Object other) {
    // Ipv4 is same if it value is same (domainName prop could be ignored)
    if (other != null && other instanceof Ipv4) {
      Ipv4 o = (Ipv4) other;
      if (this.getValue() != null && o.getValue() != null) {
        return this.getValue().equals(o.getValue());
      }
      return this.getValue() == null && o.getValue() == null;
    }
    return false;
  }
}


