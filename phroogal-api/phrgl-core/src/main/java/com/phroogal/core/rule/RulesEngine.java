/**
 * 
 */
package com.phroogal.core.rule;



/**
 * Implements rules based requirements across the application 
 * @author Christopher Mariano
 *
 */
public interface RulesEngine {
	
	/**
	 * Executes the rule
	 * @param rule to provides facts for rule execution
	 * @return an instance of {@link RuleExecutionContext} that contains the execution results
	 */
	public <T> RuleExecutionContext<T> execute(Rule rule);
}
