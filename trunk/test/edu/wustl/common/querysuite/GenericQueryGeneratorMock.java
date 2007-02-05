/**
 * 
 */

package edu.wustl.common.querysuite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import edu.common.dynamicextensions.domain.Attribute;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domain.databaseproperties.TableProperties;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.ColumnPropertiesInterface;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IConstraintEntity;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import edu.wustl.common.querysuite.queryobject.impl.Expression;

/**
 * @author prafull_kadam
 * To create Queries on Dummy entity data.
 * It does not use Entity Manager, Test queries on dummy Entity. 
 * Specifically designed to create entities & queries objects of each data type with possible operators. 

 */
public class GenericQueryGeneratorMock
{

	private static DomainObjectFactory factory = DomainObjectFactory.getInstance();

	/**
	 * To create one dummy entity.
	 * @param name Name of the Entity.
	 * @return The entity.
	 */
	public static EntityInterface createEntity(String name)
	{
		EntityInterface e = factory.createEntity();
		e.setName(name);
		e.setCreatedDate(new Date());
		e.setDescription("This is a Dummy entity");
		e.setId(1L);
		e.setLastUpdated(new Date());

		((Entity) e).setAbstractAttributeCollection(getAttributes());

		TableProperties tableProperties = new TableProperties();
		tableProperties.setName("catissue_temp");
		tableProperties.setId(1L);
		((Entity) e).setTableProperties(tableProperties);
		return e;
	}

	/**
	 * TO create attribute list, which contains all types of attributes.
	 * @return list of attributes.
	 */
	public static ArrayList getAttributes()
	{
		ArrayList<AttributeInterface> attributes = new ArrayList<AttributeInterface>();

		AttributeInterface att1 = factory.createIntegerAttribute();
		att1.setName("long");
		ColumnPropertiesInterface c1 = factory.createColumnProperties();
		c1.setName("LONG_ATTRIBUTE");
		((Attribute) att1).setColumnProperties(c1);
		att1.setIsPrimaryKey(true);

		AttributeInterface att2 = factory.createDateAttribute();
		att2.setName("date");
		ColumnPropertiesInterface c2 = factory.createColumnProperties();
		c2.setName("DATE_ATTRIBUTE");
		((Attribute) att2).setColumnProperties(c2);

		AttributeInterface att3 = factory.createLongAttribute();
		att3.setName("int");
		ColumnPropertiesInterface c3 = factory.createColumnProperties();
		c3.setName("INT_ATTRIBUTE");
		((Attribute) att3).setColumnProperties(c3);

		AttributeInterface att4 = factory.createStringAttribute();
		att4.setName("string");
		ColumnPropertiesInterface c4 = factory.createColumnProperties();
		c4.setName("STRING_ATTRIBUTE");
		((Attribute) att4).setColumnProperties(c4);

		AttributeInterface att5 = factory.createBooleanAttribute();
		att5.setName("boolean");
		ColumnPropertiesInterface c5 = factory.createColumnProperties();
		c5.setName("BOOLEAN_ATTRIBUTE");
		((Attribute) att5).setColumnProperties(c5);

		AttributeInterface att6 = factory.createDoubleAttribute();
		att6.setName("double");
		ColumnPropertiesInterface c6 = factory.createColumnProperties();
		c6.setName("DOUBLE_ATTRIBUTE");
		((Attribute) att6).setColumnProperties(c6);

		AttributeInterface att7 = factory.createFloatAttribute();
		att7.setName("float");

		ColumnPropertiesInterface c7 = factory.createColumnProperties();
		c7.setName("FLOAT_ATTRIBUTE");
		((Attribute) att7).setColumnProperties(c7);
		(att7).setIsPrimaryKey(new Boolean(true));

		attributes.add(0, att1);
		attributes.add(1, att2);
		attributes.add(2, att3);
		attributes.add(3, att4);
		attributes.add(4, att5);
		attributes.add(5, att6);
		attributes.add(6, att7);

		return attributes;
	}

	/**
	 * To create expression for Dummy entity. with rule as [name in (1,2,3,4)]
	 * @return the Expression.
	 */
	public static IExpression createExpression(EntityInterface entity)
	{
		IConstraintEntity constraintEntity = QueryObjectFactory.createConstraintEntity(entity);

		IExpression expression = new Expression(constraintEntity, 1);
		expression.addOperand(createRule(constraintEntity.getDynamicExtensionsEntity(), "int"));
		return expression;
	}

	/**
	 * Create Rule for given Participant as : name in (1,2,3,4)
	 * @param entity The Dynamic Extension Entity Paricipant
	 * @return The Rule Object.
	 */
	public static IRule createRule(EntityInterface entity, String name)
	{
		List<ICondition> conditions = new ArrayList<ICondition>();
		conditions.add(createInCondition(entity, name));
		IRule rule = QueryObjectFactory.createRule(conditions);
		return rule;
	}

	/**
	 * Cretate Condition for given entity & attributeName : name in (1,2,3,4)
	 * @param entity The Dynamic Extension Entity
	 * @return The Condition object.
	 */
	public static ICondition createInCondition(EntityInterface entity, String name)
	{
		List<String> values = new ArrayList<String>();
		values.add("1");
		values.add("2");
		values.add("3");
		values.add("4");
		AttributeInterface attribute = findAttribute(entity, name);
		ICondition condition = QueryObjectFactory.createCondition(attribute, RelationalOperator.In,
				values);
		return condition;
	}

	/**
	 * To search attribute in the Entity.
	 * @param entity The Dynamic Extension Entity Paricipant.
	 * @param attributeName The name of the attribute to search. 
	 * @return The corresponding attibute.
	 */
	public static AttributeInterface findAttribute(EntityInterface entity, String attributeName)
	{
		Collection attributes = entity.getAbstractAttributeCollection();
		for (Iterator iter = attributes.iterator(); iter.hasNext();)
		{
			AttributeInterface attribute = (AttributeInterface) iter.next();
			if (attribute.getName().equals(attributeName))
				return attribute;
		}
		return null;
	}
}
