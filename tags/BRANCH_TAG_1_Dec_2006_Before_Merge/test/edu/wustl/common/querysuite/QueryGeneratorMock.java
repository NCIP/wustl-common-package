/**
 * 
 */

package edu.wustl.common.querysuite;

import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.common.querysuite.exceptions.CyclicException;
import edu.wustl.common.querysuite.factory.QueryObjectFactory;
import edu.wustl.common.querysuite.queryobject.DataType;
import edu.wustl.common.querysuite.queryobject.IAssociation;
import edu.wustl.common.querysuite.queryobject.IAttribute;
import edu.wustl.common.querysuite.queryobject.IClass;
import edu.wustl.common.querysuite.queryobject.ICondition;
import edu.wustl.common.querysuite.queryobject.IConstraints;
import edu.wustl.common.querysuite.queryobject.IExpression;
import edu.wustl.common.querysuite.queryobject.IExpressionOperand;
import edu.wustl.common.querysuite.queryobject.IJoinGraph;
import edu.wustl.common.querysuite.queryobject.ILogicalConnector;
import edu.wustl.common.querysuite.queryobject.IQuery;
import edu.wustl.common.querysuite.queryobject.IRule;
import edu.wustl.common.querysuite.queryobject.LogicalOperator;
import edu.wustl.common.querysuite.queryobject.RelationalOperator;
import edu.wustl.common.querysuite.queryobject.impl.Expression;

/**
 * Mock to create The Query Objects.
 * @author prafull_kadam
 *
 */
public class QueryGeneratorMock
{

	static EntityManagerMock enitytManager = new EntityManagerMock();
	/**
	 * Create Participant Class: with Attributes as: id, activityStatus, birthDate
	 * @return  Participant Class.
	 */
	public static IClass createParticantClass()
	{
		List<IAttribute> attributes = new ArrayList<IAttribute>();

		IClass class1 = QueryObjectFactory.createClass("edu.wustl.catissuecore.domain.Participant", attributes, null, true);

		IAttribute att1 = QueryObjectFactory.createAttribute(DataType.String, class1,
				"activityStatus");
		attributes.add(att1);

		IAttribute att2 = QueryObjectFactory.createAttribute(DataType.Date, class1, "birthDate");
		attributes.add(att2);

		IAttribute att3 = QueryObjectFactory.createAttribute(DataType.Integer, class1, "id");
		attributes.add(att3);

		return class1;
	}

	/**
	 * Create Condition for given Participant: activityStatus = 'Active'
	 * @param participant
	 * @return The Condition object.
	 */
	public static ICondition createParticipantCondition1(IClass participant)
	{
		List<String> values = new ArrayList<String>();
		values.add("Active");
		ICondition condition = QueryObjectFactory.createCondition(participant.getAttributes()
				.get(0), RelationalOperator.Equals, values);

		return condition;
	}

	/**
	 * Cretate Condtionion for given Participant: id in (1,2,3,4)
	 * @param participant
	 * @return
	 */
	public static ICondition createParticipantCondition2(IClass participant)
	{
		List<String> values = new ArrayList<String>();
		values.add("1");
		values.add("2");
		values.add("3");
		values.add("4");
		ICondition condition = QueryObjectFactory.createCondition(participant.getAttributes()
				.get(2), RelationalOperator.In, values);
		return condition;
	}

	/**
	 * Cretate Condtionion for given Participant: birthDate between ('1-1-2000','1-2-2000')
	 * @param participant
	 * @return
	 */
	public static ICondition createParticipantCondition3(IClass participant)
	{
		List<String> values = new ArrayList<String>();
		values.add("1-1-2000");
		values.add("1-2-2000");
		ICondition condition = QueryObjectFactory.createCondition(participant.getAttributes()
				.get(1), RelationalOperator.Between, values);
		return condition;
	}

	/**
	 * Cretate Condtionion for given Participant: id < 10 
	 * @param participant
	 * @return
	 */
	public static ICondition createParticipantCondition4(IClass participant)
	{
		List<String> values = new ArrayList<String>();
		values.add("10");
		ICondition condition = QueryObjectFactory.createCondition(participant.getAttributes()
				.get(2), RelationalOperator.LessThan, values);
		return condition;
	}

	/**
	 * Create Condition for given Participant: activityStatus Contains 'Active'
	 * @param participant
	 * @return The Condition object.
	 */
	public static ICondition createParticipantCondition5(IClass participant)
	{
		List<String> values = new ArrayList<String>();
		values.add("Active");
		ICondition condition = QueryObjectFactory.createCondition(participant.getAttributes()
				.get(0), RelationalOperator.Contains, values);

		return condition;
	}

