package org.demo.service.controller;


import java.util.Set;
import java.util.Iterator;
import javax.validation.Valid;
import java.io.IOException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.demo.service.repository.AliasDomainNameRepository;
import org.demo.service.repository.CanonicalDomainNameRepository;
import org.springframework.http.HttpStatus;
import org.springframework.dao.DataAccessException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.demo.service.model.DomainName;
import org.demo.service.model.CanonicalDomainName;
import org.demo.service.model.AliasDomainName;
import org.demo.service.model.Ipv4;


@RestController
@PreAuthorize("#oauth2.hasScope('read')")
@RequestMapping(value = "/")
public class DomainNameController {
  private static final ObjectMapper objectMapper;

  @Autowired
  private CanonicalDomainNameRepository canonicalDomainNameRepository;

  @Autowired
  private AliasDomainNameRepository aliasDomainNameRepository;


  @GetMapping("/domainnames")
  public ResponseEntity<Iterable<CanonicalDomainName>> listDomainNames() {
    if (this.getCanonicalDomainNameRepository().count() > 0) {
      Iterable<CanonicalDomainName> result
        = this.getCanonicalDomainNameRepository().findAll();
      return new ResponseEntity<Iterable<CanonicalDomainName>>(
        result, HttpStatus.OK);
    }
    return new ResponseEntity<Iterable<CanonicalDomainName>>(
      HttpStatus.NOT_FOUND);
  }

