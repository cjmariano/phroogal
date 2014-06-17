package com.phroogal.core.repository.index;

import org.springframework.data.solr.repository.SolrCrudRepository;

public interface BaseSolrIndexRepository<T> extends SolrCrudRepository<T, String> {

}