	/**
	 * Create Rule for given Participant as : id in (1,2,3,4) AND birthDate between ('1-1-2000','1-2-2000')
	 * @param participant
	 * @return The Rule Created.
	 */
	public static IRule createParticipantRule1(IClass participant, IExpression containingExpression)
	{
		List<ICondition> conditions = new ArrayList<ICondition>();
		conditions.add(createParticipantCondition2(participant));
		conditions.add(createParticipantCondition3(participant));
		IRule rule = QueryObjectFactory.createRule(conditions, containingExpression);
		return rule;
	}

	/**
	 * Create Rule for given Participant as : activityStatus = 'Active'
	 * @param participant
	 * @return The Rule reference.
	 */
	public static IRule createParticipantRule2(IClass participant,IExpression containingExpression)
	{
		List<ICondition> conditions = new ArrayList<ICondition>();
		conditions.add(createParticipantCondition1(participant));
		IRule rule = QueryObjectFactory.createRule(conditions, containingExpression);
		
		return rule;
	}

	/**
	 * Create Rule for given Participant as : id < 10
	 * @param participant
	 * @return The Rule reference.
	 */
	public static IRule createParticipantRule3(IClass participant, IExpression containingExpression)
	{
		List<ICondition> conditions = new ArrayList<ICondition>();
		conditions.add(createParticipantCondition4(participant));
		IRule rule = QueryObjectFactory.createRule(conditions, containingExpression);
		return rule;
	}

	/**
	 * Create Expression for given Participant as: [id in (1,2,3,4) AND birthDate between ('1-1-2000','1-2-2000')] OR [activityStatus = 'Active']
	 * @param participant
	 * @return The Expression reference.
	 */
	public static IExpression creatParticipantExpression1(IClass participant)
	{
		IExpression expression = new Expression(participant, 1);
		expression.addOperand(createParticipantRule1(participant, expression));
		ILogicalConnector connector = QueryObjectFactory.createLogicalConnector(LogicalOperator.Or);
		expression.addOperand(connector, createParticipantRule2(participant,expression));

		return expression;
	}

	
	/**
	 * Create Expression for given Participant as: [Expression1] And [id < 10]
	 * Where Expression1 is : [medicalRecordNumber = 'M001']
	 * @param participant
	 * @return
	 */
	public static IExpression creatParticipantExpression2(IClass participant)
	{
		IExpression expression = new Expression(null, 1);
		expression.addOperand(createParticipantRule3(participant,expression));
		ILogicalConnector connector = QueryObjectFactory.createLogicalConnector(
				LogicalOperator.And);
		expression.addOperand(connector, (IExpressionOperand) creatParticipantMedicalIdentifierExpression(createParticantMedicalIdentifierClass()));

		return expression;
	}

	/**
	 * To create IQuery for the Participant & Participant Medical Identifier as: [activityStatus = 'Active'] OR [medicalRecordNumber = 'M001']
	 * @param expression The Expression reference created by function creatParticipantExpression2()
	 * @return The corresponding join Graph reference.
	 */
	public static IQuery creatParticipantQuery1()
	{
		IClass participantClass = createParticantClass();
		IClass pmClass = createParticantMedicalIdentifierClass();
		
		IQuery query = QueryObjectFactory.createQuery();
		
		IConstraints constraints = QueryObjectFactory.createConstraints();
		query.setConstraints(constraints);
		
		IJoinGraph joinGraph = constraints.getJoinGraph();
		
		IExpression participantExpression = constraints.addExpression(participantClass);
		participantExpression.addOperand(createParticipantRule2(participantClass, participantExpression));
		
		IExpression pmExpression = constraints.addExpression(pmClass);
		
		ILogicalConnector connector = QueryObjectFactory.createLogicalConnector(LogicalOperator.And);
		participantExpression.addOperand(connector,pmExpression.getExpressionId());
		pmExpression.addOperand(createParticipantMedicalIdentifierRule1(pmClass,pmExpression));
		
		
		IAssociation association = QueryObjectFactory.createIntraModelAssociation(participantClass, pmClass, "participant", "participantMedicalIdentifierCollection", true);
		
		try
		{
			joinGraph.putAssociation(participantExpression.getExpressionId(), pmExpression.getExpressionId(),association);
		}
		catch (CyclicException e)
		{
		}
		return query;
	}