  @GetMapping("/domainnames/{id}")
  public ResponseEntity<CanonicalDomainName> getDomainName(
    @PathVariable(value = "id") String name) {
    CanonicalDomainName targetDomainName
      = this.getCanonicalDomainNameRepository().findByName(name);
    if (targetDomainName == null) {
      return new ResponseEntity<CanonicalDomainName>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<CanonicalDomainName>(
      targetDomainName, HttpStatus.OK);
  }

  @PostMapping("/domainnames")
  public ResponseEntity<String> createDomainName(
    @Valid @RequestBody String rawJsonStringData) {
    // @TODO: find a solution to describle the parsing or persisting errors
    //        because of this method's complexity nature
    DomainName result = parseDomainName(rawJsonStringData);
    if (result != null) {
      if (result instanceof CanonicalDomainName) {
        return createDomainName((CanonicalDomainName) result);
      }
      if (result instanceof AliasDomainName) {
        return createDomainName((AliasDomainName) result);
      }
      return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity(HttpStatus.BAD_REQUEST);
  }

  // TODO: now to update alias domain -> simple solution: delete and create a
  //       new one any way should check that client should not put an alias
  //       domain name content
  @PutMapping("/domainnames/{id}")
  public ResponseEntity<CanonicalDomainName> updateDomainName(
      @PathVariable(value = "id") String domainNameId,
      @Valid @RequestBody CanonicalDomainName domainNameDetail) {
    // Verify the put content if required information is presented
    if (domainNameDetail.getIpv4s() == null
      || domainNameDetail.getIpv4s().size() == 0) {
      return new ResponseEntity<CanonicalDomainName>(HttpStatus.NO_CONTENT);
    }
    if (domainNameDetail.getName() != null
      && !domainNameDetail.getName().equalsIgnoreCase(domainNameId)) {
      return new ResponseEntity<CanonicalDomainName>(HttpStatus.BAD_REQUEST);
    }
    CanonicalDomainName updatedDomainName
      = this.getCanonicalDomainNameRepository().findByName(domainNameId);
    if (updatedDomainName == null) {
      return new ResponseEntity<CanonicalDomainName>(HttpStatus.NOT_FOUND);
    }
    try {
      updatedDomainName.setIpv4s(domainNameDetail.getIpv4s());
      updatedDomainName
        = this.getCanonicalDomainNameRepository().save(updatedDomainName);
      return new ResponseEntity<CanonicalDomainName>(
        updatedDomainName, HttpStatus.OK);
    }
    catch(DataAccessException daoException) {
      //daoException.printStackTrace();
      return new ResponseEntity<CanonicalDomainName>(HttpStatus.NOT_FOUND);
    }
  }

  // Should support delete both alias and canonical domain name
  @DeleteMapping("/domainnames/{id}")
  public ResponseEntity deleteDomainName(
      @PathVariable(value = "id") String domainNameId) {
    CanonicalDomainName cDomainName
      = this.getCanonicalDomainNameRepository().findByName(domainNameId);
    if (cDomainName == null) {
      AliasDomainName aDomainName
        = this.getAliasDomainNameRepository().findByName(domainNameId);
      if (aDomainName == null) {
        return new ResponseEntity(HttpStatus.NOT_FOUND);
      }
      else {
        try {
          this.getAliasDomainNameRepository().delete(aDomainName);
          return new ResponseEntity(HttpStatus.OK);
        }
        catch (Exception e) {
        }
      }
    }
    else {
      try {
        this.getCanonicalDomainNameRepository().delete(cDomainName);
        return new ResponseEntity(HttpStatus.OK);
      }
      catch (Exception e) {
      }
    }
    return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
  }


  private CanonicalDomainNameRepository getCanonicalDomainNameRepository() {
    return this.canonicalDomainNameRepository;
  }

  private AliasDomainNameRepository getAliasDomainNameRepository() {
    return this.aliasDomainNameRepository;
  }

  private ResponseEntity<String> createDomainName(
    CanonicalDomainName domainName) {
    String name = domainName.getName();
    Set<Ipv4> ipv4s = domainName.getIpv4s();
    // Make sure that the post body content is not null/empty
    if (ipv4s == null || name == null
      || ipv4s.size() == 0 || name.length() == 0) {
      return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
    }
    // Test if this domain name is exist
    CanonicalDomainName foundDomainName
      = this.getCanonicalDomainNameRepository().findByName(name);
    if (foundDomainName != null) {
      return new ResponseEntity<String>(HttpStatus.CONFLICT);
    }
    try {
      CanonicalDomainName result
        = this.getCanonicalDomainNameRepository().save(domainName);
      return new ResponseEntity<String>(
        DomainNameController.convertToJsonString(result), HttpStatus.CREATED);
    }
    catch(DataAccessException daoException) {
      return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
    }
  }

  private ResponseEntity<String> createDomainName(
    AliasDomainName domainName) {
    String name = domainName.getName();
    CanonicalDomainName cDomainName = domainName.getReferencedDomainName();

    // Make sure that posted body content is not null/empty
    if (name == null || name.length() == 0 || cDomainName == null) {
      return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
    }
    String cname = cDomainName.getName();
    if (cname == null || cname.length() == 0) {
      return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
    }

    // Test if this domain name is exist
    AliasDomainName foundDomainName
      = this.getAliasDomainNameRepository().findByName(name);
    if (foundDomainName != null) {
      return new ResponseEntity<String>(HttpStatus.CONFLICT);
    }
    try {
      AliasDomainName result = null;
      // Retrieve referenced domain name
      CanonicalDomainName referencedDomainName
        = this.getCanonicalDomainNameRepository().findByName(cname);
      // If referenced domain name exists -> point to that exists one
      if (referencedDomainName != null) {
        // Posted ipv4s should be null or it must not be conflicted with exists
        // domain name ipv4s which retrieved from database
        Set<Ipv4> ipv4s = domainName.getReferencedDomainName().getIpv4s();
        if (ipv4s == null || ipv4s.size() == 0 
          || (ipv4s.containsAll(referencedDomainName.getIpv4s()) && 
          referencedDomainName.getIpv4s().containsAll(ipv4s))) {
          domainName.setReferencedDomainName(referencedDomainName);
          result = this.getAliasDomainNameRepository().save(domainName);
          return new ResponseEntity<String>(
            DomainNameController.convertToJsonString(result), HttpStatus.CREATED);
        }
        else {
          new ResponseEntity<String>(HttpStatus.CONFLICT); 
        }
      }
      // In the case such one does not exist, create it before point to
      else {
        Set<Ipv4> ipv4s = domainName.getReferencedDomainName().getIpv4s();
        if (ipv4s == null || ipv4s.size() == 0) {
          return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
        }
        this.getCanonicalDomainNameRepository().save(
          domainName.getReferencedDomainName());
        result = this.getAliasDomainNameRepository().save(domainName);
      }
      return new ResponseEntity<String>(
        DomainNameController.convertToJsonString(result), HttpStatus.CREATED);
    }
    catch(DataAccessException daoException) {
    }
    return new ResponseEntity<String>(HttpStatus.NOT_FOUND); 
  }

  private static DomainName parseDomainName(String rawJsonStringData) {
    DomainName result = null;
    // Try to parse as CanonicalDomainName json object
    try {
      result = DomainNameController.objectMapper.readValue(
        rawJsonStringData, CanonicalDomainName.class);
      // DEBUG-TRACE: System.out.println("CanonicalDomainName parsed");
    }
    catch (IOException e) {
      // DEBUG-TRACE: e.printStackTrace();
      if (e instanceof UnrecognizedPropertyException) {
        String propName = ((UnrecognizedPropertyException) e).getPropertyName();
        // In the case parse exception state that property
        // "referencedDomainName" does not exist, it mean that client post
        // an AliasDomainName json object
        if (propName.compareTo(
          AliasDomainName.REFERENCED_DOMAINNAME_PROPERTY_NAME) == 0) {
          // Try to parse as AliasDomainName json object
          try {
            result = DomainNameController.objectMapper.readValue(
                rawJsonStringData, AliasDomainName.class);
            // DEBUG-TRACE: System.out.println("AliasDomainName parsed");
          }
          catch (IOException ee) {
          }
        }
      }
    }
    // DEBUG-TRACE: System.out.println("Failure all tries parsed");
    return result; 
  }

  private static String convertToJsonString(DomainName domainName) {
    String result = "";
    try {
      result = DomainNameController.objectMapper.writeValueAsString(domainName);
    }
    catch(IOException e) {
    }
    return result;
  }


  static {
    objectMapper = new ObjectMapper();
  }
}


