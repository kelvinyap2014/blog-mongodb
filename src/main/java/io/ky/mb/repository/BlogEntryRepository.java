package io.ky.mb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import io.ky.mb.domain.BlogEntry;

/**
 * Spring Data MongoDB repository for the BlogEntry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BlogEntryRepository extends MongoRepository<BlogEntry, String> {

	List<BlogEntry> findByUserOrderByDateDesc(Optional<String> currentUserLogin);

}
