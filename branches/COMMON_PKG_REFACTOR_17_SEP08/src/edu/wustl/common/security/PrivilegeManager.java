/**
 * PrivilegeCacheManager will manage all the instances of PrivilegeCache. 
 * This will be a singleton. 
 * Instances of PrivilegeCache can be accessed from the instance of PrivilegeCacheManager
 */

package edu.wustl.common.security;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.util.Permissions;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.security.UserProvisioningManager;
import gov.nih.nci.security.authorization.ObjectPrivilegeMap;
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.authorization.domainobjects.Privilege;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.exceptions.CSTransactionException;

/**
 * @author ravindra_jain
 * creation date : 14th April, 2008
 * @version 1.0
 */
public final class PrivilegeManager
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(PrivilegeManager.class);
	
	/* Singleton instance of PrivilegeCacheManager
	 */
	private static PrivilegeManager instance=new PrivilegeManager();

	/* the map of object id and corresponding PrivilegeCache  
	 */
	private Map<String, PrivilegeCache> privilegeCaches;

	private PrivilegeUtility privilegeUtility;

	private List<String> lazyObjects;
	private List<String> classes;
	private List<String> eagerObjects;

	// CONSTRUCTOR
	private PrivilegeManager()
	{
		lazyObjects = new ArrayList<String>();
		classes = new ArrayList<String>();
		eagerObjects = new ArrayList<String>();

		privilegeUtility = new PrivilegeUtility();
		privilegeCaches = new HashMap<String, PrivilegeCache>();

		readXmlFile("CacheableObjects.xml");
	}

	/**
	 * return the Singleton PrivilegeCacheManager instance.
	 */
	public static PrivilegeManager getInstance()
	{
		return instance;
	}

	/**
	 * to return the PrivilegeCache object from the Map of PrivilegeCaches
	 * @param loginName
	 * @return
	 * @throws Exception
	 */
	public PrivilegeCache getPrivilegeCache(String loginName)
	{
		PrivilegeCache privilegeCache = privilegeCaches.get(loginName);
		if (privilegeCache == null)
		{
			try
			{
				privilegeCache = new PrivilegeCache(loginName);
			}
			catch (Exception exception)
			{
				logger.debug("Unable to create instance of PrivilegeCache for user:"+loginName, exception);
			}
			privilegeCaches.put(loginName, privilegeCache);
		}

		return privilegeCache;
	}

	/**
	 * To get PrivilegeCache objects for all users
	 * belonging to a particular group
	 *
	 * @param groupName
	 * @return
	 * @throws Exception
	 */
	public List<PrivilegeCache> getPrivilegeCaches(String groupName) throws Exception
	{
		List<PrivilegeCache> listOfPrivCaches = new ArrayList<PrivilegeCache>();

		Set<User> users = privilegeUtility.getUserProvisioningManager().getUsers(groupName);

		for (User user : users)
		{
			PrivilegeCache privilegeCache = privilegeCaches.get(user.getUserId().toString());

			if (privilegeCache != null)
			{
				listOfPrivCaches.add(privilegeCache);
			}
		}

		return listOfPrivCaches;
	}

	/**
	 * To get all PrivilegeCache objects.
	 * 
	 * @return
	 * @throws Exception
	 */
	public Collection<PrivilegeCache> getPrivilegeCaches() throws Exception
	{
		return privilegeCaches.values();
	}

	/**
	 * This method will generally be called from CatissueCoreSesssionListener.sessionDestroyed 
	 * in order to remove the corresponding PrivilegeCache from the Session.
	 * @param userId
	 */
	public void removePrivilegeCache(String userId)
	{
		privilegeCaches.remove(userId);
	}

	/**
	 * This Utility method is called dynamically as soon as a 
	 * Site or CollectionProtocol object gets created through the UI
	 * & adds detials regarding that object to the PrivilegeCaches of
	 * appropriate users in Session.
	 *
	 * @param objectId
	 */
	private void addObjectToPrivilegeCaches(String objectId)
	{
		try
		{
			Collection<PrivilegeCache> listOfPrivCaches = getPrivilegeCaches();

			ProtectionElement protectionElement = privilegeUtility.getUserProvisioningManager()
					.getProtectionElement(objectId);

			Collection<ProtectionElement> protElements = new Vector<ProtectionElement>();
			protElements.add(protectionElement);

			for (PrivilegeCache privilegeCache : listOfPrivCaches)
			{
				Collection<ObjectPrivilegeMap> objPrivMapCol = privilegeUtility
						.getUserProvisioningManager().getPrivilegeMap(
								privilegeCache.getLoginName(), protElements);

				if (!objPrivMapCol.isEmpty())
				{
					privilegeCache.addObject(objectId, objPrivMapCol.iterator()
							.next().getPrivileges());
				}
			}
		}

		catch (Exception exception)
		{
			logger.debug("Exception in addObjectToPrivilegeCaches:"+exception);
		}
	}

	public void insertAuthorizationData(List authorizationData, Set protectionObjects,
			String[] dynamicGroups, String objectId)
	{
		PrivilegeUtility utility = new PrivilegeUtility();
		try
		{
			utility.insertAuthorizationData(authorizationData, protectionObjects, dynamicGroups);
		}
		catch (SMException exception)
		{
			logger.debug("Exception in insertAuthorizationData:"+exception);
		}

		addObjectToPrivilegeCaches(objectId);
	}

	/**
	 * Used to Update the privilege of a group 
	 * both in the Cache as well as in the database 
	 * after user (admin) selects Assign Privilege option for group.
	 * @param privilegeName
	 * @param objectType
	 * @param objectIds
	 * @param roleId
	 * @param assignOperation
	 * @throws Exception
	 */
	public void updateGroupPrivilege(String privilegeName, Class objectType, Long[] objectIds,
			String roleId, boolean assignOperation) throws Exception
	{
		PrivilegeUtility utility = new PrivilegeUtility();
		Collection<PrivilegeCache> listOfPrivCaches = null;
		String groupId = utility.getGroupIdForRole(roleId);

		Set<User> users = utility.getUserProvisioningManager().getUsers(groupId);

		for (User user : users)
		{
			listOfPrivCaches = getPrivilegeCaches();

			for (PrivilegeCache privilegeCache : listOfPrivCaches)
			{
				if (privilegeCache.getLoginName().equals(user.getLoginName()))
				{
					for (Long objectId : objectIds)
					{
						privilegeCache.updatePrivilege(objectType.getName() + "_" + objectId,
								privilegeName, assignOperation);
					}
				}
			}
			assignPrivilegeToGroup(privilegeName, objectType, objectIds, roleId, assignOperation);
		}
	}

	/**
	 * This method assigns privilege by privilegeName to the user group
	 * identified by role corresponding to roleId on the objects identified by
	 * objectIds
	 *
	 * @param privilegeName
	 * @param objectIds
	 * @param roleId
	 * @throws SMException
	 */
	private void assignPrivilegeToGroup(String privilegeName, Class objectType, Long[] objectIds,
			String roleId, boolean assignOp) throws SMException
	{
		boolean assignOperation = assignOp;
		PrivilegeUtility utility = new PrivilegeUtility();
		if (privilegeName == null || objectType == null || objectIds == null || roleId == null)
		{
			logger.debug("Cannot assign privilege to user. One of the parameters is null.");
		}
		else
		{
			String groupId;
			String protGrName = null;
			Role role;
			ProtectionGroup protectionGroup;
			try
			{
				//Get user group for the corresponding role
				groupId = utility.getGroupIdForRole(roleId);
				role = utility.getRoleByPrivilege(privilegeName);
				Set roles = new HashSet();
				roles.add(role);
				if ("USE".equals(privilegeName))
				{
					protGrName = "PG_GROUP_" + groupId + "_ROLE_" + role.getId();

					if (assignOperation == Constants.PRIVILEGE_ASSIGN)
					{
						protectionGroup = utility.getProtectionGroup(protGrName);
						logger.info("Assign Protection elements");
						utility.assignProtectionElements(protectionGroup.getProtectionGroupName(),
								objectType, objectIds);
						utility.assignGroupRoleToProtectionGroup(Long.valueOf(groupId), roles,
								protectionGroup, assignOperation);
					}
					else
					{
						logger.info("De Assign Protection elements");
						utility.deAssignProtectionElements(protGrName, objectType,objectIds);
					}
				}
				else
				{
					// In case of assign remove the READ_DENIED privilege of the group
					// and in case of de-assign add the READ_DENIED privilege to the group.
					assignOperation = !assignOperation;
					for (int i = 0; i < objectIds.length; i++)
					{
						if (objectType.getName().equals(Constants.COLLECTION_PROTOCOL_CLASS_NAME))
						{
							protGrName = Constants
									.getCollectionProtocolPGName(objectIds[i]);
						}
						else if (objectType.getName().equals(Constants.DISTRIBUTION_PROTOCOL_CLASS_NAME))
						{
							protGrName = Constants
									.getDistributionProtocolPGName(objectIds[i]);
						}
						protectionGroup = utility.getProtectionGroup(protGrName);
						utility.assignGroupRoleToProtectionGroup(Long.valueOf(groupId), roles,
								protectionGroup, assignOperation);
					}
				}
			}
			catch (CSException csex)
			{
				logger.debug("Exception in method assignPrivilegeToGroup", csex);
				throw new SMException(csex);
			}
		}
	}
	/**
	 * This is a temporary method written for StorageContainer - special case
	 * Used for StorageContainerBizLogic.isDeAssignable() method
	 *
	 * @param roleId
	 * @param objectId
	 * @param privilegeName
	 * @return
	 */
	public boolean hasGroupPrivilege(String roleId, String objectId, String privilegeName)
			throws Exception
	{
		boolean hasGroupPriv=true;
		PrivilegeUtility utility = new PrivilegeUtility();
		String groupId = utility.getGroupIdForRole(roleId);
		Set<User> users = utility.getUserProvisioningManager().getUsers(groupId);

		for (User user : users)
		{
			if (!getPrivilegeCache(user.getLoginName()).hasPrivilege(objectId, privilegeName))
			{
				hasGroupPriv= false;
			}
		}

		return hasGroupPriv;
	}

	private void readXmlFile(String fileName)
	{
		try
		{
			String xmlFileName = fileName;

			InputStream inputXmlFile = this.getClass().getClassLoader().getResourceAsStream(
					xmlFileName);

			if (inputXmlFile != null)
			{
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document doc = builder.parse(inputXmlFile);

				Element root = doc.getDocumentElement();
				NodeList nodeList = root.getElementsByTagName("Class");

				int length = nodeList.getLength();

				for (int counter = 0; counter < length; counter++)
				{
					Element element = (Element) (nodeList.item(counter));
					String temp = element.getAttribute("name");
					classes.add(temp);
				}

				NodeList nodeList1 = root.getElementsByTagName("ObjectType");

				int length1 = nodeList1.getLength();

				for (int counter = 0; counter < length1; counter++)
				{
					Element element = (Element) (nodeList1.item(counter));
					String temp = element.getAttribute("pattern");
					String lazily = element.getAttribute("cacheLazily");

					if (lazily.equalsIgnoreCase("false") || lazily.equalsIgnoreCase(""))
					{
						eagerObjects.add(temp);
					}
					else
					{
						lazyObjects.add(temp.replace('*', '_'));
					}
				}
			}
		}
		catch (ParserConfigurationException excp)
		{
			logger.debug("DocumentBuilder cannot be created:",excp);
		}
		catch (SAXException excp)
		{
			logger.debug("Not able to parse xml file:"+fileName,excp);
		}
		catch (IOException excp)
		{
			logger.debug("Not able to parse xml file:"+fileName,excp);
		}

	}

	public List<String> getClasses()
	{
		return Collections.unmodifiableList(classes);
	}

	public List<String> getLazyObjects()
	{
		return Collections.unmodifiableList(lazyObjects);
	}

	public List<String> getEagerObjects()
	{
		return Collections.unmodifiableList(eagerObjects);
	}

	public void createRole(String roleName, Set<String> privileges) throws CSException, SMException
	{
		PrivilegeUtility privilegeUtility = new PrivilegeUtility();
		Role role = null;
		try
		{
			role = privilegeUtility.getRole(roleName);
		}
		catch (Exception e)
		{
			role = new Role();
			role.setName(roleName);
			role.setDesc("Dynamically created role");
			role.setApplication(privilegeUtility
					.getApplication(SecurityManager.APPLICATION_CONTEXT_NAME));
			Set<Privilege> privilegeList = new HashSet<Privilege>();
			for (String privilegeId : privileges)
			{
				Privilege privilege = privilegeUtility.getUserProvisioningManager()
						.getPrivilegeById(privilegeId);
				privilegeList.add(privilege);
			}
			role.setPrivileges(privilegeList);
			UserProvisioningManager userProvManager = privilegeUtility
					.getUserProvisioningManager();
			userProvManager.createRole(role);
		}

	}

	public void insertPrivileges(List<Integer> userIdsList, List<Integer> entityIdsList,
			String protectionGrpName, Set<String> privileges, String roleName)
			throws CSTransactionException, CSException, SMException
	{
		ProtectionGroup protGroup;
		PrivilegeUtility privilegeUtility = new PrivilegeUtility();
		protGroup = privilegeUtility.getProtectionGroup(protectionGrpName);
		if (protGroup != null)
		{
			protGroup = new ProtectionGroup();
			protGroup.setProtectionGroupName(protectionGrpName);

		}

	}

	/**
	 * get a set of login names of users having given privilege on given object
	 *
	 * @param objectId
	 * @param privilege
	 * @return
	 * @throws CSException
	 */
	public Set<String> getAccesibleUsers(String objectId, String privilege) throws CSException
	{
		Set<String> result = new HashSet<String>();
		try
		{
			UserProvisioningManager userProvManager = privilegeUtility
					.getUserProvisioningManager();

			List list = userProvManager.getAccessibleGroups(objectId, privilege);

			for (Object o : list)
			{
				Group group = (Group) o;

				for (Object o1 : group.getUsers())
				{
					User user = (User) o1;
					result.add(user.getLoginName());
				}
			}
		}
		catch (CSException excp)
		{
			logger.debug("Not able to get instance of UserProvisioningManager:",excp);
			throw excp;
		}

		return result;
	}

}
