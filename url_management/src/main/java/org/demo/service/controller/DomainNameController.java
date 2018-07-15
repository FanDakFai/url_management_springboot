package org.demo.service.controller;


import java.util.List;
import java.util.ArrayList;
import javax.validation.Valid;
import java.util.Iterator;
import org.demo.service.model.DomainName;
import org.demo.service.repository.DomainNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.dao.DataAccessException;


@RestController
@PreAuthorize("#oauth2.hasScope('read')")
@RequestMapping(value = "/")
public class DomainNameController {
  @Autowired
  private DomainNameRepository domainNameRepository;

  @GetMapping("/domainnames")
  public ResponseEntity<Iterable<DomainName>> listDomainNames() {
    if (this.getDomainNameRepository().count() > 0) {
      Iterable<DomainName> result = this.getDomainNameRepository().findAll();
      return new ResponseEntity<Iterable<DomainName>>(result, HttpStatus.OK);
    }
    return new ResponseEntity<Iterable<DomainName>>(HttpStatus.NOT_FOUND);
  }

  @PostMapping("/domainnames")
  public ResponseEntity<DomainName> createOrUpdateDomainName(@Valid @RequestBody DomainName domainName) {
    String domainNameId = domainName.getDomainName();
    if (domainNameId == null || domainNameId.length() == 0) {
      return new ResponseEntity<DomainName>(HttpStatus.NOT_FOUND);
    }
    DomainName foundDomainName = this.getDomainNameRepository().findById(domainNameId).orElse(null);
    if (foundDomainName != null) {
      return new ResponseEntity<DomainName>(HttpStatus.CONFLICT);
    }
    try {
      DomainName result = this.getDomainNameRepository().save(domainName);
      return new ResponseEntity<DomainName>(result, HttpStatus.CREATED);
    }
    catch(DataAccessException daoException) {
      return new ResponseEntity<DomainName>(HttpStatus.NOT_FOUND);
    }
  }

  @PutMapping("/domainnames/{id}")
  public ResponseEntity<DomainName> updateDomainName(@PathVariable(value = "id") String domainNameId,
      @Valid @RequestBody DomainName domainNameDetail) {
    if (domainNameDetail.getIpv4Address() == null) {
      return new ResponseEntity<DomainName>(HttpStatus.NO_CONTENT);
    }
    if (domainNameDetail.getDomainName() != null
      && !domainNameDetail.getDomainName().equalsIgnoreCase(domainNameId)) {
      return new ResponseEntity<DomainName>(HttpStatus.BAD_REQUEST);
    }
    DomainName updatedDomainName = this.getDomainNameRepository().findById(domainNameId).orElse(null);
    if (updatedDomainName == null) {
      return new ResponseEntity<DomainName>(HttpStatus.NOT_FOUND);
    }
    try {
      updatedDomainName.setIpv4Address(domainNameDetail.getIpv4Address());
      updatedDomainName = this.getDomainNameRepository().save(updatedDomainName);
      return new ResponseEntity<DomainName>(updatedDomainName, HttpStatus.OK);
    }
    catch(DataAccessException daoException) {
      return new ResponseEntity<DomainName>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/domainnames/{id}")
  public ResponseEntity deleteDomainName(@PathVariable(value = "id") String domainNameId) {
    DomainName targetDomainName = this.getDomainNameRepository().findById(domainNameId).orElse(null);
    if (targetDomainName == null) {
      return new ResponseEntity<DomainName>(HttpStatus.NOT_FOUND);
    }
    this.getDomainNameRepository().delete(targetDomainName);
    return new ResponseEntity(targetDomainName, HttpStatus.OK);
  }

  private DomainNameRepository getDomainNameRepository() {
    return domainNameRepository;
  }
}


