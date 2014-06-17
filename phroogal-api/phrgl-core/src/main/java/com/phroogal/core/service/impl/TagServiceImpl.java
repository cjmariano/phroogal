package com.phroogal.core.service.impl;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.phroogal.core.domain.Tag;
import com.phroogal.core.repository.TagRepository;
import com.phroogal.core.repository.index.QuestionIndexRepository;
import com.phroogal.core.rule.Fact;
import com.phroogal.core.rule.Rule;
import com.phroogal.core.rule.RuleExecutionContext;
import com.phroogal.core.service.TagService;
import com.phroogal.core.utility.CollectionUtil;


/**
 * Default implementation of the {@link Tag} interface
 * @author Christopher Mariano
 *
 */
@Service
public class TagServiceImpl extends BaseService<Tag, ObjectId,TagRepository> implements TagService{

	@Autowired
	private TagRepository tagRepository;
	
	@Autowired
	private QuestionIndexRepository questionIndexRepository;
	
	@Autowired
	@Qualifier(value="tagTrendingRule")
	private Rule tagTrendingRule;
	
	@Override
	protected TagRepository getRepository() {
		return tagRepository;
	}
	

	@Override
	@CacheEvict(value="tagsCache", allEntries=true)
	public List<Tag> saveOrUpdate(List<Tag> tags) {
		return super.saveOrUpdate(tags);
	}
	
	@Override
	@CacheEvict(value="tagsCache", allEntries=true)
	public Tag saveOrUpdate(Tag tag) {
		return super.saveOrUpdate(tag);
	}
	
	@Override
	public List<Tag> findByNameStartingWith(String keyword) {
	    return tagRepository.findByNameStartingWith(keyword);
	}
	
	@Override
	public Page<Tag> getTrendingTags(int pageAt, int pageSize) {
		Sort sort = new Sort(Sort.Direction.DESC, "rank");
		return findAllTagsByPageRequestParameters(pageAt, pageSize, sort);
	}

	@Override
	public Page<Tag> getTagsSortByAscName(int pageAt, int pageSize) {
		Sort sort = new Sort(Sort.Direction.ASC, "name");
		return findAllTagsByPageRequestParameters(pageAt, pageSize, sort);
	}
	
	@Override
	public Page<Tag> getTagsSortByDescTotalNumQuestionsTagged(int pageAt, int pageSize) {
		Sort sort = new Sort(Sort.Direction.DESC, "totalNumQuestionsTagged");
		return findAllTagsByPageRequestParameters(pageAt, pageSize, sort);
	}

	@Async
	@Override
	public void refreshTagMetadata(List<String> tags) {
		tagTrendingRule.setFacts(generateFactsForTrendingTags(tags));
		RuleExecutionContext<Tag> context = tagTrendingRule.execute();
		saveOrUpdate(context.getResults());
	}

	private List<Fact> generateFactsForTrendingTags(List<String> tags) {
		List<Fact> facts = CollectionUtil.arrayList();
		facts.addAll(tagRepository.findByNames(tags));
		facts.addAll(questionIndexRepository.searchByTags(tags));
		return facts;
	}
	
	private Page<Tag> findAllTagsByPageRequestParameters(int pageAt, int pageSize, Sort sort) {
		Pageable pageRequest = generatePageRequest(pageAt, pageSize, sort);
		return tagRepository.findAll(pageRequest);
	}
}
