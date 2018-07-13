package org.demo.service.controller;


import java.util.List;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.demo.service.model.DomainName;


@RestController
@PreAuthorize("#oauth2.hasScope('read')")
public class DomainNameController {
  @RequestMapping(method = RequestMethod.GET, value = "/domainnames")
  @ResponseBody
  public List<DomainName> DomainNames() {
    List<DomainName> result = new ArrayList<DomainName>();
    result.add(new DomainName());
    result.add(new DomainName("mygateway", DomainName.computeIpv4Address(192, 168, 1, 1)));
    return result;
  }
}


