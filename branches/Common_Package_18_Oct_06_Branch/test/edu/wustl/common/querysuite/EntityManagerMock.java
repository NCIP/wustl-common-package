
package edu.wustl.common.querysuite;

/**
 * @author Mandar Shidhore
 * @version 1.0
 * @created 17-Oct-2006 16:32:04 PM
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import edu.common.dynamicextensions.domain.Association;
import edu.common.dynamicextensions.domain.Attribute;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domain.Role;
import edu.common.dynamicextensions.domain.databaseproperties.ConstraintProperties;
import edu.common.dynamicextensions.domain.databaseproperties.TableProperties;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.ColumnPropertiesInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.global.Constants;

public class EntityManagerMock extends EntityManager
{
	DomainObjectFactory factory = DomainObjectFactory.getInstance();
	public List<EntityInterface> entityList = new ArrayList<EntityInterface>();
	public static String PARTICIPANT_NAME = "edu.wustl.catissuecore.domain.Participant";
	public static String PARTICIPANT_MEDICAL_ID_NAME = "edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier";
	public static String COLLECTION_PROTOCOL_NAME = "edu.wustl.catissuecore.domain.CollectionProtocol";
	public static String COLLECTION_PROTOCOL_REGISTRATION_NAME = "edu.wustl.catissuecore.domain.CollectionProtocolRegistration";
	public static String SPECIMEN_PROTOCOL_NAME = "edu.wustl.catissuecore.domain.SpecimenProtocol";
	public static String SPECIMEN_COLLECTION_GROUP_NAME = "edu.wustl.catissuecore.domain.SpecimenCollectionGroup";
	public static String COLLECTION_PROTOCOL_EVT_NAME = "edu.wustl.catissuecore.domain.CollectionProtocolEvent";
	public static String SPECIMEN_NAME = "edu.wustl.catissuecore.domain.Specimen";
	public static String SPECIMEN_CHARACTERISTIC_NAME = "edu.wustl.catissuecore.domain.SpecimenCharacteristics";
	public static String SPECIMEN_EVT_NAME = "edu.wustl.catissuecore.domain.SpecimenEventParameters";
	public static String CHKIN_CHKOUT_EVT_NAME = "edu.wustl.catissuecore.domain.CheckInCheckOutEventParameter";
	public static String FROZEN_EVT_NAME = "edu.wustl.catissuecore.domain.FrozenEventParameters";
	public static String PROCEDURE_EVT_NAME = "edu.wustl.catissuecore.domain.ProcedureEventParameters";
	public static String RECEIVED_EVT_NAME = "edu.wustl.catissuecore.domain.ReceivedEventParameters";
	public static String SITE_NAME = "edu.wustl.catissuecore.domain.Site";
	
	public static Long PARTICIPANT_ID = new Long(1);
	public static Long PARTICIPANT_MEDICAL_ID = new Long(2);
	public static Long COLLECTION_PROTOCOL_ID = new Long(4);
	public static Long COLLECTION_PROTOCOL_REGISTRATION_ID = new Long(3);
	public static Long SPECIMEN_PROTOCOL_ID = new Long(5);
	public static Long SPECIMEN_COLLECTION_GROUP_ID = new Long(7);
	public static Long COLLECTION_PROTOCOL_EVT_ID = new Long(6);
	public static Long SPECIMEN_ID = new Long(8);
	public static Long SPECIMEN_EVT_ID = new Long(9);
	public static Long CHKIN_CHKOUT_EVT_ID = new Long(10);
	public static Long FROZEN_EVT_ID = new Long(11);
	public static Long PROCEDURE_EVT_ID = new Long(12);
	public static Long RECEIVED_EVT_ID = new Long(13);
	public static Long SITE_ID = new Long(14);
	public static Long SPECIMEN_CHARACTERISTIC_ID = new Long(15);
	
	/**
	 * @see edu.common.dynamicextensions.entitymanager.EntityManager#findEntity(edu.common.dynamicextensions.domaininterface.EntityInterface)
	 */
	@Override
	public Collection findEntity(EntityInterface arg0)
	{
		// TODO Auto-generated method stub
		return super.findEntity(arg0);
	}

	/**
	 * @see edu.common.dynamicextensions.entitymanager.EntityManager#getAllEntities()
	 */
	@Override
	public Collection getAllEntities() throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		entityList.add((EntityInterface)getEntityByName(PARTICIPANT_NAME));
		entityList.add((EntityInterface)getEntityByName(PARTICIPANT_MEDICAL_ID_NAME));
		entityList.add((EntityInterface)getEntityByName(COLLECTION_PROTOCOL_REGISTRATION_NAME));
		entityList.add((EntityInterface)getEntityByName(COLLECTION_PROTOCOL_NAME));
		entityList.add((EntityInterface)getEntityByName(COLLECTION_PROTOCOL_EVT_NAME));
		entityList.add((EntityInterface)getEntityByName(CHKIN_CHKOUT_EVT_NAME));
		entityList.add((EntityInterface)getEntityByName(SITE_NAME));
		return entityList;
	}

