/**
 * 
 */
package com.phroogal.core.rule.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.drools.event.rule.DebugAgendaEventListener;
import org.drools.event.rule.DebugWorkingMemoryEventListener;
import org.drools.runtime.StatelessKnowledgeSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.phroogal.core.rule.Fact;
import com.phroogal.core.rule.Rule;
import com.phroogal.core.rule.RuleExecutionContext;
import com.phroogal.core.rule.RulesContext;
import com.phroogal.core.rule.RulesEngine;
import com.phroogal.core.utility.CollectionUtil;

/**
 * Implementation for {@link RulesEngine} using JBoss Drools
 * @author Christopher Mariano
 *
 */
@Component
public class RulesEngineImpl implements RulesEngine {

	@Autowired
	@Qualifier(value="ksession")
	private StatelessKnowledgeSession droolsSession;
	
	@Autowired
	private RulesContext rulesContext;
	
	@Value(value="${drools.debug.on}")
	public boolean isDebugOn;
	
	@PostConstruct
	public void init() throws Exception {
		if (isDebugOn) {
			droolsSession.addEventListener(new DebugAgendaEventListener()); 
			droolsSession.addEventListener(new DebugWorkingMemoryEventListener());
		}
	}

	@Override
	public <T> RuleExecutionContext<T> execute(Rule rule) {
		RuleExecutionContext<T> ruleExecutionContext = new RuleExecutionContext<T>();
		List<Fact> facts = initializeFacts(rule, ruleExecutionContext);
		rule.preProcess(facts);
		droolsSession.execute(facts);
		rule.postProcess(facts, ruleExecutionContext);
		return ruleExecutionContext;
	}

	@SuppressWarnings({ "rawtypes" })
	private List<Fact> initializeFacts(Rule rule, RuleExecutionContext ruleExecutionContext) {
		List<Fact> facts = CollectionUtil.arrayList();
		facts.add(rulesContext);
		facts.add(ruleExecutionContext);
		facts.addAll(rule.getFacts());
		return facts;
	}
}
