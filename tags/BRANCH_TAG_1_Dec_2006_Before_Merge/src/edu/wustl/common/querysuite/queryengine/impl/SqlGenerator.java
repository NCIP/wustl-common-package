
package edu.wustl.common.querysuite.queryengine.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import edu.common.dynamicextensions.domain.Attribute;
import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.common.querysuite.exceptions.MultipleRootsException;
import edu.wustl.common.querysuite.queryengine.ISqlGenerator;
import edu.wustl.common.querysuite.queryobject.DataType;
import edu.wustl.common.querysuite.queryobject.IAssociation;
import edu.wustl.common.querysuite.queryobject.IAttribute;
import edu.wustl.common.querysuite.queryobject.IClass;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionId;
import edu.wustl.common.querysuite.queryobject.IExpressionOperand;
import edu.wustl.common.querysuite.queryobject.IIntraModelAssociation;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.queryobject.LogicalOperator;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import edu.wustl.common.querysuite.queryobject.impl.Expression;
import edu.wustl.common.querysuite.queryobject.impl.IntraModelAssociation;
import edu.wustl.common.querysuite.queryobject.impl.JoinGraph;
import edu.wustl.common.querysuite.queryobject.impl.LogicalConnector;

/**
 * To generate SQL from the given Query Object.
 * @author prafull_kadam
 *
 */
public class SqlGenerator implements ISqlGenerator
{

	private EntityManager entityManager;

	private Map<String, Entity> entityMap = new HashMap<String, Entity>(); // to cache the Dynamic Extension Entity object to avoid multiple call to APIs.

	Map<IExpression, List<String>> sqlMap = new LinkedHashMap<IExpression, List<String>>(); // Stores SQL Queries for each Expression. values in this map will be inserted while compiling Query.

	Map<IIntraModelAssociation, AssociationInterface> associationMap = new LinkedHashMap<IIntraModelAssociation, AssociationInterface>();

	Map<IExpressionId, Integer> aliasMap = new HashMap<IExpressionId, Integer>();
	Map<List<IAssociation>, IExpressionId> pathMap = new HashMap<List<IAssociation>, IExpressionId>();

	JoinGraph joinGraph;
	IConstraints constraints;

	SqlGenerator(EntityManager entityManager)
	{
		this.entityManager = entityManager;
	}