//	/**
//	 * Remove this method after getting new Jar.
//	 */
//	public AssociationInterface getAssociation(String sourceEntityName,String targetEntityName, String sourceRoleName) throws DynamicExtensionsSystemException,
//	DynamicExtensionsApplicationException
//	{
//		AssociationInterface association = null;
//		if (sourceEntityName.equals(PARTICIPANT_NAME) && targetEntityName.equals(PARTICIPANT_MEDICAL_ID_NAME) && sourceRoleName.equals("participant"))
//		{
//			association = factory.createAssociation();
//	
//			EntityInterface sourceEntity = getEntityByName(PARTICIPANT_NAME);
//			EntityInterface targetEntity = getEntityByName(PARTICIPANT_MEDICAL_ID_NAME);
//	
//			association.setEntity(sourceEntity);
//			association.setTargetEntity(targetEntity);
//			association.setAssociationDirection(Constants.AssociationDirection.BI_DIRECTIONAL);
//	
//			Role sourceRole = new Role();
//			sourceRole.setName("participant");
//			sourceRole.setId(1L);
//			sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
//			sourceRole.setMaxCardinality(1);
//			sourceRole.setMinCardinality(1);
//			association.setSourceRole(sourceRole);
//	
//			Role targetRole = new Role();
//			targetRole.setName("participantMedicalIdentifierCollection");
//			targetRole.setId(2L);
//			targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
//			targetRole.setMaxCardinality(10);
//			targetRole.setMinCardinality(0);
//			association.setTargetRole(targetRole);
//	
//			
//			ConstraintProperties constraintProperties = new ConstraintProperties();
//			constraintProperties.setSourceEntityKey("IDENTIFIER");
//			constraintProperties.setTargetEntityKey("PARTICIPANT_ID");
//			((Association)association).setConstraintProperties(constraintProperties);
//			
//		}
//		else if (sourceEntityName.equals(PARTICIPANT_NAME) && targetEntityName.equals(COLLECTION_PROTOCOL_REGISTRATION_NAME)&& sourceRoleName.equals("participant"))
//		{
//			association = factory.createAssociation();
//	
//			EntityInterface sourceEntity = getEntityByName(PARTICIPANT_NAME);
//			EntityInterface targetEntity = getEntityByName(COLLECTION_PROTOCOL_REGISTRATION_NAME);
//	
//			association.setEntity(sourceEntity);
//			association.setTargetEntity(targetEntity);
//			association.setAssociationDirection(Constants.AssociationDirection.BI_DIRECTIONAL);
//	
//			Role sourceRole = new Role();
//			sourceRole.setName("participant");
//			sourceRole.setId(1L);
//			//TODO check association Type for linking: sourceRole.setAssociationType("linking");
//			sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
//			sourceRole.setMaxCardinality(1);
//			sourceRole.setMinCardinality(1);
//			association.setSourceRole(sourceRole);
//	
//			Role targetRole = new Role();
//			targetRole.setName("collectionProtocolRegistrationCollection");
//			targetRole.setId(2L);
//			//TODO check association Type for linking: targetRole.setAssociationsType("linking");
//			targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
//			targetRole.setMaxCardinality(10);
//			targetRole.setMinCardinality(0);
//			association.setTargetRole(targetRole);
//	
//			ConstraintProperties constraintProperties = new ConstraintProperties();
//			constraintProperties.setSourceEntityKey("IDENTIFIER");
//			constraintProperties.setTargetEntityKey("PARTICIPANT_ID");
//			((Association)association).setConstraintProperties(constraintProperties);
//		}
//		else if (sourceEntityName.equals(SPECIMEN_COLLECTION_GROUP_NAME) && targetEntityName.equals(SPECIMEN_NAME) && sourceRoleName.equals("specimenCollectionGroup"))
//		{
//			association = factory.createAssociation();
//	
//			EntityInterface sourceEntity = getEntityByName(SPECIMEN_COLLECTION_GROUP_NAME);
//			EntityInterface targetEntity = getEntityByName(SPECIMEN_NAME);
//	
//			association.setEntity(sourceEntity);
//			association.setTargetEntity(targetEntity);
//			association.setAssociationDirection(Constants.AssociationDirection.BI_DIRECTIONAL);
//	
//			Role sourceRole = new Role();
//			sourceRole.setName("specimenCollectionGroup");
//			sourceRole.setId(1L);
//			//TODO check association Type for linking: sourceRole.setAssociationType("linking");
//			sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
//			sourceRole.setMaxCardinality(1);
//			sourceRole.setMinCardinality(1);
//			association.setSourceRole(sourceRole);
//	
//			Role targetRole = new Role();
//			targetRole.setName("specimenCollection");
//			targetRole.setId(2L);
//			//TODO check association Type for linking: targetRole.setAssociationType("linking");
//			targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
//	
//			targetRole.setMaxCardinality(10);
//			targetRole.setMinCardinality(1);
//			association.setTargetRole(targetRole);
//	
//			
//			ConstraintProperties constraintProperties = new ConstraintProperties();
//			constraintProperties.setSourceEntityKey("IDENTIFIER");
//			constraintProperties.setTargetEntityKey("SPECIMEN_COLLECTION_GROUP_ID");
//			((Association)association).setConstraintProperties(constraintProperties);
//			
//		}
//		else if (sourceEntityName.equals(SPECIMEN_NAME) && targetEntityName.equals(SPECIMEN_NAME) && sourceRoleName.equals("childrenSpecimen"))
//		{
//			association = factory.createAssociation();
//	
//			EntityInterface sourceEntity = getEntityByName(SPECIMEN_NAME);
//			EntityInterface targetEntity = getEntityByName(SPECIMEN_NAME);
//	
//			association.setEntity(sourceEntity);
//			association.setTargetEntity(targetEntity);
//			association.setAssociationDirection(Constants.AssociationDirection.BI_DIRECTIONAL);
//	
//			Role sourceRole = new Role();
//			sourceRole.setName("childrenSpecimen");
//			sourceRole.setId(1L);
//			//TODO check association Type for linking: sourceRole.setAssociationType("linking");
//			sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
//			sourceRole.setMaxCardinality(10);
//			sourceRole.setMinCardinality(0);
//			association.setSourceRole(sourceRole);
//	
//			Role targetRole = new Role();
//			targetRole.setName("parentSpecimen");
//			targetRole.setId(2L);
//			//TODO check association Type for linking: targetRole.setAssociationType("linking");
//			targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
//	
//			targetRole.setMaxCardinality(1);
//			targetRole.setMinCardinality(0);
//			association.setTargetRole(targetRole);
//	
//			ConstraintProperties constraintProperties = new ConstraintProperties();
//			constraintProperties.setSourceEntityKey("IDENTIFIER");
//			constraintProperties.setTargetEntityKey("PARENT_SPECIMEN_ID");
//			((Association)association).setConstraintProperties(constraintProperties);
//			
//		}
//		else if (sourceEntityName.equals(COLLECTION_PROTOCOL_REGISTRATION_NAME) && targetEntityName.equals(SPECIMEN_COLLECTION_GROUP_NAME)  && sourceRoleName.equals("collectionProtocolRegistration"))
//		{
//			association = factory.createAssociation();
//	
//			EntityInterface sourceEntity = getEntityByName(COLLECTION_PROTOCOL_REGISTRATION_NAME);
//			EntityInterface targetEntity = getEntityByName(SPECIMEN_COLLECTION_GROUP_NAME);
//	
//			association.setEntity(sourceEntity);
//			association.setTargetEntity(targetEntity);
//			association.setAssociationDirection(Constants.AssociationDirection.BI_DIRECTIONAL);
//	
//			Role sourceRole = new Role();
//			sourceRole.setName("collectionProtocolRegistration");
//			sourceRole.setId(1L);
//			//TODO check association Type for linking: sourceRole.setAssociationType("linking");
//			sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
//			sourceRole.setMaxCardinality(1);
//			sourceRole.setMinCardinality(1);
//			association.setSourceRole(sourceRole);
//	
//			Role targetRole = new Role();
//			targetRole.setName("SpecimenCollectionGroupCollection");
//			targetRole.setId(2L);
//			//TODO check association Type for linking: targetRole.setAssociationType("linking");
//			targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
//	
//			targetRole.setMaxCardinality(10);
//			targetRole.setMinCardinality(0);
//			association.setTargetRole(targetRole);
//	
//			ConstraintProperties constraintProperties = new ConstraintProperties();
//			constraintProperties.setSourceEntityKey("IDENTIFIER");
//			constraintProperties.setTargetEntityKey("COLLECTION_PROTOCOL_REG_ID");
//			((Association)association).setConstraintProperties(constraintProperties);
//		}
//		else if (sourceEntityName.equals(SPECIMEN_COLLECTION_GROUP_NAME) && targetEntityName.equals(COLLECTION_PROTOCOL_EVT_NAME) && sourceRoleName.equals("specimenCollectionGroup"))
//		{
//			association = factory.createAssociation();
//	
//			EntityInterface sourceEntity = getEntityByName(SPECIMEN_COLLECTION_GROUP_NAME);
//			EntityInterface targetEntity = getEntityByName(COLLECTION_PROTOCOL_EVT_NAME);
//	
//			association.setEntity(sourceEntity);
//			association.setTargetEntity(targetEntity);
//			association.setAssociationDirection(Constants.AssociationDirection.SRC_DESTINATION);
//	//		association.setDirection("Source -> Destination");
//	
//			Role sourceRole = new Role();
//			sourceRole.setName("specimenCollectionGroup");
//			sourceRole.setId(1L);
//			//TODO check association Type for linking: sourceRole.setAssociationType("linking");
//			sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
//			sourceRole.setMaxCardinality(10);
//			sourceRole.setMinCardinality(0);
//			association.setSourceRole(sourceRole);
//	
//			Role targetRole = new Role();
//			targetRole.setName("collectionProtocolEvent");
//			targetRole.setId(2L);
//			//TODO check association Type for linking: targetRole.setAssociationType("linking");
//			targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
//	
//			targetRole.setMaxCardinality(1);
//			targetRole.setMinCardinality(1);
//			association.setTargetRole(targetRole);
//	
//			ConstraintProperties constraintProperties = new ConstraintProperties();
//			constraintProperties.setSourceEntityKey("COLLECTION_PROTOCOL_REG_ID");
//			constraintProperties.setTargetEntityKey("IDENTIFIER");
//			((Association)association).setConstraintProperties(constraintProperties);
//
//		}
//		else if (sourceEntityName.equals(COLLECTION_PROTOCOL_NAME) && targetEntityName.equals(COLLECTION_PROTOCOL_EVT_NAME)  && sourceRoleName.equals("collectionProtocol"))
//		{
//			association = factory.createAssociation();
//	
//			EntityInterface sourceEntity = getEntityByName(COLLECTION_PROTOCOL_NAME);
//			EntityInterface targetEntity = getEntityByName(COLLECTION_PROTOCOL_EVT_NAME);
//	
//			association.setEntity(sourceEntity);
//			association.setTargetEntity(targetEntity);
//			association.setAssociationDirection(Constants.AssociationDirection.BI_DIRECTIONAL);
//	
//			Role sourceRole = new Role();
//			sourceRole.setName("collectionProtocol");
//			sourceRole.setId(1L);
//			//TODO check association Type for linking: sourceRole.setAssociationType("linking");
//			sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
//			sourceRole.setMaxCardinality(1);
//			sourceRole.setMinCardinality(1);
//			association.setSourceRole(sourceRole);
//	
//			Role targetRole = new Role();
//			targetRole.setName("collectionProtocolEventCollection");
//			targetRole.setId(2L);
//			//TODO check association Type for linking: targetRole.setAssociationType("linking");
//			targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
//	
//			targetRole.setMaxCardinality(10);
//			targetRole.setMinCardinality(1);
//			association.setTargetRole(targetRole);
//	
//			ConstraintProperties constraintProperties = new ConstraintProperties();
//			constraintProperties.setSourceEntityKey("IDENTIFIER");
//			constraintProperties.setTargetEntityKey("`COLLECTION_PROTOCOL_ID`");
//			((Association)association).setConstraintProperties(constraintProperties);
//		}
//		else if (sourceEntityName.equals(COLLECTION_PROTOCOL_REGISTRATION_NAME) && targetEntityName.equals(COLLECTION_PROTOCOL_NAME)  && sourceRoleName.equals(""))
//		{
//			association = factory.createAssociation();
//	
//			EntityInterface sourceEntity = getEntityByName(COLLECTION_PROTOCOL_REGISTRATION_NAME);
//			EntityInterface targetEntity = getEntityByName(COLLECTION_PROTOCOL_NAME);
//	
//			association.setEntity(sourceEntity);
//			association.setTargetEntity(targetEntity);
//			association.setAssociationDirection(Constants.AssociationDirection.SRC_DESTINATION);
//	
//			Role sourceRole = new Role();
//			sourceRole.setName("");
//			sourceRole.setId(1L);
//			//TODO check association Type for linking: sourceRole.setAssociationType("linking");
//			sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
//			sourceRole.setMaxCardinality(10);
//			sourceRole.setMinCardinality(0);
//			association.setSourceRole(sourceRole);
//	
//			Role targetRole = new Role();
//			targetRole.setName("collectionProtocol");
//			targetRole.setId(2L);
//			//TODO check association Type for linking: targetRole.setAssociationType("linking");
//			targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
//	
//			targetRole.setMaxCardinality(1);
//			targetRole.setMinCardinality(1);
//			association.setTargetRole(targetRole);
//	
//			ConstraintProperties constraintProperties = new ConstraintProperties();
//			constraintProperties.setSourceEntityKey("COLLECTION_PROTOCOL_ID");
//			constraintProperties.setTargetEntityKey("IDENTIFIER");
//			((Association)association).setConstraintProperties(constraintProperties);
//		}
//		else if (sourceEntityName.equals(SPECIMEN_COLLECTION_GROUP_NAME) && targetEntityName.equals(SPECIMEN_NAME) && sourceRoleName.equals("specimenCollectionGroup"))
//		{
//			association = factory.createAssociation();
//	
//			EntityInterface sourceEntity = getEntityByName(SPECIMEN_COLLECTION_GROUP_NAME);
//			EntityInterface targetEntity = getEntityByName(SPECIMEN_NAME);
//	
//			association.setEntity(sourceEntity);
//			association.setTargetEntity(targetEntity);
//			association.setAssociationDirection(Constants.AssociationDirection.BI_DIRECTIONAL);
//	
//			Role sourceRole = new Role();
//			sourceRole.setName("specimenCollectionGroup");
//			sourceRole.setId(1L);
//			//TODO check association Type for linking: sourceRole.setAssociationType("linking");
//			sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
//			sourceRole.setMaxCardinality(1);
//			sourceRole.setMinCardinality(1);
//			association.setSourceRole(sourceRole);
//	
//			Role targetRole = new Role();
//			targetRole.setName("specimenCollection");
//			targetRole.setId(2L);
//			//TODO check association Type for linking: targetRole.setAssociationType("linking");
//			targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
//	
//			targetRole.setMaxCardinality(10);
//			targetRole.setMinCardinality(1);
//			association.setTargetRole(targetRole);
//	
//			ConstraintProperties constraintProperties = new ConstraintProperties();
//			constraintProperties.setSourceEntityKey("IDENTIFIER");
//			constraintProperties.setTargetEntityKey("SPECIMEN_COLLECTION_GROUP_ID");
//			((Association)association).setConstraintProperties(constraintProperties);
//			
//		}
//		if (sourceEntityName.equals(SPECIMEN_COLLECTION_GROUP_NAME) && targetEntityName.equals(SITE_NAME) && sourceRoleName.equals("specimenCollectionGroup"))
//		{
//			association = factory.createAssociation();
//	
//			EntityInterface sourceEntity = getEntityByName(SPECIMEN_COLLECTION_GROUP_NAME);
//			EntityInterface targetEntity = getEntityByName(SITE_NAME);
//	
//			association.setEntity(sourceEntity);
//			association.setTargetEntity(targetEntity);
//			association.setAssociationDirection(Constants.AssociationDirection.BI_DIRECTIONAL);
//	
//			Role sourceRole = new Role();
//			sourceRole.setName("specimenCollectionGroup");
//			sourceRole.setId(1L);
//			sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
//			sourceRole.setMaxCardinality(1);
//			sourceRole.setMinCardinality(1);
//			association.setSourceRole(sourceRole);
//	
//			Role targetRole = new Role();
//			targetRole.setName("site");
//			targetRole.setId(2L);
//			targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
//			targetRole.setMaxCardinality(10);
//			targetRole.setMinCardinality(0);
//			association.setTargetRole(targetRole);
//			
//			ConstraintProperties constraintProperties = new ConstraintProperties();
//			constraintProperties.setSourceEntityKey("SITE_ID");
//			constraintProperties.setTargetEntityKey("IDENTIFIER");
//			((Association)association).setConstraintProperties(constraintProperties);
//			
//		}
//		return association;
//	}
	/**
	 * @see edu.common.dynamicextensions.entitymanager.EntityManager#getAssociation(java.lang.String, java.lang.String)
	 */
	@Override
	public Collection<AssociationInterface> getAssociation(String sourceEntityName, String sourceRoleName) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		Collection<AssociationInterface> associations = new ArrayList<AssociationInterface>();
		if (sourceEntityName.equals(PARTICIPANT_NAME) && sourceRoleName.equals("participant"))
		{
			AssociationInterface association = factory.createAssociation();
	
			EntityInterface sourceEntity = getEntityByName(PARTICIPANT_NAME);
			EntityInterface targetEntity = getEntityByName(PARTICIPANT_MEDICAL_ID_NAME);
	
			association.setEntity(sourceEntity);
			association.setTargetEntity(targetEntity);
			association.setAssociationDirection(Constants.AssociationDirection.BI_DIRECTIONAL);
	
			Role sourceRole = new Role();
			sourceRole.setName("participant");
			sourceRole.setId(1L);
			sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
			sourceRole.setMaxCardinality(1);
			sourceRole.setMinCardinality(1);
			association.setSourceRole(sourceRole);
	
			Role targetRole = new Role();
			targetRole.setName("participantMedicalIdentifierCollection");
			targetRole.setId(2L);
			targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
			targetRole.setMaxCardinality(10);
			targetRole.setMinCardinality(0);
			association.setTargetRole(targetRole);
	
			
			ConstraintProperties constraintProperties = new ConstraintProperties();
			constraintProperties.setSourceEntityKey("IDENTIFIER");
			constraintProperties.setTargetEntityKey("PARTICIPANT_ID");
			((Association)association).setConstraintProperties(constraintProperties);
			
			associations.add(association);
			
			// another Association.
			association = factory.createAssociation();
			sourceEntity = getEntityByName(PARTICIPANT_NAME);
			targetEntity = getEntityByName(COLLECTION_PROTOCOL_REGISTRATION_NAME);
	
			association.setEntity(sourceEntity);
			association.setTargetEntity(targetEntity);
			association.setAssociationDirection(Constants.AssociationDirection.BI_DIRECTIONAL);
	
			sourceRole = new Role();
			sourceRole.setName("participant");
			sourceRole.setId(1L);
			//TODO check association Type for linking: sourceRole.setAssociationType("linking");
			sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
			sourceRole.setMaxCardinality(1);
			sourceRole.setMinCardinality(1);
			association.setSourceRole(sourceRole);
	
			targetRole = new Role();
			targetRole.setName("collectionProtocolRegistrationCollection");
			targetRole.setId(2L);
			//TODO check association Type for linking: targetRole.setAssociationsType("linking");
			targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
			targetRole.setMaxCardinality(10);
			targetRole.setMinCardinality(0);
			association.setTargetRole(targetRole);
	
			constraintProperties = new ConstraintProperties();
			constraintProperties.setSourceEntityKey("IDENTIFIER");
			constraintProperties.setTargetEntityKey("PARTICIPANT_ID");
			((Association)association).setConstraintProperties(constraintProperties);
			associations.add(association);
		}
		else if (sourceEntityName.equals(SPECIMEN_COLLECTION_GROUP_NAME) && sourceRoleName.equals("specimenCollectionGroup"))
		{
			AssociationInterface association = factory.createAssociation();
	
			EntityInterface sourceEntity = getEntityByName(SPECIMEN_COLLECTION_GROUP_NAME);
			EntityInterface targetEntity = getEntityByName(SPECIMEN_NAME);
	
			association.setEntity(sourceEntity);
			association.setTargetEntity(targetEntity);
			association.setAssociationDirection(Constants.AssociationDirection.BI_DIRECTIONAL);
	
			Role sourceRole = new Role();
			sourceRole.setName("specimenCollectionGroup");
			sourceRole.setId(1L);
			//TODO check association Type for linking: sourceRole.setAssociationType("linking");
			sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
			sourceRole.setMaxCardinality(1);
			sourceRole.setMinCardinality(1);
			association.setSourceRole(sourceRole);
	
			Role targetRole = new Role();
			targetRole.setName("specimenCollection");
			targetRole.setId(2L);
			//TODO check association Type for linking: targetRole.setAssociationType("linking");
			targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
	
			targetRole.setMaxCardinality(10);
			targetRole.setMinCardinality(1);
			association.setTargetRole(targetRole);
	
			
			ConstraintProperties constraintProperties = new ConstraintProperties();
			constraintProperties.setSourceEntityKey("IDENTIFIER");
			constraintProperties.setTargetEntityKey("SPECIMEN_COLLECTION_GROUP_ID");
			((Association)association).setConstraintProperties(constraintProperties);
			
			associations.add(association);
			
			// Another Association 2
			association = factory.createAssociation();
			
			sourceEntity = getEntityByName(SPECIMEN_COLLECTION_GROUP_NAME);
			targetEntity = getEntityByName(COLLECTION_PROTOCOL_EVT_NAME);
	
			association.setEntity(sourceEntity);
			association.setTargetEntity(targetEntity);
			association.setAssociationDirection(Constants.AssociationDirection.SRC_DESTINATION);
	//		association.setDirection("Source -> Destination");
	
			sourceRole = new Role();
			sourceRole.setName("specimenCollectionGroup");
			sourceRole.setId(1L);
			//TODO check association Type for linking: sourceRole.setAssociationType("linking");
			sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
			sourceRole.setMaxCardinality(10);
			sourceRole.setMinCardinality(0);
			association.setSourceRole(sourceRole);
	
			targetRole = new Role();
			targetRole.setName("collectionProtocolEvent");
			targetRole.setId(2L);
			//TODO check association Type for linking: targetRole.setAssociationType("linking");
			targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
	
			targetRole.setMaxCardinality(1);
			targetRole.setMinCardinality(1);
			association.setTargetRole(targetRole);
	
			constraintProperties = new ConstraintProperties();
			constraintProperties.setSourceEntityKey("COLLECTION_PROTOCOL_REG_ID");
			constraintProperties.setTargetEntityKey("IDENTIFIER");
			((Association)association).setConstraintProperties(constraintProperties);

			associations.add(association);
			
			// Another Association 3
			
			association = factory.createAssociation();
			
			sourceEntity = getEntityByName(SPECIMEN_COLLECTION_GROUP_NAME);
			targetEntity = getEntityByName(SITE_NAME);
	
			association.setEntity(sourceEntity);
			association.setTargetEntity(targetEntity);
			association.setAssociationDirection(Constants.AssociationDirection.BI_DIRECTIONAL);
	
			sourceRole = new Role();
			sourceRole.setName("specimenCollectionGroup");
			sourceRole.setId(1L);
			sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
			sourceRole.setMaxCardinality(1);
			sourceRole.setMinCardinality(1);
			association.setSourceRole(sourceRole);
	
			targetRole = new Role();
			targetRole.setName("site");
			targetRole.setId(2L);
			targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
			targetRole.setMaxCardinality(10);
			targetRole.setMinCardinality(0);
			association.setTargetRole(targetRole);
	
			
			constraintProperties = new ConstraintProperties();
			constraintProperties.setSourceEntityKey("SITE_ID");
			constraintProperties.setTargetEntityKey("IDENTIFIER");
			((Association)association).setConstraintProperties(constraintProperties);
			
			associations.add(association);

		}
		else if (sourceEntityName.equals(SPECIMEN_NAME) && sourceRoleName.equals("childrenSpecimen"))
		{
			AssociationInterface association = factory.createAssociation();
	
			EntityInterface sourceEntity = getEntityByName(SPECIMEN_NAME);
			EntityInterface targetEntity = getEntityByName(SPECIMEN_NAME);
	
			association.setEntity(sourceEntity);
			association.setTargetEntity(targetEntity);
			association.setAssociationDirection(Constants.AssociationDirection.BI_DIRECTIONAL);
	
			Role sourceRole = new Role();
			sourceRole.setName("childrenSpecimen");
			sourceRole.setId(1L);
			//TODO check association Type for linking: sourceRole.setAssociationType("linking");
			sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
			sourceRole.setMaxCardinality(10);
			sourceRole.setMinCardinality(0);
			association.setSourceRole(sourceRole);
	
			Role targetRole = new Role();
			targetRole.setName("parentSpecimen");
			targetRole.setId(2L);
			//TODO check association Type for linking: targetRole.setAssociationType("linking");
			targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
	
			targetRole.setMaxCardinality(1);
			targetRole.setMinCardinality(0);
			association.setTargetRole(targetRole);
	
			ConstraintProperties constraintProperties = new ConstraintProperties();
			constraintProperties.setSourceEntityKey("IDENTIFIER");
			constraintProperties.setTargetEntityKey("PARENT_SPECIMEN_ID");
			((Association)association).setConstraintProperties(constraintProperties);
			
			associations.add(association);
		}
		else if (sourceEntityName.equals(SPECIMEN_NAME) && sourceRoleName.equals(""))
		{
			AssociationInterface association = factory.createAssociation();
	
			EntityInterface sourceEntity = getEntityByName(SPECIMEN_NAME);
			EntityInterface targetEntity = getEntityByName(SPECIMEN_CHARACTERISTIC_NAME);
	
			association.setEntity(sourceEntity);
			association.setTargetEntity(targetEntity);
			association.setAssociationDirection(Constants.AssociationDirection.SRC_DESTINATION);
	
			Role sourceRole = new Role();
			sourceRole.setName("");
			sourceRole.setId(1L);
			//TODO check association Type for linking: sourceRole.setAssociationType("linking");
			sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
			sourceRole.setMaxCardinality(10);
			sourceRole.setMinCardinality(0);
			association.setSourceRole(sourceRole);
	
			Role targetRole = new Role();
			targetRole.setName("specimenCharacteristics");
			targetRole.setId(2L);
			//TODO check association Type for linking: targetRole.setAssociationType("linking");
			targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
	
			targetRole.setMaxCardinality(1);
			targetRole.setMinCardinality(1);
			association.setTargetRole(targetRole);
	
			ConstraintProperties constraintProperties = new ConstraintProperties();
			constraintProperties.setSourceEntityKey("SPECIMEN_CHARACTERISTICS_ID");
			constraintProperties.setTargetEntityKey("IDENTIFIER");
			((Association)association).setConstraintProperties(constraintProperties);
			
			associations.add(association);
		}
		else if (sourceEntityName.equals(COLLECTION_PROTOCOL_REGISTRATION_NAME) && sourceRoleName.equals("collectionProtocolRegistration"))
		{
			AssociationInterface association = factory.createAssociation();
	
			EntityInterface sourceEntity = getEntityByName(COLLECTION_PROTOCOL_REGISTRATION_NAME);
			EntityInterface targetEntity = getEntityByName(SPECIMEN_COLLECTION_GROUP_NAME);
	
			association.setEntity(sourceEntity);
			association.setTargetEntity(targetEntity);
			association.setAssociationDirection(Constants.AssociationDirection.BI_DIRECTIONAL);
	
			Role sourceRole = new Role();
			sourceRole.setName("collectionProtocolRegistration");
			sourceRole.setId(1L);
			//TODO check association Type for linking: sourceRole.setAssociationType("linking");
			sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
			sourceRole.setMaxCardinality(1);
			sourceRole.setMinCardinality(1);
			association.setSourceRole(sourceRole);
	
			Role targetRole = new Role();
			targetRole.setName("SpecimenCollectionGroupCollection");
			targetRole.setId(2L);
			//TODO check association Type for linking: targetRole.setAssociationType("linking");
			targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
	
			targetRole.setMaxCardinality(10);
			targetRole.setMinCardinality(0);
			association.setTargetRole(targetRole);
	
			ConstraintProperties constraintProperties = new ConstraintProperties();
			constraintProperties.setSourceEntityKey("IDENTIFIER");
			constraintProperties.setTargetEntityKey("COLLECTION_PROTOCOL_REG_ID");
			((Association)association).setConstraintProperties(constraintProperties);

			associations.add(association);
			
			// another Association.
			association = factory.createAssociation();
			sourceEntity = getEntityByName(COLLECTION_PROTOCOL_REGISTRATION_NAME);
			targetEntity = getEntityByName(COLLECTION_PROTOCOL_NAME);
	
			association.setEntity(sourceEntity);
			association.setTargetEntity(targetEntity);
			association.setAssociationDirection(Constants.AssociationDirection.SRC_DESTINATION);
	
			sourceRole = new Role();
			sourceRole.setName("");
			sourceRole.setId(1L);
			//TODO check association Type for linking: sourceRole.setAssociationType("linking");
			sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
			sourceRole.setMaxCardinality(10);
			sourceRole.setMinCardinality(0);
			association.setSourceRole(sourceRole);
	
			targetRole = new Role();
			targetRole.setName("collectionProtocol");
			targetRole.setId(2L);
			//TODO check association Type for linking: targetRole.setAssociationType("linking");
			targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
	
			targetRole.setMaxCardinality(1);
			targetRole.setMinCardinality(1);
			association.setTargetRole(targetRole);
	
			constraintProperties = new ConstraintProperties();
			constraintProperties.setSourceEntityKey("COLLECTION_PROTOCOL_ID");
			constraintProperties.setTargetEntityKey("IDENTIFIER");
			((Association)association).setConstraintProperties(constraintProperties);

			associations.add(association);
		}
		if (sourceEntityName.equals(COLLECTION_PROTOCOL_NAME) && sourceRoleName.equals("collectionProtocol"))
		{
			AssociationInterface association = factory.createAssociation();
	
			EntityInterface sourceEntity = getEntityByName(COLLECTION_PROTOCOL_NAME);
			EntityInterface targetEntity = getEntityByName(COLLECTION_PROTOCOL_EVT_NAME);
	
			association.setEntity(sourceEntity);
			association.setTargetEntity(targetEntity);
			association.setAssociationDirection(Constants.AssociationDirection.BI_DIRECTIONAL);
	
			Role sourceRole = new Role();
			sourceRole.setName("collectionProtocol");
			sourceRole.setId(1L);
			//TODO check association Type for linking: sourceRole.setAssociationType("linking");
			sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
			sourceRole.setMaxCardinality(1);
			sourceRole.setMinCardinality(1);
			association.setSourceRole(sourceRole);
	
			Role targetRole = new Role();
			targetRole.setName("collectionProtocolEventCollection");
			targetRole.setId(2L);
			//TODO check association Type for linking: targetRole.setAssociationType("linking");
			targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
	
			targetRole.setMaxCardinality(10);
			targetRole.setMinCardinality(1);
			association.setTargetRole(targetRole);
	
			ConstraintProperties constraintProperties = new ConstraintProperties();
			constraintProperties.setSourceEntityKey("IDENTIFIER");
			constraintProperties.setTargetEntityKey("`COLLECTION_PROTOCOL_ID`");
			((Association)association).setConstraintProperties(constraintProperties);

			associations.add(association);
		}
		return associations;
	}

	/**
	 * @see edu.common.dynamicextensions.entitymanager.EntityManager#getAssociations(java.lang.Long, java.lang.Long)
	 */
	@Override
	public Collection<AssociationInterface> getAssociations(Long sourceEntityId, Long targetEntityId) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		Collection<AssociationInterface> associationsCollection = new ArrayList<AssociationInterface>();

		if (sourceEntityId.equals(PARTICIPANT_ID) && targetEntityId.equals(PARTICIPANT_MEDICAL_ID))
		{
			AssociationInterface currentAssociation = factory.createAssociation();
	
			EntityInterface sourceEntity = getEntityByName(PARTICIPANT_NAME);
			EntityInterface targetEntity = getEntityByName(PARTICIPANT_MEDICAL_ID_NAME);
	
			currentAssociation.setEntity(sourceEntity);
			currentAssociation.setTargetEntity(targetEntity);
			currentAssociation.setAssociationDirection(Constants.AssociationDirection.BI_DIRECTIONAL);
	
			Role sourceRole = new Role();
			sourceRole.setName("participant");
			sourceRole.setId(1L);
			sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
			sourceRole.setMaxCardinality(1);
			sourceRole.setMinCardinality(1);
			currentAssociation.setSourceRole(sourceRole);
	
			Role targetRole = new Role();
			targetRole.setName("participantMedicalIdentifierCollection");
			targetRole.setId(2L);
			targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
			targetRole.setMaxCardinality(10);
			targetRole.setMinCardinality(0);
			currentAssociation.setTargetRole(targetRole);
	
			
			ConstraintProperties constraintProperties = new ConstraintProperties();
			constraintProperties.setSourceEntityKey("IDENTIFIER");
			constraintProperties.setTargetEntityKey("PARTICIPANT_ID");
			((Association)currentAssociation).setConstraintProperties(constraintProperties);
			
			associationsCollection.add(currentAssociation);
			return associationsCollection;
		}
		else if (sourceEntityId.equals(PARTICIPANT_ID) && targetEntityId.equals(COLLECTION_PROTOCOL_REGISTRATION_ID))
		{
			AssociationInterface currentAssociation = factory.createAssociation();
	
			EntityInterface sourceEntity = getEntityByName(PARTICIPANT_NAME);
			EntityInterface targetEntity = getEntityByName(COLLECTION_PROTOCOL_REGISTRATION_NAME);
	
			currentAssociation.setEntity(sourceEntity);
			currentAssociation.setTargetEntity(targetEntity);
			currentAssociation.setAssociationDirection(Constants.AssociationDirection.BI_DIRECTIONAL);
	
			Role sourceRole = new Role();
			sourceRole.setName("participant");
			sourceRole.setId(1L);
			//TODO check association Type for linking: sourceRole.setAssociationType("linking");
			sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
			sourceRole.setMaxCardinality(1);
			sourceRole.setMinCardinality(1);
			currentAssociation.setSourceRole(sourceRole);
	
			Role targetRole = new Role();
			targetRole.setName("collectionProtocolRegistrationCollection");
			targetRole.setId(2L);
			//TODO check association Type for linking: targetRole.setAssociationsType("linking");
			targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
			targetRole.setMaxCardinality(10);
			targetRole.setMinCardinality(0);
			currentAssociation.setTargetRole(targetRole);
	
			associationsCollection.add(currentAssociation);
			return associationsCollection;
		}
		else if (sourceEntityId.equals(COLLECTION_PROTOCOL_REGISTRATION_ID) && targetEntityId.equals(COLLECTION_PROTOCOL_ID))
		{
			AssociationInterface currentAssociation = factory.createAssociation();
	
			EntityInterface sourceEntity = getEntityByName(COLLECTION_PROTOCOL_REGISTRATION_NAME);
			EntityInterface targetEntity = getEntityByName(COLLECTION_PROTOCOL_NAME);
	
			currentAssociation.setEntity(sourceEntity);
			currentAssociation.setTargetEntity(targetEntity);
			// TODO check Direction for: currentAssociation.setDirection("Destination -> Source");
			currentAssociation.setAssociationDirection(Constants.AssociationDirection.SRC_DESTINATION);
	
			Role sourceRole = new Role();
			sourceRole.setName("collectionProtocol");
			sourceRole.setId(1L);
			//TODO check association Type for linking: sourceRole.setAssociationType("linking");
			sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
			sourceRole.setMaxCardinality(1);
			sourceRole.setMinCardinality(1);
			currentAssociation.setSourceRole(sourceRole);
	
			Role targetRole = new Role();
			targetRole.setName("collectionProtocolRegistrationCollection");
			targetRole.setId(2L);
			//TODO check association Type for linking: targetRole.setAssociationType("linking");
			targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
			targetRole.setMaxCardinality(10);
			targetRole.setMinCardinality(0);
			currentAssociation.setTargetRole(targetRole);
	
			associationsCollection.add(currentAssociation);
			return associationsCollection;
		}
		else if (sourceEntityId.equals(COLLECTION_PROTOCOL_REGISTRATION_ID) && targetEntityId.equals(SPECIMEN_COLLECTION_GROUP_ID))
		{
			AssociationInterface currentAssociation = factory.createAssociation();
	
			EntityInterface sourceEntity = getEntityByName(COLLECTION_PROTOCOL_REGISTRATION_NAME);
			EntityInterface targetEntity = getEntityByName(SPECIMEN_COLLECTION_GROUP_NAME);
	
			currentAssociation.setEntity(sourceEntity);
			currentAssociation.setTargetEntity(targetEntity);
			currentAssociation.setAssociationDirection(Constants.AssociationDirection.BI_DIRECTIONAL);
	
			Role sourceRole = new Role();
			sourceRole.setName("collectionProtocolRegistration");
			sourceRole.setId(1L);
			//TODO check association Type for linking: sourceRole.setAssociationType("linking");
			sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
			sourceRole.setMaxCardinality(1);
			sourceRole.setMinCardinality(1);
			currentAssociation.setSourceRole(sourceRole);
	
			Role targetRole = new Role();
			targetRole.setName("SpecimenCollectionGroupCollection");
			targetRole.setId(2L);
			//TODO check association Type for linking: targetRole.setAssociationType("linking");
			targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
	
			targetRole.setMaxCardinality(10);
			targetRole.setMinCardinality(0);
			currentAssociation.setTargetRole(targetRole);
	
			associationsCollection.add(currentAssociation);
			return associationsCollection;
		}
		else if (sourceEntityId.equals(SPECIMEN_COLLECTION_GROUP_ID) && targetEntityId.equals(COLLECTION_PROTOCOL_EVT_ID))
		{
			AssociationInterface currentAssociation = factory.createAssociation();
	
			EntityInterface sourceEntity = getEntityByName(SPECIMEN_COLLECTION_GROUP_NAME);
			EntityInterface targetEntity = getEntityByName(COLLECTION_PROTOCOL_EVT_NAME);
	
			currentAssociation.setEntity(sourceEntity);
			currentAssociation.setTargetEntity(targetEntity);
			currentAssociation.setAssociationDirection(Constants.AssociationDirection.SRC_DESTINATION);
	//		currentAssociation.setDirection("Source -> Destination");
	
			Role sourceRole = new Role();
			sourceRole.setName("specimenCollectionGroup");
			sourceRole.setId(1L);
			//TODO check association Type for linking: sourceRole.setAssociationType("linking");
			sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
			sourceRole.setMaxCardinality(10);
			sourceRole.setMinCardinality(0);
			currentAssociation.setSourceRole(sourceRole);
	
			Role targetRole = new Role();
			targetRole.setName("collectionProtocolEvent");
			targetRole.setId(2L);
			//TODO check association Type for linking: targetRole.setAssociationType("linking");
			targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
	
			targetRole.setMaxCardinality(1);
			targetRole.setMinCardinality(1);
			currentAssociation.setTargetRole(targetRole);
	
			associationsCollection.add(currentAssociation);
			return associationsCollection;
		}
		else if (sourceEntityId.equals(COLLECTION_PROTOCOL_ID) && targetEntityId.equals(COLLECTION_PROTOCOL_EVT_ID))
		{
			AssociationInterface currentAssociation = factory.createAssociation();
	
			EntityInterface sourceEntity = getEntityByName(COLLECTION_PROTOCOL_NAME);
			EntityInterface targetEntity = getEntityByName(COLLECTION_PROTOCOL_EVT_NAME);
	
			currentAssociation.setEntity(sourceEntity);
			currentAssociation.setTargetEntity(targetEntity);
			currentAssociation.setAssociationDirection(Constants.AssociationDirection.BI_DIRECTIONAL);
	
			Role sourceRole = new Role();
			sourceRole.setName("collectionProtocol");
			sourceRole.setId(1L);
			//TODO check association Type for linking: sourceRole.setAssociationType("linking");
			sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
			sourceRole.setMaxCardinality(1);
			sourceRole.setMinCardinality(1);
			currentAssociation.setSourceRole(sourceRole);
	
			Role targetRole = new Role();
			targetRole.setName("collectionProtocolEventCollection");
			targetRole.setId(2L);
			//TODO check association Type for linking: targetRole.setAssociationType("linking");
			targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
	
			targetRole.setMaxCardinality(10);
			targetRole.setMinCardinality(1);
			currentAssociation.setTargetRole(targetRole);
	
			associationsCollection.add(currentAssociation);
			return associationsCollection;
		}
		else if (sourceEntityId.equals(SPECIMEN_COLLECTION_GROUP_ID) && targetEntityId.equals(SPECIMEN_ID))
		{
			AssociationInterface currentAssociation = factory.createAssociation();
	
			EntityInterface sourceEntity = getEntityByName(SPECIMEN_COLLECTION_GROUP_NAME);
			EntityInterface targetEntity = getEntityByName(SPECIMEN_NAME);
	
			currentAssociation.setEntity(sourceEntity);
			currentAssociation.setTargetEntity(targetEntity);
			currentAssociation.setAssociationDirection(Constants.AssociationDirection.BI_DIRECTIONAL);
	
			Role sourceRole = new Role();
			sourceRole.setName("specimenCollectionGroup");
			sourceRole.setId(1L);
			//TODO check association Type for linking: sourceRole.setAssociationType("linking");
			sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
			sourceRole.setMaxCardinality(1);
			sourceRole.setMinCardinality(1);
			currentAssociation.setSourceRole(sourceRole);
	
			Role targetRole = new Role();
			targetRole.setName("specimenCollection");
			targetRole.setId(2L);
			//TODO check association Type for linking: targetRole.setAssociationType("linking");
			targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
	
			targetRole.setMaxCardinality(10);
			targetRole.setMinCardinality(1);
			currentAssociation.setTargetRole(targetRole);
	
			associationsCollection.add(currentAssociation);
			
			ConstraintProperties constraintProperties = new ConstraintProperties();
			constraintProperties.setSourceEntityKey("IDENTIFIER");
			constraintProperties.setTargetEntityKey("SPECIMEN_COLLECTION_GROUP_ID");
			((Association)currentAssociation).setConstraintProperties(constraintProperties);
			
			return associationsCollection;
		}
		else if (sourceEntityId.equals(SPECIMEN_ID) && targetEntityId.equals(SPECIMEN_ID))
		{
			AssociationInterface currentAssociation = factory.createAssociation();
	
			EntityInterface sourceEntity = getEntityByName(SPECIMEN_NAME);
			EntityInterface targetEntity = getEntityByName(SPECIMEN_NAME);
	
			currentAssociation.setEntity(sourceEntity);
			currentAssociation.setTargetEntity(targetEntity);
			currentAssociation.setAssociationDirection(Constants.AssociationDirection.BI_DIRECTIONAL);
	
			Role sourceRole = new Role();
			sourceRole.setName("childrenSpecimen");
			sourceRole.setId(1L);
			//TODO check association Type for linking: sourceRole.setAssociationType("linking");
			sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
			sourceRole.setMaxCardinality(10);
			sourceRole.setMinCardinality(0);
			currentAssociation.setSourceRole(sourceRole);
	
			Role targetRole = new Role();
			targetRole.setName("parentSpecimen");
			targetRole.setId(2L);
			//TODO check association Type for linking: targetRole.setAssociationType("linking");
			targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
	
			targetRole.setMaxCardinality(1);
			targetRole.setMinCardinality(0);
			currentAssociation.setTargetRole(targetRole);
	
			associationsCollection.add(currentAssociation);
			
			ConstraintProperties constraintProperties = new ConstraintProperties();
			constraintProperties.setSourceEntityKey("IDENTIFIER");
			constraintProperties.setTargetEntityKey("PARENT_SPECIMEN_ID");
			((Association)currentAssociation).setConstraintProperties(constraintProperties);
			
			return associationsCollection;
		}
		else if (sourceEntityId.equals(SPECIMEN_COLLECTION_GROUP_ID) && targetEntityId.equals(SITE_ID))
		{
			AssociationInterface currentAssociation = factory.createAssociation();
	
			EntityInterface sourceEntity = getEntityByName(SPECIMEN_COLLECTION_GROUP_NAME);
			EntityInterface targetEntity = getEntityByName(SITE_NAME);
	
			currentAssociation.setEntity(sourceEntity);
			currentAssociation.setTargetEntity(targetEntity);
			currentAssociation.setAssociationDirection(Constants.AssociationDirection.BI_DIRECTIONAL);
	
			Role sourceRole = new Role();
			sourceRole.setName("specimenCollectionGroup");
			sourceRole.setId(1L);
			sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
			sourceRole.setMaxCardinality(1);
			sourceRole.setMinCardinality(1);
			currentAssociation.setSourceRole(sourceRole);
	
			Role targetRole = new Role();
			targetRole.setName("site");
			targetRole.setId(2L);
			targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
			targetRole.setMaxCardinality(10);
			targetRole.setMinCardinality(0);
			currentAssociation.setTargetRole(targetRole);
	
			
			ConstraintProperties constraintProperties = new ConstraintProperties();
			constraintProperties.setSourceEntityKey("SITE_ID");
			constraintProperties.setTargetEntityKey("IDENTIFIER");
			((Association)currentAssociation).setConstraintProperties(constraintProperties);
			
			associationsCollection.add(currentAssociation);
			return associationsCollection;
		}
		else if (sourceEntityId.equals(SPECIMEN_ID) && targetEntityId.equals(SPECIMEN_CHARACTERISTIC_ID))
		{
			AssociationInterface currentAssociation = factory.createAssociation();
			
			EntityInterface sourceEntity = getEntityByName(SPECIMEN_NAME);
			EntityInterface targetEntity = getEntityByName(SPECIMEN_CHARACTERISTIC_NAME);
	
			currentAssociation.setEntity(sourceEntity);
			currentAssociation.setTargetEntity(targetEntity);
			currentAssociation.setAssociationDirection(Constants.AssociationDirection.SRC_DESTINATION);
	
			Role sourceRole = new Role();
			sourceRole.setName("");
			sourceRole.setId(1L);
			//TODO check association Type for linking: sourceRole.setAssociationType("linking");
			sourceRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
			sourceRole.setMaxCardinality(10);
			sourceRole.setMinCardinality(0);
			currentAssociation.setSourceRole(sourceRole);
	
			Role targetRole = new Role();
			targetRole.setName("specimenCharacteristics");
			targetRole.setId(2L);
			//TODO check association Type for linking: targetRole.setAssociationType("linking");
			targetRole.setAssociationsType(Constants.AssociationType.CONTAINTMENT);
	
			targetRole.setMaxCardinality(1);
			targetRole.setMinCardinality(1);
			currentAssociation.setTargetRole(targetRole);
	
			ConstraintProperties constraintProperties = new ConstraintProperties();
			constraintProperties.setSourceEntityKey("SPECIMEN_CHARACTERISTICS_ID");
			constraintProperties.setTargetEntityKey("IDENTIFIER");
			((Association)currentAssociation).setConstraintProperties(constraintProperties);
			
			associationsCollection.add(currentAssociation);
			return associationsCollection;
			
		}
		else
		{
			System.out.println("There is no association between these two entities");
		}
		
		return associationsCollection;
	}

	/**
	 * @see edu.common.dynamicextensions.entitymanager.EntityManager#getAttribute(java.lang.String, java.lang.String)
	 */
	@Override
	public AttributeInterface getAttribute(String entityName, String attributeName) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		if (entityName.equalsIgnoreCase(PARTICIPANT_NAME))
		{
			ArrayList list = getParticipantAttributes();
			return getSpecificAttribute(list, attributeName);
		}
		else if (entityName.equalsIgnoreCase(PARTICIPANT_MEDICAL_ID_NAME))
		{
			ArrayList list = getParticipantMedicalIdentifierAttributes();
			return getSpecificAttribute(list, attributeName);
		}
		else if (entityName.equalsIgnoreCase(COLLECTION_PROTOCOL_REGISTRATION_NAME))
		{
			ArrayList list = getCollectionProtocolRegistrationAttributes();
			return getSpecificAttribute(list, attributeName);
		}
		else if (entityName.equalsIgnoreCase(COLLECTION_PROTOCOL_NAME))
		{
			ArrayList list = getCollectionProtocolAttributes();
			return getSpecificAttribute(list, attributeName);
		}
		else if (entityName.equalsIgnoreCase(SPECIMEN_PROTOCOL_NAME))
		{
			ArrayList list = getSpecimenProtocolAttributes();
			//System.out.println(list.get(3).getClass().getName());
			return getSpecificAttribute(list, attributeName);
		}
		else if (entityName.equalsIgnoreCase(COLLECTION_PROTOCOL_EVT_NAME))
		{
			ArrayList list = getCollectionProtocolEventAttributes();
			return getSpecificAttribute(list, attributeName);
		}
		else if (entityName.equalsIgnoreCase(SPECIMEN_COLLECTION_GROUP_NAME))
		{
			ArrayList list = getSpecimenCollectionGroupAttributes();
			return getSpecificAttribute(list, attributeName);
		}
		else if (entityName.equalsIgnoreCase(SPECIMEN_NAME))
		{
			ArrayList list = getSpecimenAttributes();
			return getSpecificAttribute(list, attributeName);
		}
		else if (entityName.equalsIgnoreCase(SPECIMEN_CHARACTERISTIC_NAME))
		{
			ArrayList list = getSpecimenCharacteristicAttributes();
			return getSpecificAttribute(list, attributeName);
		}
		if (entityName.equalsIgnoreCase(SITE_NAME))
		{
			ArrayList list = getSiteAttributes();
			return getSpecificAttribute(list, attributeName);
		}
		return null;
	}

	/**
	 * @see edu.common.dynamicextensions.entitymanager.EntityManager#getEntitiesByAttributeConceptCode(java.lang.String)
	 */
	@Override
	public Collection getEntitiesByAttributeConceptCode(String arg0) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		// TODO Auto-generated method stub
		return super.getEntitiesByAttributeConceptCode(arg0);
	}

	/**
	 * @see edu.common.dynamicextensions.entitymanager.EntityManager#getEntitiesByAttributeConceptName(java.lang.String)
	 */
	@Override
	public Collection getEntitiesByAttributeConceptName(String arg0) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		// TODO Auto-generated method stub
		return super.getEntitiesByAttributeConceptName(arg0);
	}

	/**
	 * @see edu.common.dynamicextensions.entitymanager.EntityManager#getEntitiesByAttributeDescription(java.lang.String)
	 */
	@Override
	public Collection getEntitiesByAttributeDescription(String arg0) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		// TODO Auto-generated method stub
		return super.getEntitiesByAttributeDescription(arg0);
	}

	/**
	 * @see edu.common.dynamicextensions.entitymanager.EntityManager#getEntitiesByAttributeName(java.lang.String)
	 */
	@Override
	public Collection getEntitiesByAttributeName(String arg0) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		// TODO Auto-generated method stub
		return super.getEntitiesByAttributeName(arg0);
	}

	/**
	 * @see edu.common.dynamicextensions.entitymanager.EntityManager#getEntitiesByConceptCode(java.lang.String)
	 */
	@Override
	public Collection getEntitiesByConceptCode(String arg0) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		// TODO Auto-generated method stub
		return super.getEntitiesByConceptCode(arg0);
	}

	/**
	 * @see edu.common.dynamicextensions.entitymanager.EntityManager#getEntitiesByConceptName(java.lang.String)
	 */
	@Override
	public Collection getEntitiesByConceptName(String arg0) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		// TODO Auto-generated method stub
		return super.getEntitiesByConceptName(arg0);
	}

	/**
	 * @see edu.common.dynamicextensions.entitymanager.EntityManager#getEntityByDescription(java.lang.String)
	 */
	@Override
	public Collection getEntityByDescription(String arg0) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		// TODO Auto-generated method stub
		return super.getEntityByDescription(arg0);
	}

	/**
	 * @see edu.common.dynamicextensions.entitymanager.EntityManager#getEntityByName(java.lang.String)
	 */
	@Override
	public EntityInterface getEntityByName(String name) throws DynamicExtensionsSystemException
	{
		if (name.equalsIgnoreCase(PARTICIPANT_NAME))
		{
			return createParticipantEntity(name);
		}
		else if (name.equalsIgnoreCase(PARTICIPANT_MEDICAL_ID_NAME))
		{
			return createParticipantMedicalIdentifierEntity(name);
		}
		else if (name.equalsIgnoreCase(COLLECTION_PROTOCOL_REGISTRATION_NAME))
		{
			return createCollectionProtocolRegistrationEntity(name);
		}
		else if (name.equalsIgnoreCase(COLLECTION_PROTOCOL_NAME))
		{
			return createCollectionProtocolEntity(name);
		}
		else if (name.equalsIgnoreCase(SPECIMEN_PROTOCOL_NAME))
		{
			return createSpecimenProtocolEntity(name);
		}
		else if (name.equalsIgnoreCase(COLLECTION_PROTOCOL_EVT_NAME))
		{
			return createCollectionProtocolEventEntity(name);
		}
		else if (name.equalsIgnoreCase(SPECIMEN_COLLECTION_GROUP_NAME))
		{
			return createSpecimenCollectionGroupEntity(name);
		}
		else if (name.equalsIgnoreCase(SPECIMEN_NAME))
		{
			return createSpecimenEntity(name);
		}
		else if (name.equalsIgnoreCase(SPECIMEN_CHARACTERISTIC_NAME))
		{
			return createSpecimenCharacteristicEntity(name);
		}
		else if (name.equalsIgnoreCase(SPECIMEN_EVT_NAME))
		{
			return createSpecimenEventParametersEntity(name);
		}
		else if (name.equalsIgnoreCase(CHKIN_CHKOUT_EVT_NAME))
		{
			return createCheckInCheckOutEventParameterEntity(name);
		}
		else if (name.equalsIgnoreCase(FROZEN_EVT_NAME))
		{
			return createFrozenEventParametersEntity(name);
		}
		else if (name.equalsIgnoreCase(PROCEDURE_EVT_NAME))
		{
			return createProcedureEventParametersEntity(name);
		}
		else if (name.equalsIgnoreCase(RECEIVED_EVT_NAME))
		{
			return createReceivedEventParametersEntity(name);
		}
		if (name.equalsIgnoreCase(SITE_NAME))
		{
			return createSiteEntity(name);
		}
		return null;
	}

	/*
	 * @param name
	 * Creates a Site entity, sets the attributes collection and
	 * table properties for the entity.
	 */
	private EntityInterface createSiteEntity(String name)
	{
		EntityInterface e = factory.createEntity();
		e.setName(SITE_NAME);
		e.setCreatedDate(new Date());
		e.setDescription("This is a Site entity");
		e.setId(SITE_ID);
		e.setLastUpdated(new Date());

		((Entity)e).setAbstractAttributeCollection(getSiteAttributes());

		TableProperties siteTableProperties = new TableProperties();
		siteTableProperties.setName("catissue_site");
		siteTableProperties.setId(SITE_ID);
		((Entity)e).setTableProperties(siteTableProperties);
		return e;
	}
	/*
	 * @param name
	 * Creates a participant entity, sets the attributes collection and
	 * table properties for the entity.
	 */
	private EntityInterface createParticipantEntity(String name)
	{
		EntityInterface e = factory.createEntity();
		e.setName(PARTICIPANT_NAME);
		e.setCreatedDate(new Date());
		e.setDescription("This is a participant entity");
		e.setId(PARTICIPANT_ID);
		e.setLastUpdated(new Date());

		((Entity)e).setAbstractAttributeCollection(getParticipantAttributes());

		TableProperties participantTableProperties = new TableProperties();
		participantTableProperties.setName("catissue_participant");
		participantTableProperties.setId(PARTICIPANT_ID);
		((Entity)e).setTableProperties(participantTableProperties);
		return e;
	}

	/*
	 * @param name
	 * Creates a participant medical identifier entity, sets attributes collection 
	 * and table properties for the entity.
	 */
	private EntityInterface createParticipantMedicalIdentifierEntity(String name)
	{
		EntityInterface e = factory.createEntity();
		e.setName(PARTICIPANT_MEDICAL_ID_NAME);
		e.setCreatedDate(new Date());
		e.setDescription("This is a participant medical identifier entity");
		e.setId(PARTICIPANT_MEDICAL_ID);
		e.setLastUpdated(new Date());

		((Entity)e).setAbstractAttributeCollection(getParticipantMedicalIdentifierAttributes());

		TableProperties participantMedicalIdentifierTableProperties = new TableProperties();
		participantMedicalIdentifierTableProperties.setName("catissue_part_medical_id");
		participantMedicalIdentifierTableProperties.setId(PARTICIPANT_MEDICAL_ID);
		((Entity)e).setTableProperties(participantMedicalIdentifierTableProperties);
		return e;
	}

	/*
	 * @param name
	 * Creates a collection protocol registration entity, sets attributes collection 
	 * and table properties for the entity.
	 */
	private EntityInterface createCollectionProtocolRegistrationEntity(String name)
	{
		EntityInterface e = factory.createEntity();
		e.setName(COLLECTION_PROTOCOL_REGISTRATION_NAME);
		e.setCreatedDate(new Date());
		e.setDescription("This is a collection protocol registration entity");
		e.setId(COLLECTION_PROTOCOL_REGISTRATION_ID);
		e.setLastUpdated(new Date());

		((Entity)e).setAbstractAttributeCollection(getCollectionProtocolRegistrationAttributes());

		TableProperties collectionProtocolRegistrationTableProperties = new TableProperties();
		collectionProtocolRegistrationTableProperties.setName("catissue_coll_prot_reg");
		collectionProtocolRegistrationTableProperties.setId(COLLECTION_PROTOCOL_REGISTRATION_ID);
		((Entity)e).setTableProperties(collectionProtocolRegistrationTableProperties);
		return e;
	}

	/*
	 * @param name
	 * Creates a collection protocol entity, sets attributes collection 
	 * and table properties for the entity.
	 */
	private EntityInterface createCollectionProtocolEntity(String name)
	{
		EntityInterface e = factory.createEntity();
		e.setName(COLLECTION_PROTOCOL_NAME);
		e.setCreatedDate(new Date());
		e.setDescription("This is a collection protocol entity");
		e.setId(COLLECTION_PROTOCOL_ID);
		e.setLastUpdated(new Date());

		((Entity)e).setAbstractAttributeCollection(getCollectionProtocolAttributes());

		TableProperties collectionProtocolTableProperties = new TableProperties();
		collectionProtocolTableProperties.setName("catissue_collection_protocol");
		collectionProtocolTableProperties.setId(COLLECTION_PROTOCOL_ID);
		((Entity)e).setTableProperties(collectionProtocolTableProperties);
		return e;
	}

	/*
	 * @param name
	 * Creates a specimen protocol entity, sets attributes collection 
	 * and table properties for the entity.
	 */
	private EntityInterface createSpecimenProtocolEntity(String name)
	{
		EntityInterface e = factory.createEntity();
		e.setName(SPECIMEN_PROTOCOL_NAME);
		e.setCreatedDate(new Date());
		e.setDescription("This is a specimen protocol entity");
		e.setId(SPECIMEN_PROTOCOL_ID);
		e.setLastUpdated(new Date());

		((Entity)e).setAbstractAttributeCollection(getSpecimenProtocolAttributes());

		TableProperties specimenProtocolTableProperties = new TableProperties();
		specimenProtocolTableProperties.setName("catissue_specimen_protocol");
		specimenProtocolTableProperties.setId(SPECIMEN_PROTOCOL_ID);
		((Entity)e).setTableProperties(specimenProtocolTableProperties);
		return e;
	}

	/*
	 * @param name
	 * Creates a collection protocol event entity, sets attributes collection 
	 * and table properties for the entity.
	 */
	private EntityInterface createCollectionProtocolEventEntity(String name)
	{
		EntityInterface e = factory.createEntity();
		e.setName(COLLECTION_PROTOCOL_EVT_NAME);
		e.setCreatedDate(new Date());
		e.setDescription("This is a collection protocol event entity");
		e.setId(COLLECTION_PROTOCOL_EVT_ID);
		e.setLastUpdated(new Date());

		((Entity)e).setAbstractAttributeCollection(getCollectionProtocolEventAttributes());

		TableProperties collectionProtocolEventTableProperties = new TableProperties();
		collectionProtocolEventTableProperties.setName("catissue_coll_prot_event");
		collectionProtocolEventTableProperties.setId(COLLECTION_PROTOCOL_EVT_ID);
		((Entity)e).setTableProperties(collectionProtocolEventTableProperties);
		return e;
	}

	/*
	 * @param name
	 * Creates a specimen collection group entity, sets attributes collection 
	 * and table properties for the entity.
	 */
	private EntityInterface createSpecimenCollectionGroupEntity(String name)
	{
		EntityInterface e = factory.createEntity();
		e.setName(SPECIMEN_COLLECTION_GROUP_NAME);
		e.setCreatedDate(new Date());
		e.setDescription("This is a specimen collection group entity");
		e.setId(SPECIMEN_COLLECTION_GROUP_ID);
		e.setLastUpdated(new Date());

		((Entity)e).setAbstractAttributeCollection(getSpecimenCollectionGroupAttributes());

		TableProperties specimenCollectionGroupTableProperties = new TableProperties();
		specimenCollectionGroupTableProperties.setName("catissue_specimen_coll_group");
		specimenCollectionGroupTableProperties.setId(SPECIMEN_COLLECTION_GROUP_ID);
		((Entity)e).setTableProperties(specimenCollectionGroupTableProperties);
		return e;
	}

	/*
	 * @param name
	 * Creates a specimen entity, sets attributes collection 
	 * and table properties for the entity.
	 */
	private EntityInterface createSpecimenEntity(String name)
	{
		EntityInterface e = factory.createEntity();
		e.setName(SPECIMEN_NAME);
		e.setCreatedDate(new Date());
		e.setDescription("This is a specimen entity");
		e.setId(SPECIMEN_ID);
		e.setLastUpdated(new Date());

		((Entity)e).setAbstractAttributeCollection(getSpecimenAttributes());

		TableProperties specimenTableProperties = new TableProperties();
		specimenTableProperties.setName("catissue_specimen");
		specimenTableProperties.setId(SPECIMEN_ID);
		((Entity)e).setTableProperties(specimenTableProperties);
		return e;
	}

	/*
	 * @param name
	 * Creates a SpecimenCharacteristic entity, sets attributes collection 
	 * and table properties for the entity.
	 */
	private EntityInterface createSpecimenCharacteristicEntity(String name)
	{
		EntityInterface e = factory.createEntity();
		e.setName(SPECIMEN_CHARACTERISTIC_NAME);
		e.setCreatedDate(new Date());
		e.setDescription("This is a specimen Characteristic entity");
		e.setId(SPECIMEN_CHARACTERISTIC_ID);
		e.setLastUpdated(new Date());

		((Entity)e).setAbstractAttributeCollection(getSpecimenCharacteristicAttributes());

		TableProperties specimenCharacteristicTableProperties = new TableProperties();
		specimenCharacteristicTableProperties.setName("catissue_specimen_char");
		specimenCharacteristicTableProperties.setId(SPECIMEN_CHARACTERISTIC_ID);
		((Entity)e).setTableProperties(specimenCharacteristicTableProperties);
		return e;
	}

	/*
	 * @param name
	 * Creates a specimen event parameters entity, sets attributes collection 
	 * and table properties for the entity.
	 */
	private EntityInterface createSpecimenEventParametersEntity(String name)
	{
		EntityInterface e = factory.createEntity();
		e.setName(SPECIMEN_EVT_NAME);
		e.setCreatedDate(new Date());
		e.setDescription("This is a specimen event parameters entity");
		e.setId(SPECIMEN_EVT_ID);
		e.setLastUpdated(new Date());

		((Entity)e).setAbstractAttributeCollection(getSpecimenEventParametersAttributes());

		TableProperties specimenEventParametersTableProperties = new TableProperties();
		specimenEventParametersTableProperties.setName("catissue_specimen_event_param");
		specimenEventParametersTableProperties.setId(SPECIMEN_EVT_ID);
		((Entity)e).setTableProperties(specimenEventParametersTableProperties);
		return e;
	}
	
	/*
	 * @param name
	 * Creates a check in check out event parameters entity, sets attributes collection 
	 * and table properties for the entity.
	 */
	private EntityInterface createCheckInCheckOutEventParameterEntity(String name)
	{
		EntityInterface e = factory.createEntity();
		e.setName(CHKIN_CHKOUT_EVT_NAME);
		e.setCreatedDate(new Date());
		e.setDescription("This is a check in check out event parameters entity");
		e.setId(CHKIN_CHKOUT_EVT_ID);
		e.setLastUpdated(new Date());

		((Entity)e).setAbstractAttributeCollection(getCheckInCheckOutEventParameterAttributes());

		TableProperties checkInCheckOutEventParameterTableProperties = new TableProperties();
		checkInCheckOutEventParameterTableProperties.setName("catissue_in_out_event_param");
		checkInCheckOutEventParameterTableProperties.setId(CHKIN_CHKOUT_EVT_ID);
		((Entity)e).setTableProperties(checkInCheckOutEventParameterTableProperties);
		return e;
	}
	
	/*
	 * @param name
	 * Creates a frozen event parameters entity, sets attributes collection 
	 * and table properties for the entity.
	 */
	private EntityInterface createFrozenEventParametersEntity(String name)
	{
		EntityInterface e = factory.createEntity();
		e.setName(FROZEN_EVT_NAME);
		e.setCreatedDate(new Date());
		e.setDescription("This is a frozen event parameters entity");
		e.setId(FROZEN_EVT_ID);
		e.setLastUpdated(new Date());

		((Entity)e).setAbstractAttributeCollection(getFrozenEventParameterAttributes());

		TableProperties frozenEventParameterTableProperties = new TableProperties();
		frozenEventParameterTableProperties.setName("catissue_frozen_event_param");
		frozenEventParameterTableProperties.setId(FROZEN_EVT_ID);
		((Entity)e).setTableProperties(frozenEventParameterTableProperties);
		return e;
	}	
	
	/*
	 * @param name
	 * Creates a procedure event parameters entity, sets attributes collection 
	 * and table properties for the entity.
	 */
	private EntityInterface createProcedureEventParametersEntity(String name)
	{
		EntityInterface e = factory.createEntity();
		e.setName(PROCEDURE_EVT_NAME);
		e.setCreatedDate(new Date());
		e.setDescription("This is a procedure event parameters entity");
		e.setId(PROCEDURE_EVT_ID);
		e.setLastUpdated(new Date());

		((Entity)e).setAbstractAttributeCollection(getProcedureEventParametersAttributes());

		TableProperties procedureEventParametersTableProperties = new TableProperties();
		procedureEventParametersTableProperties.setName("catissue_procedure_event_param");
		procedureEventParametersTableProperties.setId(PROCEDURE_EVT_ID);
		((Entity)e).setTableProperties(procedureEventParametersTableProperties);
		return e;
	}	
	
	/*
	 * @param name
	 * Creates a received event parameters entity, sets attributes collection 
	 * and table properties for the entity.
	 */
	private EntityInterface createReceivedEventParametersEntity(String name)
	{
		EntityInterface e = factory.createEntity();
		e.setName(RECEIVED_EVT_NAME);
		e.setCreatedDate(new Date());
		e.setDescription("This is a received event parameters entity");
		e.setId(RECEIVED_EVT_ID);
		e.setLastUpdated(new Date());

		((Entity)e).setAbstractAttributeCollection(getReceivedEventParametersAttributes());

		TableProperties receivedEventParametersTableProperties = new TableProperties();
		receivedEventParametersTableProperties.setName("catissue_received_event_param");
		receivedEventParametersTableProperties.setId(RECEIVED_EVT_ID);
		((Entity)e).setTableProperties(receivedEventParametersTableProperties);
		return e;
	}	

	/*
	 * Creates attributes for site entity, creates and sets a 
	 * column property for each attribute and adds all the attributes to
	 * a collection.
	 */
	private ArrayList getSiteAttributes()
	{
		ArrayList<AttributeInterface> siteAttributes = new ArrayList<AttributeInterface>();
		
		AttributeInterface att1 = factory.createLongAttribute();
		//att7.setDefaultValue(20L);
		att1.setName("id");
		ColumnPropertiesInterface c1 = factory.createColumnProperties();
		c1.setName("IDENTIFIER");
		((Attribute)att1).setColumnProperties(c1);
		(att1).setIsPrimaryKey(new Boolean(true));
		
		AttributeInterface att2 =  factory.createStringAttribute();
		//att5.setDefaultValue("firstName");
		att2.setName("name");
		ColumnPropertiesInterface c2 = factory.createColumnProperties();
		c2.setName("NAME");
		((Attribute)att2).setColumnProperties(c2);
		
		AttributeInterface att3 =  factory.createStringAttribute();
		//att5.setDefaultValue("firstName");
		att3.setName("type");
		ColumnPropertiesInterface c3 = factory.createColumnProperties();
		c3.setName("TYPE");
		((Attribute)att3).setColumnProperties(c3);
		
		AttributeInterface att4 =  factory.createStringAttribute();
		//att5.setDefaultValue("firstName");
		att4.setName("emailAddress");
		ColumnPropertiesInterface c4 = factory.createColumnProperties();
		c4.setName("EMAIL_ADDRESS");
		((Attribute)att4).setColumnProperties(c4);
		
		AttributeInterface att5 = factory.createStringAttribute();
		//att1.setDefaultValue("activityStatus");
		att5.setName("activityStatus");
		ColumnPropertiesInterface c5 = factory.createColumnProperties();
		c5.setName("ACTIVITY_STATUS");
		((Attribute)att5).setColumnProperties(c5);

		siteAttributes.add(0, att1);
		siteAttributes.add(1, att2);
		siteAttributes.add(2, att3);
		siteAttributes.add(3, att4);
		siteAttributes.add(4, att5);
		return siteAttributes;
	}
	/*
	 * Creates attributes for participant entity, creates and sets a 
	 * column property for each attribute and adds all the attributes to
	 * a collection.
	 */
	private ArrayList getParticipantAttributes()
	{
		ArrayList<AttributeInterface> participantAttributes = new ArrayList<AttributeInterface>();
		
		AttributeInterface att1 = factory.createStringAttribute();
		//att1.setDefaultValue("activityStatus");
		att1.setName("activityStatus");
		ColumnPropertiesInterface c1 = factory.createColumnProperties();
		c1.setName("ACTIVITY_STATUS");
		((Attribute)att1).setColumnProperties(c1);

		AttributeInterface att2 = factory.createDateAttribute();
		//att2.setDefaultValue(new Date(12 - 03 - 1995));
		att2.setName("birthDate");
		ColumnPropertiesInterface c2 = factory.createColumnProperties();
		c2.setName("BIRTH_DATE");
		((Attribute)att2).setColumnProperties(c2);

		AttributeInterface att3 =  factory.createDateAttribute();
		//att3.setDefaultValue(new Date(12 - 03 - 2005));
		att3.setName("deathDate");
		ColumnPropertiesInterface c3 = factory.createColumnProperties();
		c3.setName("DEATH_DATE");
		((Attribute)att3).setColumnProperties(c3);

		AttributeInterface att4 =  factory.createStringAttribute();
		//att4.setDefaultValue("ethnicity");
		att4.setName("ethnicity");
		ColumnPropertiesInterface c4 = factory.createColumnProperties();
		c4.setName("ETHNICITY");
		((Attribute)att4).setColumnProperties(c4);

		AttributeInterface att5 =  factory.createStringAttribute();
		//att5.setDefaultValue("firstName");
		att5.setName("firstName");
		ColumnPropertiesInterface c5 = factory.createColumnProperties();
		c5.setName("FIRST_NAME");
		((Attribute)att5).setColumnProperties(c5);

		AttributeInterface att6 = factory.createStringAttribute();
		//att6.setDefaultValue("gender");
		att6.setName("gender");
		ColumnPropertiesInterface c6 = factory.createColumnProperties();
		c6.setName("GENDER");
		((Attribute)att6).setColumnProperties(c6);

		AttributeInterface att7 = factory.createLongAttribute();
		//att7.setDefaultValue(20L);
		att7.setName("id");
		
		ColumnPropertiesInterface c7 = factory.createColumnProperties();
		c7.setName("IDENTIFIER");
		((Attribute)att7).setColumnProperties(c7);
		(att7).setIsPrimaryKey(new Boolean(true));

		AttributeInterface att8 = factory.createStringAttribute();;
		//att8.setDefaultValue("lastName");
		att8.setName("lastName");
		ColumnPropertiesInterface c8 = factory.createColumnProperties();
		c8.setName("LAST_NAME");
		((Attribute)att8).setColumnProperties(c8);

		AttributeInterface att9 = factory.createStringAttribute();
		//att9.setDefaultValue("middleName");
		att9.setName("middleName");
		ColumnPropertiesInterface c9 = factory.createColumnProperties();
		c9.setName("MIDDLE_NAME");
		((Attribute)att9).setColumnProperties(c9);

		AttributeInterface att10 = factory.createStringAttribute();
		//att10.setDefaultValue("sexGenotype");
		att10.setName("sexGenotype");
		ColumnPropertiesInterface c10 = factory.createColumnProperties();
		c10.setName("GENOTYPE");
		((Attribute)att10).setColumnProperties(c10);

		AttributeInterface att11 = factory.createStringAttribute();
		//att11.setDefaultValue("socialSecurityNumber");
		att11.setName("socialSecurityNumber");
		ColumnPropertiesInterface c11 = factory.createColumnProperties();
		c11.setName("SOCIAL_SECURITY_NUMBER");
		((Attribute)att11).setColumnProperties(c11);

		AttributeInterface att12 = factory.createStringAttribute();
		//att12.setDefaultValue("vitalStatus");
		att12.setName("vitalStatus");
		ColumnPropertiesInterface c12 = factory.createColumnProperties();
		c12.setName("VITAL_STATUS");
		((Attribute)att12).setColumnProperties(c12);

		participantAttributes.add(0, att1);
		participantAttributes.add(1, att2);
		participantAttributes.add(2, att3);
		participantAttributes.add(3, att4);
		participantAttributes.add(4, att5);
		participantAttributes.add(5, att6);
		participantAttributes.add(6, att7);
		participantAttributes.add(7, att8);
		participantAttributes.add(8, att9);
		participantAttributes.add(9, att10);
		participantAttributes.add(10, att11);
		participantAttributes.add(11, att12);

		return participantAttributes;
	}

	/*
	 * Creates attributes for participant medical identifier entity, creates 
	 * and sets a column property for each attribute and adds all the 
	 * attributes to a collection.
	 */
	private ArrayList getParticipantMedicalIdentifierAttributes()
	{
		ArrayList<AttributeInterface> participantMedicalIdentifierAttributes = new ArrayList<AttributeInterface>();

		AttributeInterface att1 = factory.createLongAttribute();
		att1.setName("id");
		ColumnPropertiesInterface c1 = factory.createColumnProperties();
		c1.setName("IDENTIFIER");
		((Attribute)att1).setColumnProperties(c1);
		att1.setIsPrimaryKey(new Boolean(true));

		AttributeInterface att2 = factory.createStringAttribute();
		att2.setName("medicalRecordNumber");
		ColumnPropertiesInterface c2 = factory.createColumnProperties();
		c2.setName("MEDICAL_RECORD_NUMBER");
		((Attribute)att2).setColumnProperties(c2);

//		LongAttribute att3 = new LongAttribute();
//		att3.setName("id");
//		att3.setMeasurementUnits("Long");
//		ColumnProperties c3 = factory.createColumnProperties();
//		c3.setName("PARTICIPANT_ID");
//		att3.setColumnProperties(c3);
		
		participantMedicalIdentifierAttributes.add(0, att1);
		participantMedicalIdentifierAttributes.add(1, att2);
//		participantMedicalIdentifierAttributes.add(2, att3);
		return participantMedicalIdentifierAttributes;
	}

	/*
	 * Creates attributes for collection protocol registration entity,  
	 * creates and sets a column property for each attribute and adds all  
	 * the attributes to a collection.
	 */
	private ArrayList getCollectionProtocolRegistrationAttributes()
	{
		ArrayList<AttributeInterface> collectionProtocolRegistrationAttributes = new ArrayList<AttributeInterface>();

		AttributeInterface att1 = factory.createStringAttribute();
		att1.setName("activityStatus");
		ColumnPropertiesInterface c1 = factory.createColumnProperties();
		c1.setName("ACTIVITY_STATUS");
		((Attribute)att1).setColumnProperties(c1);

		AttributeInterface att2 = factory.createLongAttribute();
		att2.setName("id");
		ColumnPropertiesInterface c2 = factory.createColumnProperties();
		c2.setName("IDENTIFIER");
		((Attribute)att2).setColumnProperties(c2);
		att2.setIsPrimaryKey(new Boolean(true));

		AttributeInterface att3 = factory.createStringAttribute();
		att3.setName("protocolParticipantIdentifier");
		ColumnPropertiesInterface c3 = factory.createColumnProperties();
		c3.setName("PROTOCOL_PARTICIPANT_ID");
		((Attribute)att3).setColumnProperties(c3);

		AttributeInterface att4 = factory.createDateAttribute();
		att4.setName("registrationDate");
		ColumnPropertiesInterface c4 = factory.createColumnProperties();
		c4.setName("REGISTRATION_DATE");
		((Attribute)att4).setColumnProperties(c4);

		collectionProtocolRegistrationAttributes.add(0, att1);
		collectionProtocolRegistrationAttributes.add(1, att2);
		collectionProtocolRegistrationAttributes.add(2, att3);
		collectionProtocolRegistrationAttributes.add(3, att4);

		return collectionProtocolRegistrationAttributes;
	}

	/*
	 * Creates attributes for collection protocol entity,  
	 * creates and sets a column property for each attribute and adds all  
	 * the attributes to a collection.
	 */
	private ArrayList getCollectionProtocolAttributes()
	{
		ArrayList<AttributeInterface> collectionProtocolAttributes = new ArrayList<AttributeInterface>();

		AttributeInterface att1 = factory.createBooleanAttribute();
		att1.setName("aliquotInSameContainer");
		ColumnPropertiesInterface c1 = factory.createColumnProperties();
		c1.setName("ALIQUOT_IN_SAME_CONTAINER");
		((Attribute)att1).setColumnProperties(c1);

		collectionProtocolAttributes.add(0, att1);

		return collectionProtocolAttributes;
	}

	/*
	 * Creates attributes for specimen protocol entity,  
	 * creates and sets a column property for each attribute and adds all  
	 * the attributes to a collection.
	 */
	private ArrayList getSpecimenProtocolAttributes()
	{
		ArrayList<AttributeInterface> specimenProtocolAttributes = new ArrayList<AttributeInterface>();

		AttributeInterface att1 = factory.createStringAttribute();
		att1.setName("activityStatus");
		ColumnPropertiesInterface c1 = factory.createColumnProperties();
		c1.setName("ACTIVITY_STATUS");
		((Attribute)att1).setColumnProperties(c1);

		AttributeInterface att2 = factory.createStringAttribute();
		att2.setName("descriptionURL");
		ColumnPropertiesInterface c2 = factory.createColumnProperties();
		c2.setName("DESCRIPTION_URL");
		((Attribute)att2).setColumnProperties(c2);

		AttributeInterface att3 = factory.createDateAttribute();
		att3.setName("endDate");
		ColumnPropertiesInterface c3 = factory.createColumnProperties();
		c3.setName("END_DATE");
		((Attribute)att3).setColumnProperties(c3);

		AttributeInterface att4 =factory.createIntegerAttribute();
		att4.setName("enrollment");
		ColumnPropertiesInterface c4 = factory.createColumnProperties();
		c4.setName("ENROLLMENT");
		((Attribute)att4).setColumnProperties(c4);

		AttributeInterface att5 = factory.createLongAttribute();
		att5.setName("id");
		ColumnPropertiesInterface c5 = factory.createColumnProperties();
		c5.setName("IDENTIFIER");
		((Attribute)att5).setColumnProperties(c5);
		att5.setIsPrimaryKey(new Boolean(true));

		AttributeInterface att6 = factory.createStringAttribute();
		att6.setName("irbIdentifier");
		ColumnPropertiesInterface c6 = factory.createColumnProperties();
		c6.setName("IRB_IDENTIFIER");
		((Attribute)att6).setColumnProperties(c6);

		AttributeInterface att7 = factory.createStringAttribute();
		att7.setName("shortTitle");
		ColumnPropertiesInterface c7 = factory.createColumnProperties();
		c7.setName("SHORT_TITLE");
		((Attribute)att7).setColumnProperties(c7);

		AttributeInterface att8 = factory.createDateAttribute();
		att8.setName("startDate");
		ColumnPropertiesInterface c8 = factory.createColumnProperties();
		c8.setName("START_DATE");
		((Attribute)att8).setColumnProperties(c8);

		AttributeInterface att9 = factory.createStringAttribute();
		att9.setName("title");
		ColumnPropertiesInterface c9 = factory.createColumnProperties();
		c9.setName("TITLE");
		((Attribute)att9).setColumnProperties(c9);

		specimenProtocolAttributes.add(0, att1);
		specimenProtocolAttributes.add(1, att2);
		specimenProtocolAttributes.add(2, att3);
		specimenProtocolAttributes.add(3, att4);
		specimenProtocolAttributes.add(4, att5);
		specimenProtocolAttributes.add(5, att6);
		specimenProtocolAttributes.add(6, att7);
		specimenProtocolAttributes.add(7, att8);
		specimenProtocolAttributes.add(8, att9);

		return specimenProtocolAttributes;
	}

	/*
	 * Creates attributes for collection protocol event entity,  
	 * creates and sets a column property for each attribute and adds all  
	 * the attributes to a collection.
	 */
	private ArrayList getCollectionProtocolEventAttributes()
	{
		ArrayList<AttributeInterface> collectionProtocolEventAttributes = new ArrayList<AttributeInterface>();
		
		AttributeInterface att1 = factory.createStringAttribute();
		att1.setName("clinicalStatus");
		ColumnPropertiesInterface c1 = factory.createColumnProperties();
		c1.setName("CLINICAL_STATUS");
		((Attribute)att1).setColumnProperties(c1);

		AttributeInterface att2 = factory.createLongAttribute();
		att2.setName("id");
		ColumnPropertiesInterface c2 = factory.createColumnProperties();
		c2.setName("IDENTIFIER");
		((Attribute)att2).setColumnProperties(c2);
		att2.setIsPrimaryKey(new Boolean(true));

		AttributeInterface att3 = factory.createDoubleAttribute();
		att3.setName("studyCalendarEventPoint");
		//att3.setSize(50);
		ColumnPropertiesInterface c3 = factory.createColumnProperties();
		c3.setName("STUDY_CALENDAR_EVENT_POINT");
		((Attribute)att3).setColumnProperties(c3);

		collectionProtocolEventAttributes.add(0, att1);
		collectionProtocolEventAttributes.add(1, att2);
		collectionProtocolEventAttributes.add(2, att3);

		return collectionProtocolEventAttributes;
	}

	/*
	 * Creates attributes for specimen collection group entity,  
	 * creates and sets a column property for each attribute and adds all  
	 * the attributes to a collection.
	 */
	private ArrayList getSpecimenCollectionGroupAttributes()
	{
		ArrayList<AttributeInterface> specimenCollectionGroupAttributes = new ArrayList<AttributeInterface>();

		AttributeInterface att1 = factory.createLongAttribute();
		att1.setName("id");
		ColumnPropertiesInterface c1 = factory.createColumnProperties();
		c1.setName("IDENTIFIER");
		((Attribute)att1).setColumnProperties(c1);
		att1.setIsPrimaryKey(new Boolean(true));

		AttributeInterface att2 = factory.createStringAttribute();
		att2.setName("name");
		ColumnPropertiesInterface c2 = factory.createColumnProperties();
		c2.setName("NAME");
		((Attribute)att2).setColumnProperties(c2);

		AttributeInterface att3 = factory.createStringAttribute();
		att3.setName("clinicalDiagnosis");
		ColumnPropertiesInterface c3 = factory.createColumnProperties();
		c3.setName("CLINICAL_DIAGNOSIS");
		((Attribute)att3).setColumnProperties(c3);

		AttributeInterface att4 = factory.createStringAttribute();
		att4.setName("clinicalStatus");
		ColumnPropertiesInterface c4 = factory.createColumnProperties();
		c4.setName("CLINICAL_STATUS");
		((Attribute)att4).setColumnProperties(c4);

		AttributeInterface att5 = factory.createStringAttribute();
		att5.setName("activityStatus");
		ColumnPropertiesInterface c5 = factory.createColumnProperties();
		c5.setName("ACTIVITY_STATUS");
		((Attribute)att5).setColumnProperties(c5);

		specimenCollectionGroupAttributes.add(0, att1);
		specimenCollectionGroupAttributes.add(1, att2);
		specimenCollectionGroupAttributes.add(2, att3);
		specimenCollectionGroupAttributes.add(3, att4);
		specimenCollectionGroupAttributes.add(4, att5);

		return specimenCollectionGroupAttributes;
	}

	/*
	 * Creates attributes for specimen entity,  
	 * creates and sets a column property for each attribute and adds all  
	 * the attributes to a collection.
	 */
	private ArrayList getSpecimenAttributes()
	{
		ArrayList<AttributeInterface> specimenAttributes = new ArrayList<AttributeInterface>();
		
		AttributeInterface att1 = factory.createStringAttribute();
		att1.setName("activityStatus");
		ColumnPropertiesInterface c1 = factory.createColumnProperties();
		c1.setName("ACTIVITY_STATUS");
		((Attribute)att1).setColumnProperties(c1);

		AttributeInterface att2 = factory.createBooleanAttribute();
		att2.setName("available");
		ColumnPropertiesInterface c2 = factory.createColumnProperties();
		c2.setName("AVAILABLE");
		((Attribute)att2).setColumnProperties(c2);

		AttributeInterface att3 = factory.createStringAttribute();
		att3.setName("barcode");
		ColumnPropertiesInterface c3 = factory.createColumnProperties();
		c3.setName("BARCODE");
		((Attribute)att3).setColumnProperties(c3);

		AttributeInterface att4 = factory.createStringAttribute();
		att4.setName("comment");
		ColumnPropertiesInterface c4 = factory.createColumnProperties();
		c4.setName("COMMENTS");
		((Attribute)att4).setColumnProperties(c4);

		AttributeInterface att5 = factory.createLongAttribute();
		att5.setName("id");
		ColumnPropertiesInterface c5 = factory.createColumnProperties();
		c5.setName("IDENTIFIER");
		((Attribute)att5).setColumnProperties(c5);
		att5.setIsPrimaryKey(new Boolean(true));

		AttributeInterface att6 = factory.createStringAttribute();
		att6.setName("label");
		ColumnPropertiesInterface c6 = factory.createColumnProperties();
		c6.setName("LABEL");
		((Attribute)att6).setColumnProperties(c6);

		AttributeInterface att7 = factory.createStringAttribute();
		att7.setName("lineage");
		ColumnPropertiesInterface c7 = factory.createColumnProperties();
		c7.setName("LINEAGE");
		((Attribute)att7).setColumnProperties(c7);

		AttributeInterface att8 = factory.createStringAttribute();
		att8.setName("pathologicalStatus");
		ColumnPropertiesInterface c8 = factory.createColumnProperties();
		c8.setName("PATHOLOGICAL_STATUS");
		((Attribute)att8).setColumnProperties(c8);

		AttributeInterface att9 = factory.createIntegerAttribute();
		att9.setName("positionDimensionOne");
		ColumnPropertiesInterface c9 = factory.createColumnProperties();
		c9.setName("POSITION_DIMENSION_ONE");
		((Attribute)att9).setColumnProperties(c9);

		AttributeInterface att10 = factory.createIntegerAttribute();
		att10.setName("positionDimensionTwo");
		ColumnPropertiesInterface c10 = factory.createColumnProperties();
		c10.setName("POSITION_DIMENSION_TWO");
		((Attribute)att10).setColumnProperties(c10);

		AttributeInterface att11 = factory.createStringAttribute();
		att11.setName("type");
		ColumnPropertiesInterface c11 = factory.createColumnProperties();
		c11.setName("TYPE");
		((Attribute)att11).setColumnProperties(c11);

		specimenAttributes.add(0, att1);
		specimenAttributes.add(1, att2);
		specimenAttributes.add(2, att3);
		specimenAttributes.add(3, att4);
		specimenAttributes.add(4, att5);
		specimenAttributes.add(5, att6);
		specimenAttributes.add(6, att7);
		specimenAttributes.add(7, att8);
		specimenAttributes.add(8, att9);
		specimenAttributes.add(9, att10);
		specimenAttributes.add(10, att11);

		return specimenAttributes;
	}
	
	/*
	 * Creates attributes for SpecimenCharacteristic entity,  
	 * creates and sets a column property for each attribute and adds all  
	 * the attributes to a collection.
	 */
	private ArrayList getSpecimenCharacteristicAttributes()
	{
		ArrayList<AttributeInterface> specimenCharacteristicAttributes = new ArrayList<AttributeInterface>();
		
		AttributeInterface att1= factory.createLongAttribute();
		att1.setName("id");
		ColumnPropertiesInterface c1 = factory.createColumnProperties();
		c1.setName("IDENTIFIER");
		((Attribute)att1).setColumnProperties(c1);
		att1.setIsPrimaryKey(new Boolean(true));
		
		AttributeInterface att2 = factory.createStringAttribute();
		att2.setName("tissueSite");
		ColumnPropertiesInterface c2 = factory.createColumnProperties();
		c2.setName("TISSUE_SITE");
		((Attribute)att2).setColumnProperties(c2);
		
		AttributeInterface att3 = factory.createStringAttribute();
		att3.setName("tissueSide");
		ColumnPropertiesInterface c3 = factory.createColumnProperties();
		c3.setName("TISSUE_SIDE");
		((Attribute)att3).setColumnProperties(c3);
		
		

		specimenCharacteristicAttributes.add(0, att1);
		specimenCharacteristicAttributes.add(1, att2);
		specimenCharacteristicAttributes.add(2, att3);
		

		return specimenCharacteristicAttributes;
	}
	/*
	 * Creates attributes for specimen event parameters entity,  
	 * creates and sets a column property for each attribute and adds all  
	 * the attributes to a collection.
	 */
	private ArrayList getSpecimenEventParametersAttributes()
	{
		ArrayList<AttributeInterface> specimenEventParametersAttributes = new ArrayList<AttributeInterface>();
		
		AttributeInterface att1 = factory.createLongAttribute();
		att1.setName("id");
		ColumnPropertiesInterface c1 = factory.createColumnProperties();
		c1.setName("IDENTIFIER");
		((Attribute)att1).setColumnProperties(c1);
		
		AttributeInterface att2 = factory.createDateAttribute();
		att2.setName("timestamp");
		ColumnPropertiesInterface c2 = factory.createColumnProperties();
		c2.setName("EVENT_TIMESTAMP");
		((Attribute)att2).setColumnProperties(c2);
		
		AttributeInterface att3 = factory.createStringAttribute();
		att3.setName("comments");
		ColumnPropertiesInterface c3 = factory.createColumnProperties();
		c3.setName("COMMENTS");
		((Attribute)att3).setColumnProperties(c3);
		
		specimenEventParametersAttributes.add(0, att1);
		specimenEventParametersAttributes.add(1, att2);
		specimenEventParametersAttributes.add(2, att3);
		
		return specimenEventParametersAttributes;
	}
	
	/*
	 * Creates attributes for check in check out event parameters entity,  
	 * creates and sets a column property for each attribute and adds all  
	 * the attributes to a collection.
	 */
	private ArrayList getCheckInCheckOutEventParameterAttributes()
	{
		ArrayList<AttributeInterface> checkInCheckOutEventParameterAttributes = new ArrayList<AttributeInterface>();
		
		AttributeInterface att1 = factory.createStringAttribute();
		att1.setName("storageStatus");
		ColumnPropertiesInterface c1 = factory.createColumnProperties();
		c1.setName("STORAGE_STATUS");
		((Attribute)att1).setColumnProperties(c1);
		
		checkInCheckOutEventParameterAttributes.add(0, att1);
		
		return checkInCheckOutEventParameterAttributes;
	}
	
	/*
	 * Creates attributes for frozen event parameters entity,  
	 * creates and sets a column property for each attribute and adds all  
	 * the attributes to a collection.
	 */
	private ArrayList getFrozenEventParameterAttributes()
	{
		ArrayList<AttributeInterface> frozenEventParameterAttributes = new ArrayList<AttributeInterface>();
		
		AttributeInterface att1 = factory.createStringAttribute();
		att1.setName("method");
		ColumnPropertiesInterface c1 = factory.createColumnProperties();
		c1.setName("METHOD");
		((Attribute)att1).setColumnProperties(c1);
		
		frozenEventParameterAttributes.add(0, att1);
		
		return frozenEventParameterAttributes;
	}
	
	/*
	 * Creates attributes for procedure event parameters entity,  
	 * creates and sets a column property for each attribute and adds all  
	 * the attributes to a collection.
	 */
	private ArrayList getProcedureEventParametersAttributes()
	{
		ArrayList<AttributeInterface> procedureEventParameterAttributes = new ArrayList<AttributeInterface>();
			
		AttributeInterface att1 = factory.createStringAttribute();
		att1.setName("url");
		ColumnPropertiesInterface c1 = factory.createColumnProperties();
		c1.setName("URL");
		((Attribute)att1).setColumnProperties(c1);
		
		AttributeInterface att2 = factory.createStringAttribute();
		att2.setName("name");
		ColumnPropertiesInterface c2 = factory.createColumnProperties();
		c2.setName("NAME");
		((Attribute)att2).setColumnProperties(c2);
		
		procedureEventParameterAttributes.add(0, att1);
		procedureEventParameterAttributes.add(0, att2);
		
		return procedureEventParameterAttributes;
	}
	
	/*
	 * Creates attributes for received event parameters entity,  
	 * creates and sets a column property for each attribute and adds all  
	 * the attributes to a collection.
	 */
	private ArrayList getReceivedEventParametersAttributes()
	{
		ArrayList<AttributeInterface> receivedEventParameterAttributes = new ArrayList<AttributeInterface>();
		
		AttributeInterface att1 = factory.createStringAttribute();
		att1.setName("receivedQuality");
		ColumnPropertiesInterface c1 = factory.createColumnProperties();
		c1.setName("RECEIVED_QUALITY");
		((Attribute)att1).setColumnProperties(c1);
		
		receivedEventParameterAttributes.add(0, att1);
		
		return receivedEventParameterAttributes;
	}

	private Attribute getSpecificAttribute(ArrayList list, String aName)
	{
		for (int i = 0; i < list.size(); i++)
		{
			if ((((AttributeInterface) list.get(i)).getName()).equalsIgnoreCase(aName))
			{
				return (Attribute) list.get(i);
			}
		}
		return null;
	}

	public static void main(String[] args)
	{
		EntityManagerMock testMock = new EntityManagerMock();
		try
		{
			System.out.println(testMock.getEntityByName(SPECIMEN_PROTOCOL_NAME).getAbstractAttributeCollection().size());
			System.out.println(testMock.getEntityByName(PARTICIPANT_NAME).getName());
			System.out.println(testMock.getEntityByName(COLLECTION_PROTOCOL_REGISTRATION_NAME).getDescription());
			System.out.println(testMock.getEntityByName(PARTICIPANT_MEDICAL_ID_NAME).getId());
			System.out.println("getAttribute(String, String) METHOD returns--> " + testMock.getAttribute(SPECIMEN_NAME, "lineage").getName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}