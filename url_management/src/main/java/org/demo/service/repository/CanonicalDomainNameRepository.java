package org.demo.service.repository;


import org.demo.service.model.CanonicalDomainName;
import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;


/**
 * A DAO for the entity CanonicalDomainName is simply created by extending the CrudRepository
 * interface provided by Spring. The following methods are some of the ones
 * available from such interface: save, delete, deleteAll, findOne and findAll.
 * The magic is that such methods must not be implemented, and moreover it is
 * possible create new query methods working only by defining their signature!
 *
 */
@Transactional
public interface CanonicalDomainNameRepository extends CrudRepository<CanonicalDomainName, String> {
  public CanonicalDomainName findByName(String name);
}


