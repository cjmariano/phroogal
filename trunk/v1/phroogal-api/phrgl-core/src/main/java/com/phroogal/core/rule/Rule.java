package com.phroogal.core.rule;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.phroogal.core.utility.CollectionUtil;

/**
 * Base rules to be extended by specific rules to be fed to the rules engine
 * @author Christopher Mariano
 *
 */
@Component
public class Rule {
	
	@Autowired
	private RulesEngine rulesEngine;
	
	private List<? extends Fact> facts;
	
	/**
	 * Invoking this command jumpstarts the rule engine and execute the 
	 * facts against it.
	 * @return an instance of {@link RuleExecutionContext} that contains the execution results
	 */
	public <T> RuleExecutionContext<T> execute() {
		return rulesEngine.execute(this);
	}
	
	/**
	 * Hook for allowing child classes to implement pre process functions before
	 * executing the rule
	 * @param facts
	 */
	@SuppressWarnings("rawtypes")
	public void preProcess(List facts) {
		//To be overridden by child classes if needed
	}
	
	/**
	 * Hook for allowing child classes to implement post process functions before
	 * executing the rule
	 * @param facts
	 */
	@SuppressWarnings("rawtypes")
	public void postProcess(List facts, RuleExecutionContext<?> ruleExecutionContext) {
		//To be implemented by child classes if needed
	}
	
	public List<? extends Fact> getFacts() {
		if (facts == null) {
			facts = CollectionUtil.arrayList();
		}
		return facts;
	}

	public void setFacts(List<? extends Fact> facts) {
		this.facts = facts;
	}
}