	public static IQuery createSCGQuery()
	{
		
		IClass specimenCollectionGroup= null;
		IClass specimenClass = null;
		EntityInterface specimenEntity= null;
		EntityInterface scgEntity = null;
		
		try
		{
			specimenEntity = enitytManager.getEntityByName(EntityManagerMock.SPECIMEN_NAME);
			specimenClass = QueryObjectFactory.createClass(specimenEntity);
			scgEntity = enitytManager.getEntityByName(EntityManagerMock.SPECIMEN_COLLECTION_GROUP_NAME);
			specimenCollectionGroup = QueryObjectFactory.createClass(scgEntity);
			
			
		}
		catch (DynamicExtensionsSystemException e)
		{
			return null;
		}
		
		List<String> values = new ArrayList<String>();
		values.add("value1");

		IQuery query = QueryObjectFactory.createQuery();
		IConstraints constraints = QueryObjectFactory.createConstraints();
		query.setConstraints(constraints);

		IJoinGraph joinGraph  = constraints.getJoinGraph();
		
		IExpression scgExpression = constraints.addExpression(specimenCollectionGroup);
		IRule SCGRule = QueryObjectFactory.createRule(null,scgExpression);
		ICondition SCGCondition = QueryObjectFactory.createCondition(specimenCollectionGroup.getAttributes()
				.get(0), RelationalOperator.StartsWith, values);
		SCGRule.addCondition(SCGCondition);
		scgExpression.addOperand(SCGRule);
		
		ILogicalConnector orConnector = QueryObjectFactory.createLogicalConnector(LogicalOperator.Or);
		IExpression SpecimenExpression1 = constraints.addExpression(specimenClass);
		IRule specimenRule1 = QueryObjectFactory.createRule(null,SpecimenExpression1);
		ICondition specimenCondition1 = QueryObjectFactory.createCondition(specimenClass.getAttributes()
				.get(0), RelationalOperator.StartsWith, values);
		specimenRule1.addCondition(specimenCondition1);
		SpecimenExpression1.addOperand(specimenRule1);
		scgExpression.addOperand(orConnector, SpecimenExpression1.getExpressionId());
		
		
		IExpression childSpecimenExpression = constraints.addExpression(specimenClass);
		IRule childSpecimenRule = QueryObjectFactory.createRule(null,childSpecimenExpression);
		ICondition childSpecimenCondition = QueryObjectFactory.createCondition(specimenClass.getAttributes()
				.get(0), RelationalOperator.StartsWith, values);
		
		childSpecimenRule.addCondition(childSpecimenCondition);
		childSpecimenExpression.addOperand(childSpecimenRule);
		SpecimenExpression1.addOperand(orConnector, childSpecimenExpression.getExpressionId());
		
		
		IExpression SpecimenExpression2 = constraints.addExpression(specimenClass);
		IRule specimenRule2 = QueryObjectFactory.createRule(null,SpecimenExpression2);
		ICondition specimenCondition2 = QueryObjectFactory.createCondition(specimenClass.getAttributes()
				.get(3), RelationalOperator.Equals, values);
		specimenRule2.addCondition(specimenCondition2);
		SpecimenExpression2.addOperand(specimenRule2);
//		ILogicalConnector pAndConnector = QueryObjectFactory.createLogicalConnector(LogicalOperator.PAnd);
		ILogicalConnector pAndConnector = QueryObjectFactory.createLogicalConnector(LogicalOperator.And);
		scgExpression.addOperand(pAndConnector, SpecimenExpression2.getExpressionId());		

		
		IAssociation scgAndSpecimenAssociation = QueryObjectFactory.createIntraModelAssociation(specimenCollectionGroup, specimenClass, "specimenCollectionGroup", "specimenCollection", true);
		IAssociation specimenAndSpecimenAssociation = QueryObjectFactory.createIntraModelAssociation(specimenClass, specimenClass, "childrenSpecimen", "parentSpecimen", true);
		
		//----
		IExpression SpecimenExpression3 = constraints.addExpression(specimenClass);
		IRule specimenRule3 = QueryObjectFactory.createRule(null,SpecimenExpression3);
		ICondition specimenCondition3 = QueryObjectFactory.createCondition(specimenClass.getAttributes()
				.get(3), RelationalOperator.NotEquals, values);
		specimenRule3.addCondition(specimenCondition3);
		SpecimenExpression3.addOperand(specimenRule3);
		scgExpression.addOperand(orConnector, SpecimenExpression3.getExpressionId());		
		//---
		try
		{
			joinGraph.putAssociation(scgExpression.getExpressionId(), SpecimenExpression1.getExpressionId(), scgAndSpecimenAssociation);
			joinGraph.putAssociation(scgExpression.getExpressionId(), SpecimenExpression2.getExpressionId(), scgAndSpecimenAssociation);
			joinGraph.putAssociation(SpecimenExpression1.getExpressionId(), childSpecimenExpression.getExpressionId(), specimenAndSpecimenAssociation);
			
			//--
			joinGraph.putAssociation(scgExpression.getExpressionId(), SpecimenExpression3.getExpressionId(), scgAndSpecimenAssociation);
			//--
		}
		catch (CyclicException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return query;
	}
	/**
	 * To create IQuery for the Participant as: [activityStatus = 'Active']
	 * @param expression The Expression reference created by function creatParticipantExpression2()
	 * @return The corresponding join Graph reference.
	 */
	public static IQuery creatParticipantQuery2()
	{
		IClass participantClass = createParticantClass();
		
		IQuery query = QueryObjectFactory.createQuery();
		
		IConstraints constraints = QueryObjectFactory.createConstraints();
		query.setConstraints(constraints);
		
		IExpression participantExpression = constraints.addExpression(participantClass);
		participantExpression.addOperand(createParticipantRule2(participantClass,participantExpression));
		
		
		return query;
	}
	
	/**
	 * Create Participant Medical Identifier Class: with Attributes as: id, medicalRecordNumber, ParticipantId
	 * @return  Participant Medical Identifier Class.
	 */
	public static IClass createParticantMedicalIdentifierClass()
	{
		List<IAttribute> attributes = new ArrayList<IAttribute>();

		IClass class1 = QueryObjectFactory.createClass("edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier", attributes, null, true);

		IAttribute att1 = QueryObjectFactory.createAttribute(DataType.Integer, class1, "id");
		attributes.add(att1);

		IAttribute att2 = QueryObjectFactory.createAttribute(DataType.String, class1,"medicalRecordNumber");
		attributes.add(att2);

		IAttribute att3 = QueryObjectFactory.createAttribute(DataType.Integer, class1, "ParticipantId");
		attributes.add(att3);

		return class1;
	}
	
	/**
	 * Create Condition for given Participant: medicalRecordNumber = 'M001'
	 * @param participantMedicalId The Iclass representing Participant Medical Identifier obje
	 * @return The Condition object reference.
	 */
	public static ICondition createParticipantMedicalIdentifierCondition1(IClass participantMedicalId)
	{
		List<String> values = new ArrayList<String>();
		values.add("M001");
		ICondition condition = QueryObjectFactory.createCondition(participantMedicalId.getAttributes()
				.get(1), RelationalOperator.Equals, values);

		return condition;
	}
	
	/**
	 * Create Rule for Participant Medical Identifier as: [medicalRecordNumber = 'M001']
	 * @param participantMedicalId The Iclass representing Participant Medical Identifier obje 
	 * @return The Rule reference.
	 */
	private static IRule createParticipantMedicalIdentifierRule1(IClass participantMedicalId, IExpression containingExpression)
	{
		List<ICondition> conditions = new ArrayList<ICondition>();
		conditions.add(createParticipantMedicalIdentifierCondition1(participantMedicalId));
		IRule rule = QueryObjectFactory.createRule(conditions, containingExpression);
		return rule;
	}
	
	/**
	 * Create Expression for Participant Medical Identifier as: [medicalRecordNumber = 'M001']
	 * @param participantMedicalId The Iclass representing Participant Medical Identifier object.
	 * @return The Expression reference
	 */
	public static IExpression creatParticipantMedicalIdentifierExpression(IClass participantMedicalId)
	{
		IExpression expression = new Expression(null, 2);
		IRule rule = createParticipantMedicalIdentifierRule1(participantMedicalId,expression);
		expression.addOperand(rule);
		return expression;
	}
	
	/**
	 * 
	 * @return the IQuery Object representation for the sample query no. 1 in the "SampleQueriesWithMultipleSubQueryApproach.doc"
	 * 
	 *  P: LastNameStarts with 'S'
	 *  	C: ANY
	 *  		G: ANY
	 *  			S: Type equals "Fixed Tissue"
	 *  					OR
	 *  			S: Type equals "Fresh Tissue" 
	 *  	
	 */
	public static IQuery createSampleQuery1()
	{
		IQuery query = QueryObjectFactory.createQuery();

		try
		{
			IClass participantClass = null;
			IClass collectionProtocolRegClass = null;
			IClass specimenCollectionGroup = null;
			IClass specimenClass = null;

			participantClass = QueryObjectFactory.createClass(enitytManager
					.getEntityByName(EntityManagerMock.PARTICIPANT_NAME));
			collectionProtocolRegClass = QueryObjectFactory.createClass(enitytManager
					.getEntityByName(EntityManagerMock.COLLECTION_PROTOCOL_REGISTRATION_NAME));
			specimenClass = QueryObjectFactory.createClass(enitytManager
					.getEntityByName(EntityManagerMock.SPECIMEN_NAME));
			specimenCollectionGroup = QueryObjectFactory.createClass(enitytManager
					.getEntityByName(EntityManagerMock.SPECIMEN_COLLECTION_GROUP_NAME));

			IConstraints constraints = QueryObjectFactory.createConstraints();
			query.setConstraints(constraints);

			IJoinGraph joinGraph = constraints.getJoinGraph();
			ILogicalConnector andConnector = QueryObjectFactory
					.createLogicalConnector(LogicalOperator.And);

			// creating expression for Participant.
			IExpression participantExpression = constraints.addExpression(participantClass);
			IRule participantRule = QueryObjectFactory.createRule(participantExpression);
			List<String> participantValues = new ArrayList<String>();
			participantValues.add("s");
			ICondition ParticipantCondition = QueryObjectFactory.createCondition(participantClass
					.getAttributes().get(7), RelationalOperator.StartsWith, participantValues);
			participantRule.addCondition(ParticipantCondition);
			participantExpression.addOperand(participantRule);

			// creating expression for collection Protocol Registration
			IExpression collectionProtocolRegExpression = constraints
					.addExpression(collectionProtocolRegClass);
			participantExpression.addOperand(andConnector, collectionProtocolRegExpression
					.getExpressionId());
			AssociationInterface participanCPRegAssociation = enitytManager.getAssociation(
					EntityManagerMock.PARTICIPANT_NAME,
					EntityManagerMock.COLLECTION_PROTOCOL_REGISTRATION_NAME, "participant");
			joinGraph.putAssociation(participantExpression.getExpressionId(),
					collectionProtocolRegExpression.getExpressionId(), QueryObjectFactory
							.createAssociation(participanCPRegAssociation));

			//			// creating expression for collection Protocol
			//			IExpression collectionProtocolExpression = constraints.addExpression(collectionProtocolClass);
			//			collectionProtocolRegExpression.addOperand(collectionProtocolExpression.getExpressionId());
			//			AssociationInterface participanCPAssociation = enitytManager.getAssociation(EntityManagerMock.COLLECTION_PROTOCOL_REGISTRATION_NAME, EntityManagerMock.COLLECTION_PROTOCOL_NAME, "");
			//			joinGraph.putAssociation(collectionProtocolRegExpression.getExpressionId(), collectionProtocolExpression.getExpressionId(), QueryObjectFactory.createAssociation(participanCPAssociation));
			//	
			//			List<String> collectionProtocolExpressionValues = new ArrayList<String>();
			//			collectionProtocolExpressionValues.add("cpr");
			//			IAttribute collectionProtocolNameAttribute = QueryObjectFactory.getAttribute(enitytManager.getAttribute(EntityManagerMock.COLLECTION_PROTOCOL_NAME, "type"),specimenClass);
			//			ICondition specimenExpression1Rule1Condition1 = QueryObjectFactory.createCondition(collectionProtocolNameAttribute, RelationalOperator.StartsWith, collectionProtocolExpressionValues);

			//			IExpression CollectionProtocolEventExpression = constraints.addExpression(collectionProtocolEventClass);
			//			collectionProtocolRegExpression.addOperand(CollectionProtocolEventExpression.getExpressionId());
			//			AssociationInterface participanCPAssociation = enitytManager.getAssociation(EntityManagerMock.PARTICIPANT_NAME, "participant");
			//			joinGraph.putAssociation(collectionProtocolRegExpression.getExpressionId(), collectionProtocolExpression.getExpressionId(), QueryObjectFactory.createAssociation(participanCPAssociation));

			// creating expression for SpecimenCollectionGroup.
			IExpression specimenCollectionGroupExpression = constraints
					.addExpression(specimenCollectionGroup);
			collectionProtocolRegExpression.addOperand(specimenCollectionGroupExpression
					.getExpressionId());
			AssociationInterface cprAndSpgAssociation = enitytManager.getAssociation(
					EntityManagerMock.COLLECTION_PROTOCOL_REGISTRATION_NAME,
					EntityManagerMock.SPECIMEN_COLLECTION_GROUP_NAME,
					"collectionProtocolRegistration");
			joinGraph.putAssociation(collectionProtocolRegExpression.getExpressionId(),
					specimenCollectionGroupExpression.getExpressionId(), QueryObjectFactory
							.createAssociation(cprAndSpgAssociation));

			//creating SpecimeExpression.
			IExpression specimenExpression1 = constraints.addExpression(specimenClass);

			specimenCollectionGroupExpression.addOperand(specimenExpression1.getExpressionId());
			AssociationInterface spgAndSpecimeAssociation = enitytManager.getAssociation(
					EntityManagerMock.SPECIMEN_COLLECTION_GROUP_NAME,
					EntityManagerMock.SPECIMEN_NAME, "specimenCollectionGroup");
			joinGraph.putAssociation(specimenCollectionGroupExpression.getExpressionId(),
					specimenExpression1.getExpressionId(), QueryObjectFactory
							.createAssociation(spgAndSpecimeAssociation));

			List<String> specimenExpression1Rule1Values = new ArrayList<String>();
			specimenExpression1Rule1Values.add("Fixed Tissue");

			IAttribute specimenAttributeType = QueryObjectFactory.getAttribute(enitytManager
					.getAttribute(EntityManagerMock.SPECIMEN_NAME, "type"), specimenClass);
			ICondition specimenExpression1Rule1Condition1 = QueryObjectFactory.createCondition(
					specimenAttributeType, RelationalOperator.Equals,
					specimenExpression1Rule1Values);
			IRule specimenExpression1Rule1 = QueryObjectFactory.createRule(specimenExpression1);
			specimenExpression1Rule1.addCondition(specimenExpression1Rule1Condition1);
			specimenExpression1.addOperand(specimenExpression1Rule1);

			ILogicalConnector orConnetor = QueryObjectFactory
					.createLogicalConnector(LogicalOperator.Or);
			List<String> specimenExpression1Rule2Values = new ArrayList<String>();
			specimenExpression1Rule2Values.add("Fresh Tissue");
			ICondition specimenExpression1Rule2Condition1 = QueryObjectFactory.createCondition(
					specimenAttributeType, RelationalOperator.Equals,
					specimenExpression1Rule2Values);
			IRule specimenExpression1Rule2 = QueryObjectFactory.createRule(specimenExpression1);
			specimenExpression1Rule2.addCondition(specimenExpression1Rule2Condition1);
			specimenExpression1.addOperand(orConnetor, specimenExpression1Rule2);

		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return query;
	}

	/**
	 * 
	 * @return the IQuery Object representation for the sample query no. 3 in the "SampleQueriesWithMultipleSubQueryApproach.doc"
	 * 
	 *  P: ANY
	 *  	C: ANY
	 *  		G: Clinical status equals "New Diagnosis"
	 *  			S: Type equals "DNA"
	 *  			OR
	 *  			S: Type equals "Fresh Tissue" 
	 *  		Pseudo AND
	 *  		G: Clinical status equals "Post Operative"
	 *  			S: Type equals "Fixed Tissue"
	 *  			OR
	 *  			S: Type equals "Fresh Tissue" 
	 *  	
	 */
	public static IQuery createSampleQuery3()
	{
		IQuery query = QueryObjectFactory.createQuery();

		try
		{
			IClass participantClass = null;
			IClass collectionProtocolRegClass = null;
			IClass specimenCollectionGroup = null;
			IClass specimenClass = null;

			participantClass = QueryObjectFactory.createClass(enitytManager
					.getEntityByName(EntityManagerMock.PARTICIPANT_NAME));
			collectionProtocolRegClass = QueryObjectFactory.createClass(enitytManager
					.getEntityByName(EntityManagerMock.COLLECTION_PROTOCOL_REGISTRATION_NAME));
			specimenClass = QueryObjectFactory.createClass(enitytManager
					.getEntityByName(EntityManagerMock.SPECIMEN_NAME));
			specimenCollectionGroup = QueryObjectFactory.createClass(enitytManager
					.getEntityByName(EntityManagerMock.SPECIMEN_COLLECTION_GROUP_NAME));

			IConstraints constraints = QueryObjectFactory.createConstraints();
			query.setConstraints(constraints);

			IJoinGraph joinGraph = constraints.getJoinGraph();
			ILogicalConnector andConnector = QueryObjectFactory
					.createLogicalConnector(LogicalOperator.And);
			ILogicalConnector orConnector = QueryObjectFactory
					.createLogicalConnector(LogicalOperator.Or);

			// creating expression for Participant.
			IExpression participantExpression = constraints.addExpression(participantClass);

			// creating expression for collection Protocol Registration
			IExpression collectionProtocolRegExpression = constraints
					.addExpression(collectionProtocolRegClass);
			participantExpression.addOperand(collectionProtocolRegExpression.getExpressionId());
			AssociationInterface participanCPRegAssociation = enitytManager.getAssociation(
					EntityManagerMock.PARTICIPANT_NAME,
					EntityManagerMock.COLLECTION_PROTOCOL_REGISTRATION_NAME, "participant");
			joinGraph.putAssociation(participantExpression.getExpressionId(),
					collectionProtocolRegExpression.getExpressionId(), QueryObjectFactory
							.createAssociation(participanCPRegAssociation));

			// creating expression for SpecimenCollectionGroup.
			IExpression specimenCollectionGroupExpression1 = constraints
					.addExpression(specimenCollectionGroup);
			collectionProtocolRegExpression.addOperand(specimenCollectionGroupExpression1
					.getExpressionId());
			AssociationInterface cprAndSpgAssociation = enitytManager.getAssociation(
					EntityManagerMock.COLLECTION_PROTOCOL_REGISTRATION_NAME,
					EntityManagerMock.SPECIMEN_COLLECTION_GROUP_NAME,
					"collectionProtocolRegistration");
			joinGraph.putAssociation(collectionProtocolRegExpression.getExpressionId(),
					specimenCollectionGroupExpression1.getExpressionId(), QueryObjectFactory
							.createAssociation(cprAndSpgAssociation));

			IRule specimenCollectionGroupExpression1Rule1 = QueryObjectFactory
					.createRule(specimenCollectionGroupExpression1);
			List<String> specimenCollectionGroupExpression1Rule1Values = new ArrayList<String>();
			specimenCollectionGroupExpression1Rule1Values.add("New Diagnosis");
			ICondition specimenCollectionGroupExpression1Rule1Condition = QueryObjectFactory
					.createCondition(specimenCollectionGroup.getAttributes().get(2),
							RelationalOperator.Equals,
							specimenCollectionGroupExpression1Rule1Values);
			specimenCollectionGroupExpression1Rule1
					.addCondition(specimenCollectionGroupExpression1Rule1Condition);
			specimenCollectionGroupExpression1.addOperand(specimenCollectionGroupExpression1Rule1);

			//creating SpecimeExpression.
			IExpression specimenExpression1 = constraints.addExpression(specimenClass);

			specimenCollectionGroupExpression1.addOperand(andConnector, specimenExpression1
					.getExpressionId());
			AssociationInterface spgAndSpecimeAssociation = enitytManager.getAssociation(
					EntityManagerMock.SPECIMEN_COLLECTION_GROUP_NAME,
					EntityManagerMock.SPECIMEN_NAME, "specimenCollectionGroup");
			joinGraph.putAssociation(specimenCollectionGroupExpression1.getExpressionId(),
					specimenExpression1.getExpressionId(), QueryObjectFactory
							.createAssociation(spgAndSpecimeAssociation));

			List<String> specimenExpression1Rule1Values = new ArrayList<String>();
			specimenExpression1Rule1Values.add("DNA");

			IAttribute specimenAttributeType = QueryObjectFactory.getAttribute(enitytManager
					.getAttribute(EntityManagerMock.SPECIMEN_NAME, "type"), specimenClass);
			ICondition specimenExpression1Rule1Condition1 = QueryObjectFactory.createCondition(
					specimenAttributeType, RelationalOperator.Equals,
					specimenExpression1Rule1Values);
			IRule specimenExpression1Rule1 = QueryObjectFactory.createRule(specimenExpression1);
			specimenExpression1Rule1.addCondition(specimenExpression1Rule1Condition1);
			specimenExpression1.addOperand(specimenExpression1Rule1);

			List<String> specimenExpression1Rule2Values = new ArrayList<String>();
			specimenExpression1Rule1Values.add("Fresh Tissue");
			ICondition specimenExpression1Rule2Condition1 = QueryObjectFactory.createCondition(
					specimenAttributeType, RelationalOperator.Equals,
					specimenExpression1Rule2Values);
			IRule specimenExpression1Rule2 = QueryObjectFactory.createRule(specimenExpression1);
			specimenExpression1Rule2.addCondition(specimenExpression1Rule2Condition1);
			specimenExpression1.addOperand(orConnector, specimenExpression1Rule2);

			ILogicalConnector pandConnector = QueryObjectFactory
			.createLogicalConnector(LogicalOperator.And);
//			ILogicalConnector pandConnector = QueryObjectFactory
//			.createLogicalConnector(LogicalOperator.PAnd);
			//			 creating expression for SpecimenCollectionGroup2.
			IExpression specimenCollectionGroupExpression2 = constraints
					.addExpression(specimenCollectionGroup);
			collectionProtocolRegExpression.addOperand(pandConnector,
					specimenCollectionGroupExpression2.getExpressionId());
			//			AssociationInterface cprAndSpgAssociation = enitytManager.getAssociation(EntityManagerMock.COLLECTION_PROTOCOL_REGISTRATION_NAME,EntityManagerMock.SPECIMEN_COLLECTION_GROUP_NAME, "collectionProtocolRegistration");
			joinGraph.putAssociation(collectionProtocolRegExpression.getExpressionId(),
					specimenCollectionGroupExpression2.getExpressionId(), QueryObjectFactory
							.createAssociation(cprAndSpgAssociation));

			IRule specimenCollectionGroupExpression2Rule1 = QueryObjectFactory
					.createRule(specimenCollectionGroupExpression2);
			List<String> specimenCollectionGroupExpression2Rule1Values = new ArrayList<String>();
			specimenCollectionGroupExpression2Rule1Values.add("Post-Operative");
			ICondition specimenCollectionGroupExpression2Rule1Condition = QueryObjectFactory
					.createCondition(specimenCollectionGroup.getAttributes().get(2),
							RelationalOperator.Equals,
							specimenCollectionGroupExpression2Rule1Values);
			specimenCollectionGroupExpression2Rule1
					.addCondition(specimenCollectionGroupExpression2Rule1Condition);
			specimenCollectionGroupExpression2.addOperand(specimenCollectionGroupExpression2Rule1);

			//			creating SpecimeExpression.
			IExpression specimenExpression2 = constraints.addExpression(specimenClass);

			specimenCollectionGroupExpression2.addOperand(andConnector, specimenExpression2
					.getExpressionId());
			//			AssociationInterface spgAndSpecimeAssociation = enitytManager.getAssociation(EntityManagerMock.SPECIMEN_COLLECTION_GROUP_NAME,EntityManagerMock.SPECIMEN_NAME, "specimenCollectionGroup");
			joinGraph.putAssociation(specimenCollectionGroupExpression2.getExpressionId(),
					specimenExpression2.getExpressionId(), QueryObjectFactory
							.createAssociation(spgAndSpecimeAssociation));

			List<String> specimenExpression2Rule1Values = new ArrayList<String>();
			specimenExpression2Rule1Values.add("Fixed Tissue");

			//			IAttribute specimenAttributeType = QueryObjectFactory.getAttribute(enitytManager.getAttribute(EntityManagerMock.SPECIMEN_NAME, "type"),specimenClass);
			ICondition specimenExpression2Rule1Condition1 = QueryObjectFactory.createCondition(
					specimenAttributeType, RelationalOperator.Equals,
					specimenExpression2Rule1Values);
			IRule specimenExpression2Rule1 = QueryObjectFactory.createRule(specimenExpression2);
			specimenExpression2Rule1.addCondition(specimenExpression2Rule1Condition1);
			specimenExpression2.addOperand(specimenExpression2Rule1);

			List<String> specimenExpression2Rule2Values = new ArrayList<String>();
			specimenExpression2Rule2Values.add("Fixed Tissue");

			ICondition specimenExpression2Rule2Condition1 = QueryObjectFactory.createCondition(
					specimenAttributeType, RelationalOperator.Equals,
					specimenExpression2Rule2Values);
			IRule specimenExpression2Rule2 = QueryObjectFactory.createRule(specimenExpression2);
			specimenExpression2Rule2.addCondition(specimenExpression2Rule2Condition1);
			specimenExpression2.addOperand(orConnector, specimenExpression2Rule2);

		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return query;
	}	
}