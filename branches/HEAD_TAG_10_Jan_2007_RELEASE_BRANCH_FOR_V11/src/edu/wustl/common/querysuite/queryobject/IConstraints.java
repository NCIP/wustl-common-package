
package edu.wustl.common.querysuite.queryobject;

import java.util.Enumeration;
import java.util.Set;

import edu.wustl.common.querysuite.exceptions.MultipleRootsException;

/**
 * Contains information about the constraints of a query. It contains a list of
 * {@link edu.wustl.common.querysuite.queryobject.IExpression}s that is indexed
 * by {@link edu.wustl.common.querysuite.queryobject.IExpressionId}. This is
 * global storage location for all expressions in a query; an
 * {@link edu.wustl.common.querysuite.queryobject.IExpression} can be created
 * only by calling the addExpression method here.<br>
 * It also contains a join graph for specifying how the expressions are linked
 * together.
 * @author srinath_k
 * @see edu.wustl.common.querysuite.queryobject.IExpression
 * @see edu.wustl.common.querysuite.queryobject.IJoinGraph
 */
public interface IConstraints extends IBaseQueryObject
{

	/**
	 * To get the Expression corresponding to the Expression id.
	 * @param id
	 *            the id (usually obtained from getExpressionIds)
	 * @return the reference to the IExpression associatied with the given IExpressionId. 
	 */
	IExpression getExpression(IExpressionId id);

	/**
	 * @param id the id of the expression to be removed.
	 * @return the removed expression
	 */
	IExpression removeExpressionWithId(IExpressionId id);

	/**
	 * @return an enumeration of the expressions' ids.
	 * @see Enumeration
	 */
	Enumeration<IExpressionId> getExpressionIds();

	/**
	 * Creates a new {@link IExpression}, adds it to the list, and returns it.
	 * The expressionId is autoGenerated in this method. This is the only way to
	 * create an expression.
	 * @param constraintEntity the constraint Entity for which the new expr is created.
	 * @return the newly created expression.
	 */
	IExpression addExpression(IQueryEntity constraintEntity);

	/**
	 * @return the reference to joingraph.
	 */
	IJoinGraph getJoinGraph();

	/**
	 * @return the root expression of the join graph.
	 * @throws MultipleRootsException When there exists multiple roots in joingraph.
	 */
	IExpressionId getRootExpressionId() throws MultipleRootsException;
	
	/**
	 * TO get the Set of all ConstraintEntites present in the Constraints object.
	 * @return Set of all Constraint Entities.
	 */
	Set<IQueryEntity> getQueryEntities();
}