	/**
	 * @see edu.wustl.common.querysuite.queryengine.ISqlGenerator#generateSQL(edu.wustl.common.querysuite.queryobject.IQuery)
	 */
	public String generateSQL(IQuery query) throws MultipleRootsException,
			DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		//		// TODO Auto-generated method stub
		//		IExpression rootExpression = buildQuery(query);
		//		String wherePart = getWherePartSQL();
		//		String fromPart = getFromPartSQL(rootExpression,null, new HashSet<Integer>());
		//		String selectPart = getSelectPart(rootExpression);
		//		String SQL = selectPart + " " + fromPart + " " + wherePart;
		return buildQuery(query);
	}

	/**
	 * To initialize map the variables.  
	 * @param query the IQuery reference.
	 * @return The Root Expetssion of the IQuery. 
	 * @throws MultipleRootsException
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	String buildQuery(IQuery query) throws MultipleRootsException,
			DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		constraints = query.getConstraints();
		this.joinGraph = (JoinGraph) constraints.getJoinGraph();
		IExpression rootExpression = constraints.getExpression(constraints.getRootExpressionId());
		entityMap = new HashMap<String, Entity>();
		sqlMap = new LinkedHashMap<IExpression, List<String>>();
		associationMap = new LinkedHashMap<IIntraModelAssociation, AssociationInterface>();

		aliasMap = new HashMap<IExpressionId, Integer>();
		pathMap = new HashMap<List<IAssociation>, IExpressionId>();
		createAliasAppenderMap(rootExpression, 1, new Integer(1));

		String wherePart = getSQL(rootExpression, null, false);
		String fromPart = getFromPartSQL(rootExpression, null, new HashSet<Integer>());
		String selectPart = getSelectPart(rootExpression);
		String SQL = selectPart + " " + fromPart + " " + wherePart;
		return SQL;
	}

	/**
	 * To get the Select clause of the Query.
	 * @param expression The Expression which will appear in the Select part.
	 * @return the Select clause of the Query.
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	String getSelectPart(IExpression expression) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		StringBuffer buffer = new StringBuffer("Select ");
		Entity entity = getEntity((IClass) expression.getFunctionalClass());
		String aliasName = getAliasName(entity, expression);
		Iterator attributeCollectionItr = entity.getAbstractAttributeCollection().iterator();
		while (attributeCollectionItr.hasNext())
		{
			Attribute attribute = (Attribute) attributeCollectionItr.next();
			buffer.append(aliasName + "." + attribute.getColumnProperties().getName());
			if (attributeCollectionItr.hasNext())
				buffer.append(", ");
		}
		return buffer.toString();
	}

	/**
	 *  To get the From clause of the Query.
	 * @param expression The Root Expression.
	 * @param processedAlias The set of aliases processed.
	 * @return the From clause of the SQL.
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	String getFromPartSQL(IExpression expression, String leftAlias, Set<Integer> processedAlias)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		StringBuffer buffer = new StringBuffer("");
		IExpressionId parentExpressionId = expression.getExpressionId();

		if (processedAlias.isEmpty()) // this will be true only for root node.
		{
			Entity leftEntity = getEntity((IClass) expression.getFunctionalClass());
			leftAlias = getAliasName(leftEntity, expression);
			buffer.append("From " + leftEntity.getTableProperties().getName() + " " + leftAlias);
		}

		processedAlias.add(aliasMap.get(parentExpressionId));

		List<IExpressionId> children = joinGraph.getDirectSuccessorOf(parentExpressionId);
		if (!children.isEmpty())
		{
			for (int index = 0; index < children.size(); index++)
			{
				IExpressionId childExpressionId = children.get(index);
				IExpression childExpression = constraints.getExpression(childExpressionId);
				if (!processedAlias.contains(aliasMap.get(childExpressionId)))
				{
					IAssociation association = joinGraph.getAssociation(parentExpressionId,
							childExpressionId);
					IClass rightClass = association.getTargetClass();
					Entity rightEntity = getEntity(rightClass);
					String rightAlias = getAliasName(rightEntity, childExpression);

					buffer.append(" left join " + rightEntity.getTableProperties().getName() + " "
							+ rightAlias + " on ");

					AssociationInterface eavAssociation = getAssoication((IntraModelAssociation) association);

					String leftAttribute = leftAlias + "."
							+ eavAssociation.getConstraintProperties().getSourceEntityKey();
					String rightAttribute = rightAlias + "."
							+ eavAssociation.getConstraintProperties().getTargetEntityKey();

					buffer.append("(" + leftAttribute + "=" + rightAttribute + ")");

					buffer.append(getFromPartSQL(childExpression, rightAlias, processedAlias));
				}
			}
		}
		return buffer.toString();
	}

	String getLeftJoinSQL() throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		StringBuffer buffer = new StringBuffer("");

		return buffer.toString();
	}

	//  Not using this method, since Query Building Logic is changed.
	//	/**
	//	 * To get the Where Part of the SQL Query. 
	//	 * @return The String representing Where part og the Query.
	//	 * @throws DynamicExtensionsApplicationException 
	//	 * @throws DynamicExtensionsSystemException 
	//	 */
	//	String getWherePartSQL() throws DynamicExtensionsSystemException,
	//			DynamicExtensionsApplicationException
	//	{
	//		StringBuffer buffer = new StringBuffer("Where ");
	//		Iterator<IExpression> keys = sqlMap.keySet().iterator();
	//		while (keys.hasNext())
	//		{
	//			// Forming Restriction Criteria for Each Expressions.
	//			IExpression expression = keys.next();
	//			List<String> sqlList = sqlMap.get(expression);
	//			IClass iClass = (IClass) expression.getFunctionalClass();
	//			String selectAttribute = getPrimaryKeyAttrinbute(iClass, expression);
	//			if (sqlList.size() == 1)
	//			{
	//				buffer.append(selectAttribute + " = ANY(" + sqlList.get(0) + ") ");
	//			}
	//			else
	//			// if there are multiple sql for Expression, then they will be connected by OR condition
	//			{
	//				buffer.append("(");
	//				for (int i = 0; i < sqlList.size(); i++)
	//				{
	//					buffer.append(selectAttribute + " = ANY(" + sqlList.get(i) + ") ");
	//					if (i != sqlList.size() - 1)
	//						buffer.append(LogicalOperator.Or + " ");
	//				}
	//				buffer.append(") ");
	//			}
	//			if (keys.hasNext())
	//				buffer.append(LogicalOperator.And + " "); // Anding all Restriction criterias.
	//		}
	//		return buffer.toString();
	//	}

	/**
	 * To compile the SQL & get the SQL representation of the Expression.
	 * @param expression the Expression whose SQL to be generated.
	 * @param parentExpression The Parent Expression.
	 * @return The SQL representation of the Expression.
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 * @throws NoSuchElementException when The Class in an expression does not have Primary Key attribute.
	 */
	String getSQL(IExpression expression, IExpression parentExpression, boolean isPAND)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		StringBuffer buffer = new StringBuffer("");
		if (parentExpression == null)
			buffer.append("Where ");
		int noOfRules = expression.numberOfOperands();
		int prevNesting = 0;
		int openingBraces = 0; // holds number of opening Braces added to SQL.

		// Put the Expression in the sqlMap.
		List<String> sqlList = sqlMap.get(expression);
		if (sqlList == null)
		{
			sqlList = new ArrayList<String>();
			sqlMap.put(expression, sqlList);
		}

		IClass iClass = (IClass) expression.getFunctionalClass();
		Entity entity = getEntity(iClass);

		if (parentExpression != null)
		{
			IAssociation association = joinGraph.getAssociation(parentExpression.getExpressionId(),
					expression.getExpressionId());
			AssociationInterface eavAssociation = getAssoication((IIntraModelAssociation) association);
			if (isPAND)
			{
				String tableName = entity.getTableProperties().getName() + " ";
				String leftAlias = getAliasName(entity, expression);
				String selectAttribute = leftAlias + "."
						+ eavAssociation.getConstraintProperties().getTargetEntityKey();

				buffer.append("Select " + selectAttribute);
				// TODO from part.
				Set<Integer> processedAlias = new HashSet<Integer>();
				processedAlias.add(aliasMap.get(expression.getExpressionId()));
				String fromPart = getFromPartSQL(expression, leftAlias, processedAlias);
				buffer.append(" From " + tableName + " " + leftAlias + fromPart + " where ");
			}

		}

		for (int i = 0; i < noOfRules; i++)
		{
			IExpressionOperand operand = expression.getOperand(i);
			String ruleSQL = "";
			if (!operand.isSubExpressionOperand())
			{
				ruleSQL = getSQL((IRule) operand); // Processing Rule.
			}
			else
			//Processing sub Expression.
			{
				IExpression childExpression = constraints.getExpression((IExpressionId) operand);

				isPAND = ((Expression) expression).isPseudoAnded(childExpression.getExpressionId());
				ruleSQL = getSQL(childExpression, expression, isPAND);
				if (isPAND)
				{
					IAssociation association = joinGraph.getAssociation(expression
							.getExpressionId(), childExpression.getExpressionId());
					AssociationInterface eavAssociation = getAssoication((IIntraModelAssociation) association);
					String joinAttribute = getAliasName(entity, expression) + "."
							+ eavAssociation.getConstraintProperties().getSourceEntityKey();
					ruleSQL = joinAttribute + " = ANY(" + ruleSQL + ")";
				}

			}
			if (!ruleSQL.equals("") && noOfRules != 1)
			{
				ruleSQL = "(" + ruleSQL + ")"; // putting RuleSQL  in Braces so that it will not get mixed with other Rules.
			}
			if (!ruleSQL.trim().equals("") && i != noOfRules - 1) // puts opening or closing brace in SQL depending upon nesting number along with LogicalOperator.
			{
				LogicalConnector connector = (LogicalConnector) expression.getLogicalConnector(i,
						i + 1);
				int nestingNumber = connector.getNestingNumber();

				if (prevNesting < nestingNumber)
				{
					buffer.append("(" + ruleSQL + " " + connector.getLogicalOperator());
					openingBraces++;
				}
				else if (prevNesting > nestingNumber)
				{
					buffer.append(ruleSQL + ") " + connector.getLogicalOperator());
					openingBraces--;
				}
				else
					buffer.append(ruleSQL + " " + connector.getLogicalOperator());
				prevNesting = nestingNumber;
			}
			else
			{ // finishing SQL.
				buffer.append(ruleSQL);
				if (openingBraces != 0)
				{
					buffer.append(")");
				}
			}
		}
		String sql = buffer.toString();

		sqlList.add(sql); //Add the SQL in the SQL map

		return sql;
	}

	/**
	 * To get the Association from Dynamic Extentsion for the given Assoctaion.
	 * @param association The reference to association
	 * @return the reference of Association from Dynamic Extentsion 
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	private AssociationInterface getAssoication(IIntraModelAssociation association)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		if (associationMap.containsKey(association))
			return associationMap.get(association);

		//		Collection associations = entityManager.getAssociation(association.getSourceClass()
		//				.getFullyQualifiedName(), association.getSourceRoleName());
		//		AssociationInterface theAssociation = null;
		//		Iterator itr = associations.iterator();
		//		while (itr.hasNext())
		//		{
		//			theAssociation = (AssociationInterface) itr.next();
		//
		//			if (association.getSourceRoleName().equals(theAssociation.getSourceRole().getName())
		//					&& (association.getTargetRoleName() == null || association.getTargetRoleName()
		//							.equals(theAssociation.getTargetRole().getName())))
		//			{
		//				associationMap.put(association, theAssociation);
		//				return theAssociation;
		//			}
		//		}
		//		return null;
		AssociationInterface theAssociation = entityManager.getAssociation(association
				.getSourceClass().getFullyQualifiedName(), association.getSourceRoleName());
		associationMap.put(association, theAssociation);
		return theAssociation;
	}

	/**
	 * To get the SQL representation of the Rule.
	 * @param rule
	 * @return The SQL representation of the Rule.
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	String getSQL(IRule rule) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		StringBuffer buffer = new StringBuffer("");
		int noOfConditions = rule.size();

		for (int i = 0; i < noOfConditions; i++) // Processing all conditions in Rule combining them with AND operator.
		{
			String condition = getSQL(rule.getCondition(i), rule.getContainingExpression());

			if (i != noOfConditions - 1) // Intermediate Condition.
				buffer.append(condition + " " + LogicalOperator.And + " ");
			else
				// Last Condition
				buffer.append(condition);
		}
		return buffer.toString();
	}

	/**
	 * To get the SQL Representation of the Condition.
	 * @param condition
	 * @return The SQL Representation of the Condition.
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	String getSQL(ICondition condition, IExpression expression)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		StringBuffer buffer = new StringBuffer("");
		IAttribute attribute = condition.getAttribute();
		DataType dataType = attribute.getDataType();
		String attributeName = getSQL(attribute, expression);

		RelationalOperator operator = condition.getRelationalOperator();
		String strOperator = RelationalOperator.getSQL(operator);

		if (operator.equals(RelationalOperator.Between))//Processing Between Operator, it will be treated as (op>=val1 and op<=;val2)
		{
			List<String> values = condition.getValues();
			String firstValue = modifyValueforDataType(values.get(0), dataType);
			String secondValue = modifyValueforDataType(values.get(1), dataType);

			buffer.append("(" + attributeName);
			buffer.append(RelationalOperator.getSQL(RelationalOperator.LessThanOrEquals)
					+ firstValue);
			buffer.append(" " + LogicalOperator.And + " " + attributeName
					+ RelationalOperator.getSQL(RelationalOperator.GreaterThanOrEquals)
					+ secondValue + ")");
		}
		else if (operator.equals(RelationalOperator.In)) // Processing In Operator
		{
			buffer.append(attributeName + " " + strOperator + " (");
			List<String> valueList = condition.getValues();
			for (int i = 0; i < valueList.size(); i++)
			{
				String value = modifyValueforDataType(valueList.get(i), dataType);

				if (i == valueList.size() - 1)
					buffer.append(value + ")");
				else
					buffer.append(value + ",");
			}
		}
		else if (operator.equals(RelationalOperator.IsNotNull)
				|| operator.equals(RelationalOperator.IsNull)) // Processing isNull & isNotNull operator.
		{
			buffer.append(attributeName + " " + strOperator);
		}
		else if (operator.equals(RelationalOperator.Contains)
				|| operator.equals(RelationalOperator.StartsWith)
				|| operator.equals(RelationalOperator.EndsWith)) // Processing String related Operators.
		{
			String value = condition.getValue();
			if (operator.equals(RelationalOperator.Contains))
				value = "'%" + value + "%'";
			else if (operator.equals(RelationalOperator.StartsWith))
				value = "'" + value + "%'";
			else if (operator.equals(RelationalOperator.EndsWith))
				value = "'%" + value + "'";

			buffer.append(attributeName + " like " + value);
		}
		else
		// Processing rest operators like =, !=, <, > , <=, >= etc.
		{
			String value = condition.getValue();
			value = modifyValueforDataType(value, dataType);
			buffer.append(attributeName + strOperator + value);
		}

		return buffer.toString();
	}

	/**
	 * To Modify value as per the Data type. 
	 * 1. Enclose the Given values by single Quotes for String & Date Data type. 
	 * 2. For Boolean DataType it will change value to 1 if its TRUE, else 0.
	 * @param value the Modified value.
	 * @param dataType The DataType of the passed value.
	 * @return The encoded String.
	 */
	String modifyValueforDataType(String value, DataType dataType)
	{

		if (dataType.equals(DataType.String) || dataType.equals(DataType.Date))
			value = "'" + value + "'";
		else if (dataType.equals(DataType.Boolean))
		{
			if (value != null && value.toUpperCase().equals("TRUE"))
				value = "1";
			else
				value = "0";
		}
		return value;
	}

	/**
	 * Get the SQL representatio for Attribute.
	 * @param attribute The reference to Attribute
	 * @return The SQL representatio for Attribute.
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	String getSQL(IAttribute attribute, IExpression expression)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		Entity entity = getEntity(attribute.getUMLClass());
		String tableName = getAliasName(entity, expression);
		Attribute entityAttribute = getAttribute(attribute);
		return tableName + "." + entityAttribute.getColumnProperties().getName();
	}

	/**
	 * To get the Alias Name for the given Entity.
	 * @param entity the reference to Entity.
	 * @param expression TODO
	 * @return The Alias Name for the given Entity.
	 */
	private String getAliasName(Entity entity, IExpression expression)
	{
		String tableName = entity.getName();
		Integer alias = aliasMap.get(expression.getExpressionId());
		if (alias == null)
			alias = new Integer(0);
		tableName = tableName.substring(tableName.lastIndexOf('.') + 1, tableName.length()) + alias;

		return tableName;
	}

	int createAliasAppenderMap(IExpression expression, int currentAliasCount, Integer aliasToSet)
			throws MultipleRootsException
	{
		aliasMap.put(expression.getExpressionId(), aliasToSet);
		for (int index = 0; index < expression.numberOfOperands(); index++)
		{
			IExpressionOperand operand = expression.getOperand(index);
			if (operand.isSubExpressionOperand())
			{
				IExpression childExpression = constraints.getExpression((IExpressionId) operand);

				List<IAssociation> path = joinGraph.getEdgePath(childExpression.getExpressionId());

				IExpressionId simillarPathExpressionId = pathMap.get(path);
				if (simillarPathExpressionId != null) // use already existing alias.
				{
					aliasToSet = aliasMap.get(simillarPathExpressionId);
				}
				else
				// define new alias.
				{

					pathMap.put(path, childExpression.getExpressionId());
					aliasToSet = new Integer(++currentAliasCount);// assigned alias to this class, hence increment currentAliasCount. 
				}
				currentAliasCount = createAliasAppenderMap(childExpression, currentAliasCount,
						aliasToSet);
			}
		}
		return currentAliasCount;
	}

	/**
	 * @return the aliasMap
	 */
	Map<IExpressionId, Integer> getAliasMap()
	{
		return aliasMap;
	}

	/**
	 * To get the Entity Class from Dynamic Extension for the given class. 
	 * It caches the Entity in the entityMap. First it checks whether the Entity is cached, if it is cached then it returns it from cache, else get it from Dynamic Extension. 
	 * @param iClass The IClass reference.
	 * @return The reference to Entity Class from Dynamic Extension for the given class.
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	private Entity getEntity(IClass iClass) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		String fullyQualifiedName = iClass.getFullyQualifiedName();
		Entity entity = entityMap.get(fullyQualifiedName);
		if (entity == null)
		{
			entity = (Entity) entityManager.getEntityByName(fullyQualifiedName);
			entityMap.put(fullyQualifiedName, entity);
		}
		return entity;
	}

	/**
	 * To get the Attribute from Dynamic Extenstion for the Given Attribute.
	 * @param attribute The IAttribute reference.
	 * @return The reference to Attribute from Dynamic Extenstion for the Given Attribute.
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	private Attribute getAttribute(IAttribute attribute) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		Entity entity = getEntity(attribute.getUMLClass());

		Attribute entityAttribute = (Attribute) entityManager.getAttribute(entity.getName(),
				attribute.getAttributeName());

		return entityAttribute;
	}

	//
	//	  Not using this method, since Query Building Logic is changed.
	//	/**
	//	 * To get the Primary key attribute of the Class. 
	//	 * @param iClass the iClass reference.
	//	 * @param expression TODO
	//	 * @return the actual Column name of the primary Attribute concatenated with alias name.
	//	 * @throws DynamicExtensionsApplicationException 
	//	 * @throws DynamicExtensionsSystemException 
	//	 * @throws NoSuchElementException if The Primary key Attribute not found.
	//	 */
	//	private String getPrimaryKeyAttrinbute(IClass iClass, IExpression expression) throws DynamicExtensionsSystemException,
	//			DynamicExtensionsApplicationException
	//	{
	//		Entity entity = getEntity(iClass);
	//
	//		Collection attributeCollection = entity.getAbstractAttributeCollection();
	//		for (Iterator iter = attributeCollection.iterator(); iter.hasNext();)
	//		{
	//			Attribute element = (Attribute) iter.next();
	//			Boolean isPrimaryKey = element.getIsPrimaryKey();
	//
	//			if (isPrimaryKey != null && isPrimaryKey.booleanValue() == true)
	//			{
	//				String primaryKeyName = getAliasName(entity, expression) + "."
	//						+ element.getColumnProperties().getName();
	//				return primaryKeyName;
	//			}
	//		}
	//
	//		throw new NoSuchElementException(Constants.SYSTEM_IDENTIFIER
	//				+ " Attribute not found for class " + iClass.getFullyQualifiedName());
	//	}

	void setJoinGraph(JoinGraph joinGraph)
	{
		this.joinGraph = joinGraph;
	}
}