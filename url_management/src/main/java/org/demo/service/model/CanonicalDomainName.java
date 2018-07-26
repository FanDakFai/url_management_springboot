package org.demo.service.model;


import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import java.util.Set;
import java.util.HashSet;


@Entity
@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
public class CanonicalDomainName extends DomainName {
  @OneToMany(mappedBy = "domainName", cascade = {CascadeType.ALL},
    orphanRemoval = true)
  private Set<Ipv4> ipv4s;

  public CanonicalDomainName() {
    this.ipv4s = new HashSet<Ipv4>();
  }

  public Set<Ipv4> getIpv4s() {
    return this.ipv4s;
  }

  public void setIpv4s(Set<Ipv4> ipv4s) {
    // clone this.ipv4s content with ipv4s content voiding remove then re-add
    // items that should be in the set
    this.getIpv4s().retainAll(ipv4s);
    this.getIpv4s().addAll(ipv4s);
    // targets ips should be updated so that 'this' is theirs domain name
    for (Ipv4 ipv4 : this.getIpv4s()) {
      if (ipv4.getDomainName() != this) {
        ipv4.setDomainName(this);
      }
    }
  }
}


