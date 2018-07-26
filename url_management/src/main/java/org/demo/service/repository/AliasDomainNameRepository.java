package org.demo.service.repository;


import org.demo.service.model.AliasDomainName;
import org.springframework.data.repository.CrudRepository;
import javax.transaction.Transactional;


/**
 * A DAO for the entity AliasDomainName is simply created by extending the CrudRepository
 * interface provided by Spring. The following methods are some of the ones
 * available from such interface: save, delete, deleteAll, findOne and findAll.
 * The magic is that such methods must not be implemented, and moreover it is
 * possible create new query methods working only by defining their signature!
 *
 */
@Transactional
public interface AliasDomainNameRepository extends CrudRepository<AliasDomainName, String> {
  public AliasDomainName findByName(String name);
}


