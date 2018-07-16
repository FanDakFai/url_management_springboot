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
public class ResolvedIpController {
  @Autowired
  private DomainNameController domainNameController;

  @GetMapping("/resolvedips/{id}")
  public ResponseEntity<Integer> getResolvedIp(@PathVariable(value = "id") String domainNameId) {
    ResponseEntity<DomainName> result = this.getDomainNameController().listDomainNames(domainNameId);
    if (result.getStatusCode() == HttpStatus.OK) {
      Integer resolvedIp = result.getBody().getIpv4Address();
      return new ResponseEntity<Integer>(resolvedIp, HttpStatus.OK);
    }
    return new ResponseEntity<Integer>(result.getStatusCode());
  }

  private DomainNameController getDomainNameController() {
    return this.domainNameController;
  }
}


