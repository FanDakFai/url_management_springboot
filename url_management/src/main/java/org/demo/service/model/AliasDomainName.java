package org.demo.service.model;


import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;


@Entity
@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
public class AliasDomainName extends DomainName {
  public static final String REFERENCED_DOMAINNAME_PROPERTY_NAME
    = "referencedDomainName";

  @OneToOne
  @JoinColumn(name="ref_id")
  private CanonicalDomainName referencedDomainName;

  public AliasDomainName() {
    this.referencedDomainName = null;
  }

  public CanonicalDomainName getReferencedDomainName() {
    return this.referencedDomainName;
  }

  public void setReferencedDomainName(
    CanonicalDomainName referencedDomainName) {
    this.referencedDomainName = referencedDomainName;
  }
}


