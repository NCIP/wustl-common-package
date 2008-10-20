/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright: (c) Washington University, School of Medicine 2004</p>
 *<p>Company: Washington University, School of Medicine, St. Louis.</p>
 *@author Aarti Sharma
 *@version 1.0
 */

package edu.wustl.common.security;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.QueryResultObjectData;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.query.AbstractClient;
import edu.wustl.common.security.exceptions.SMException;
import edu.wustl.common.security.exceptions.SMTransactionException;
import edu.wustl.common.util.Permissions;
import edu.wustl.common.util.Roles;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.dbmanager.DBUtil;
import edu.wustl.common.util.dbmanager.HibernateMetaData;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.global.TextConstants;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.security.AuthenticationManager;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.UserProvisioningManager;
import gov.nih.nci.security.authorization.ObjectPrivilegeMap;
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.authorization.domainobjects.Privilege;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.authorization.domainobjects.ProtectionGroup;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.authorization.domainobjects.User;
import gov.nih.nci.security.dao.GroupSearchCriteria;
import gov.nih.nci.security.dao.ProtectionElementSearchCriteria;
import gov.nih.nci.security.dao.RoleSearchCriteria;
import gov.nih.nci.security.dao.SearchCriteria;
import gov.nih.nci.security.dao.UserSearchCriteria;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.exceptions.CSObjectNotFoundException;
import gov.nih.nci.security.exceptions.CSTransactionException;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: (c) Washington University, School of Medicine 2005
 * </p>
 * <p>
 * Company: Washington University, School of Medicine, St. Louis.
 * </p>
 *
 * @author Aarti Sharma
 * @version 1.0
 */

public class SecurityManager implements Permissions
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(SecurityManager.class);

	/**
	 * AuthenticationManager instance.
	 */
	private static AuthenticationManager authenticationManager = null;

	/**
	 * AuthorizationManager instance.
	 */
	private static AuthorizationManager authorizationManager = null;

	/**
	 * Class instance.
	 */
	private Class requestingClass = null;
	/**
	 * specify boolean variable.
	 */
	public static boolean initialized = false;

	/**
	 * specify APPLICATION_CONTEXT_NAME variable.
	 */
	public static String APPLICATION_CONTEXT_NAME = null;

	/**
	 * specify HashMap rolegroupNamevsId.
	 */
	public static HashMap<String, String> rolegroupNamevsId = new HashMap<String, String>();

	/**
	 * specify ADMINISTRATOR_GROUP.
	 */
	public static final String ADMINISTRATOR_GROUP = "ADMINISTRATOR_GROUP";

	/**
	 * specify SUPER_ADMINISTRATOR_GROUP.
	 */
	public static final String SUPER_ADMINISTRATOR_GROUP = "SUPER_ADMINISTRATOR_GROUP";

	/**
	 * specify SUPERVISOR_GROUP.
	 */
	public static final String SUPERVISOR_GROUP = "SUPERVISOR_GROUP";

	/**
	 * specify TECHNICIAN_GROUP.
	 */
	public static final String TECHNICIAN_GROUP = "TECHNICIAN_GROUP";

	/**
	 * specify PUBLIC_GROUP.
	 */
	public static final String PUBLIC_GROUP = "PUBLIC_GROUP";

	/**
	 * specify CLASS_NAME.
	 */
	public static final String CLASS_NAME = "CLASS_NAME";

	/**
	 * specify table name.
	 */
	public static final String TABLE_NAME = "TABLE_NAME";

	/**
	 * specify table alias name.
	 */
	public static final String TABLE_ALIAS_NAME = "TABLE_ALIAS_NAME";

	/**
	 * specify securityDataPrefix.
	 */
	private static String securityDataPrefix = CLASS_NAME;

	/**
	 * specify SECURITY_MANAGER_PROP.
	 */
	private static Properties SECURITY_MANAGER_PROP;

	static
	{
		InputStream inputStream = DBUtil.class.getClassLoader().getResourceAsStream(
				Constants.SECURITY_MANAGER_PROP_FILE);
		SECURITY_MANAGER_PROP = new Properties();
		try
		{
			SECURITY_MANAGER_PROP.load(inputStream);
			inputStream.close();
		}
		catch (IOException exception)
		{
			logger.warn("Not able to initialize Security Manager Properties.", exception);
		}
	}

	/**
	 * @param class1 class
	 */
	public SecurityManager(Class class1)
	{
		super();
		requestingClass = class1;
		if (!initialized)
		{
			getApplicationContextName();
			initializeConstants();
		}
	}

	/**
	 * This method gets instance of SecurityManager.
	 * @param class1 class
	 * @return securityManager
	 */
	public static final SecurityManager getInstance(Class class1)
	{
		Class className = null;
		SecurityManager securityManager = null;
		try
		{
			String securityManagerClass = SECURITY_MANAGER_PROP
					.getProperty(Constants.SECURITY_MANAGER_CLASSNAME);
			if (securityManagerClass != null)
			{
				className = Class.forName(securityManagerClass);
			}
			if (className != null)
			{
				Constructor[] cons = className.getConstructors();
				securityManager = (SecurityManager) cons[0].newInstance(class1);
			}
		}
		catch (Exception exception)
		{
			logger.warn("Error in creating SecurityManager instance", exception);
		}

		if (null == securityManager)
		{
			securityManager = new SecurityManager(class1);
		}
		return securityManager;
	}

	/**
	 * This method gets Application Context Name.
	 * @return applicationName
	 */
	public static String getApplicationContextName()
	{
		String applicationName = "";
		try
		{
			applicationName = SECURITY_MANAGER_PROP.getProperty(Constants.APPLN_CONTEXT_NAME);
			APPLICATION_CONTEXT_NAME = applicationName;
		}
		catch (Exception exception)
		{
			logger.warn("Error in getting application context name", exception);
		}

		return applicationName;
	}

	/**
	* Returns group id from Group name.
	* @param groupName group Name
	* @return group Id
	* @throws CSException CSException
	* @throws SMException SMException
	*/
	private String getGroupID(String groupName) throws CSException, SMException
	{
		List list;
		String groupId = null;
		Group group = new Group();
		group.setGroupName(groupName);
		UserProvisioningManager userProvisioningManager = getUserProvisioningManager();
		SearchCriteria searchCriteria = new GroupSearchCriteria(group);
		group.setApplication(userProvisioningManager.getApplication(APPLICATION_CONTEXT_NAME));
		list = getObjects(searchCriteria);
		if (!list.isEmpty())
		{
			group = (Group) list.get(0);
			groupId = group.getGroupId().toString();
		}

		return groupId;
	}

	/**
	 * Returns role id from role name.
	 * @param roleName role Name
	 * @return roleId
	 * @throws CSException CSException
	 * @throws SMException SMException
	 */
	private String getRoleID(String roleName) throws CSException, SMException
	{
		List list;
		String roleId = null;
		Role role = new Role();
		role.setName(roleName);
		SearchCriteria searchCriteria = new RoleSearchCriteria(role);
		UserProvisioningManager userProvisioningManager = getUserProvisioningManager();
		role.setApplication(userProvisioningManager.getApplication(APPLICATION_CONTEXT_NAME));
		list = getObjects(searchCriteria);
		if (!list.isEmpty())
		{
			role = (Role) list.get(0);
			roleId = role.getId().toString();
		}
		return roleId;
	}

	/**
	 * This method initialize Constants.
	 */
	public void initializeConstants()
	{
		try
		{
			rolegroupNamevsId.put(Constants.ADMINISTRATOR_ROLE,
					getRoleID(Constants.ROLE_ADMINISTRATOR));
			rolegroupNamevsId.put(Constants.PUBLIC_ROLE, getRoleID(Constants.SCIENTIST));
			rolegroupNamevsId.put(Constants.TECHNICIAN_ROLE, getRoleID(Constants.TECHNICIAN));
			rolegroupNamevsId.put(Constants.SUPERVISOR_ROLE, getRoleID(Constants.SUPERVISOR));
			rolegroupNamevsId
					.put(Constants.ADMINISTRATOR_GROUP_ID, getGroupID(ADMINISTRATOR_GROUP));
			rolegroupNamevsId.put(Constants.PUBLIC_GROUP_ID, getGroupID(PUBLIC_GROUP));
			rolegroupNamevsId.put(Constants.TECHNICIAN_GROUP_ID, getGroupID(TECHNICIAN_GROUP));
			rolegroupNamevsId.put(Constants.SUPERVISOR_GROUP_ID, getGroupID(SUPERVISOR_GROUP));
			rolegroupNamevsId.put(Constants.SUPER_ADMINISTRATOR_ROLE,
					getRoleID(Constants.ROLE_SUPER_ADMINISTRATOR));
			rolegroupNamevsId.put(Constants.SUPER_ADMINISTRATOR_GROUP_ID,
					getRoleID(SUPER_ADMINISTRATOR_GROUP));
			initialized = true;
		}
		catch (CSException exception)
		{
			logger.warn("Error in initializing rolegroupNamevsId map", exception);
		}
		catch (SMException exception)
		{
			logger.warn("Error in initializing rolegroupNamevsId map", exception);
		}
	}

	/**
	 * Returns the AuthenticationManager for the caTISSUE Core. This method
	 * follows the singleton pattern so that only one AuthenticationManager is
	 * created for the caTISSUE Core.
	 *
	 * @return authenticationManager
	 * @throws	CSException CSException
	 */
	protected AuthenticationManager getAuthenticationManager() throws CSException
	{
		if (authenticationManager == null)
		{
			authenticationManager = SecurityServiceProvider
					.getAuthenticationManager(APPLICATION_CONTEXT_NAME);
		}
		return authenticationManager;
	}

	/**
	 * Returns the Authorization Manager for the caTISSUE Core. This method
	 * follows the singleton pattern so that only one AuthorizationManager is
	 * created.
	 *
	 * @return authorizationManager
	 * @throws	CSException CSException
	 */
	protected AuthorizationManager getAuthorizationManager() throws CSException
	{

		if (authorizationManager == null)
		{
			authorizationManager = SecurityServiceProvider
					.getAuthorizationManager(APPLICATION_CONTEXT_NAME);
		}

		return authorizationManager;
	}

	/**
	 * Returns the UserProvisioningManager singleton object.
	 *
	 * @return
	 * @throws	CSException
	 */
	public UserProvisioningManager getUserProvisioningManager() throws CSException
	{
		return (UserProvisioningManager) getAuthorizationManager();
	}

	/**
	 * Returns true or false depending on the person gets authenticated or not.
	 * @param requestingClass
	 * @param loginName login name
	 * @param password password
	 * @return Returns true or false depending on the person gets authenticated or not.
	 * @throws SMException SMException
	 */
	public boolean login(String loginName, String password) throws SMException
	{
		boolean loginSuccess = false;
		try
		{
			AuthenticationManager authMngr = getAuthenticationManager();
			loginSuccess = authMngr.login(loginName, password);
		}
		catch (CSException exception)
		{
			StringBuffer mesg = new StringBuffer("Authentication fails for user").append(loginName)
					.append("requestingClass:").append(requestingClass);
			logger.debug(mesg.toString());
			throw new SMException(mesg.toString(), exception);
		}
		return loginSuccess;
	}

	/**
	 * This method creates a new User in the database based on the data passed.
	 *
	 * @param user
	 *            user to be created
	 * @throws SMTransactionException
	 *             If there is any exception in creating the User
	 */
	public void createUser(User user) throws SMTransactionException
	{
		try
		{
			getUserProvisioningManager().createUser(user);
		}
		catch (CSTransactionException exception)
		{
			logger.debug("Unable to create user " + user.getEmailId());
			throw new SMTransactionException(exception.getMessage(), exception);
		}
		catch (CSException exception)
		{
			logger.debug("Unable to create user:" + user.getEmailId(), exception);
		}
	}

	/**
	 * This method returns the User object from the database for the passed
	 * User's Login Name. If no User is found then null is returned
	 *
	 * @param loginName
	 *            Login name of the user
	 * @return returns the User object
	 * @throws SMException SMException
	 */
	public User getUser(String loginName) throws SMException
	{
		try
		{
			return getAuthorizationManager().getUser(loginName);
		}
		catch (CSException exception)
		{
			logger.debug("Unable to get user: " + loginName, exception);
			throw new SMException("Unable to get user: " + loginName, exception);
		}
	}

	/**
	 * This method returns array of CSM user id of all users who are administrators.
	 * @return userId user Id
	 * @throws SMException SMException
	 */
	public Long[] getAllAdministrators() throws SMException
	{
		Long[] userId;
		try
		{
			Group group = new Group();
			group.setGroupName(ADMINISTRATOR_GROUP);
			GroupSearchCriteria groupSearchCriteria = new GroupSearchCriteria(group);
			List list = getObjects(groupSearchCriteria);
			group = (Group) list.get(0);
			Set<User> users = group.getUsers();
			userId = new Long[users.size()];
			Iterator<User> iterator = users.iterator();
			for (int i = 0; i < users.size(); i++)
			{
				userId[i] = ((User) iterator.next()).getUserId();
			}
		}
		catch (CSException exception)
		{
			logger.debug("Unable to get users: Exception: " + exception.getMessage());
			throw new SMException(exception.getMessage(), exception);
		}
		return userId;
	}

	/**
	 * This method checks whether a user exists in the database or not.
	 *
	 * @param loginName
	 *            Login name of the user
	 * @return TRUE is returned if a user exists else FALSE is returned
	 * @throws SMException SMException
	 */
	public boolean userExists(String loginName) throws SMException
	{
		boolean userExists = true;
		try
		{
			if (getUser(loginName) == null)
			{
				userExists = false;
			}
		}
		catch (SMException exception)
		{
			logger.debug("Unable to get user :" + loginName);
			throw exception;
		}
		return userExists;
	}

	/**
	 * This method removes user.
	 * @param userId user Id
	 * @throws SMException SMException
	 */
	public void removeUser(String userId) throws SMException
	{
		try
		{
			getUserProvisioningManager().removeUser(userId);
		}
		catch (CSTransactionException ex)
		{
			logger.debug("Unable to get user: Exception: " + ex.getMessage());
			throw new SMTransactionException("Failed to find this user with userId:" + userId, ex);
		}
		catch (CSException e)
		{
			logger.debug("Unable to obtain Authorization Manager: Exception: " + e.getMessage());
			throw new SMException("Failed to find this user with userId:" + userId, e);
		}
	}

	/**
	 * This method returns Vactor of all the role objects defined for the
	 * application from the database.
	 *
	 * @return roles
	 * @throws SMException SMException
	 */
	public Vector getRoles() throws SMException
	{
		Vector<Role> roles = new Vector();
		UserProvisioningManager userProvisioningManager = null;
		try
		{
			userProvisioningManager = getUserProvisioningManager();
			roles.add(userProvisioningManager.getRoleById(rolegroupNamevsId
					.get(Constants.SUPER_ADMINISTRATOR_ROLE)));
			roles.add(userProvisioningManager.getRoleById(rolegroupNamevsId
					.get(Constants.ADMINISTRATOR_ROLE)));
			roles.add(userProvisioningManager.getRoleById(rolegroupNamevsId
					.get(Constants.SUPERVISOR_ROLE)));
			roles.add(userProvisioningManager.getRoleById(rolegroupNamevsId
					.get(Constants.TECHNICIAN_ROLE)));
			roles.add(userProvisioningManager.getRoleById(rolegroupNamevsId
					.get(Constants.PUBLIC_ROLE)));

		}
		catch (CSException exception)
		{
			logger.debug("Unable to get roles: Exception: ", exception);
			throw new SMException(exception.getMessage(), exception);
		}
		return roles;
	}

	/**
	 * Assigns a Role to a User.
	 *
	 * @param userID - the user Id to whom the Role will be assigned
	 * @param roleID -	The id of the Role which is to be assigned to the user
	 * @throws SMException SMException
	 */
	public void assignRoleToUser(String userID, String roleID) throws SMException
	{
		UserProvisioningManager userProvisioningManager = null;
		User user;
		String groupId;
		try
		{
			userProvisioningManager = getUserProvisioningManager();
			user = userProvisioningManager.getUserById(userID);

			//Remove user from any other role if he is assigned some
			userProvisioningManager.removeUserFromGroup(rolegroupNamevsId
					.get(Constants.ADMINISTRATOR_GROUP_ID), String.valueOf(user.getUserId()));
			userProvisioningManager.removeUserFromGroup(rolegroupNamevsId
					.get(Constants.SUPERVISOR_GROUP_ID), String.valueOf(user.getUserId()));
			userProvisioningManager.removeUserFromGroup(rolegroupNamevsId
					.get(Constants.TECHNICIAN_GROUP_ID), String.valueOf(user.getUserId()));
			userProvisioningManager.removeUserFromGroup(rolegroupNamevsId
					.get(Constants.PUBLIC_GROUP_ID), String.valueOf(user.getUserId()));

			//Add user to corresponding group
			groupId = getGroupIdForRole(roleID);
			if (groupId == null)
			{
				logger.info(" User assigned no role");
			}
			else
			{
				assignAdditionalGroupsToUser(String.valueOf(user.getUserId()),
						new String[]{groupId});
				logger.info(" User assigned role:" + groupId);
			}

		}
		catch (CSException exception)
		{
			logger.debug("UNABLE TO ASSIGN ROLE TO USER: Exception: " + exception.getMessage());
			throw new SMException(exception.getMessage(), exception);
		}
	}

	/**
	 * This method gets Group Id For Role.
	 * @param roleID role Id
	 * @return roleGroupId
	 */
	public String getGroupIdForRole(String roleID)
	{
		String roleName = null;
		String roleId = null;
		String roleGroupId = null;
		if (roleID.equals(rolegroupNamevsId.get(Constants.ADMINISTRATOR_ROLE)))
		{
			roleName = Constants.ADMINISTRATOR_ROLE;
			roleId = Constants.ADMINISTRATOR_GROUP_ID;
		}
		else if (roleID.equals(rolegroupNamevsId.get(Constants.SUPERVISOR_ROLE)))
		{
			roleName = Constants.SUPERVISOR_ROLE;
			roleId = Constants.SUPERVISOR_GROUP_ID;
		}
		else if (roleID.equals(rolegroupNamevsId.get(Constants.TECHNICIAN_ROLE)))
		{
			roleName = Constants.TECHNICIAN_ROLE;
			roleId = Constants.TECHNICIAN_GROUP_ID;
		}
		else if (roleID.equals(rolegroupNamevsId.get(Constants.PUBLIC_ROLE)))
		{
			roleName = Constants.PUBLIC_ROLE;
			roleId = Constants.PUBLIC_GROUP_ID;
		}
		else
		{
			logger.debug("role corresponds to no group");
		}
		if (roleId != null)
		{
			roleGroupId = rolegroupNamevsId.get(roleId);
			logger.info("role corresponds to " + roleName);
		}
		return roleGroupId;
	}

	/**
	 * This method gets user role.
	 * @param userID userID
	 * @return role
	 * @throws SMException SMException
	 */
	public Role getUserRole(long userID) throws SMException
	{
		Set<Group> groups;
		UserProvisioningManager userProvisioningManager = null;
		Role role = null;
		try
		{
			userProvisioningManager = getUserProvisioningManager();
			groups = userProvisioningManager.getGroups(String.valueOf(userID));
			role = getRole(groups, userProvisioningManager, role);
		}
		catch (CSException exception)
		{
			logger.debug("Unable to get roles: Exception: " + exception.getMessage(), exception);
			throw new SMException(exception.getMessage(), exception);
		}
		return role;
	}

	/**
	 * This method gets the role.
	 * @param groups groups
	 * @param userProvisioningManager userProvisioningManager
	 * @param role role
	 * @return role
	 * @throws CSObjectNotFoundException CSObjectNotFoundException
	 */
	private Role getRole(Set groups, UserProvisioningManager userProvisioningManager, Role role)
			throws CSObjectNotFoundException
	{
		Group group;
		Iterator<Group> it = groups.iterator();
		while (it.hasNext())
		{
			group = (Group) it.next();
			if (group.getApplication().getApplicationName().equals(APPLICATION_CONTEXT_NAME))
			{
				if (group.getGroupName().equals(ADMINISTRATOR_GROUP))
				{
					role = userProvisioningManager.getRoleById(rolegroupNamevsId
							.get(Constants.ADMINISTRATOR_ROLE));
					break;
				}
				else if (group.getGroupName().equals(SUPERVISOR_GROUP))
				{
					role = userProvisioningManager.getRoleById(rolegroupNamevsId
							.get(Constants.SUPERVISOR_ROLE));
					break;
				}
				else if (group.getGroupName().equals(TECHNICIAN_GROUP))
				{
					role = userProvisioningManager.getRoleById(rolegroupNamevsId
							.get(Constants.TECHNICIAN_ROLE));
					break;
				}
				else if (group.getGroupName().equals(PUBLIC_GROUP))
				{
					role = userProvisioningManager.getRoleById(rolegroupNamevsId
							.get(Constants.PUBLIC_ROLE));
					break;
				}
			}
		}
		return role;
	}

	/**
	 * Name : Virender Mehta.
	 * Reviewer: Sachin Lale
	 * Bug ID: 3842
	 * Patch ID: 3842_2
	 * See also: 3842_1
	 * Description: This function will return the Role name(Administrator, Scientist, Technician, Supervisor )
	 * @param userID user Id
	 * @return Role Name
	 * @throws SMException SMException
	 */
	public String getUserGroup(long userID) throws SMException
	{
		String role = TextConstants.EMPTY_STRING;
		Set groups;
		UserProvisioningManager userProvisioningManager = null;
		Iterator it;
		Group group;
		try
		{
			userProvisioningManager = getUserProvisioningManager();
			groups = userProvisioningManager.getGroups(String.valueOf(userID));
			it = groups.iterator();
			while (it.hasNext())
			{
				group = (Group) it.next();
				if (group.getApplication().getApplicationName().equals(APPLICATION_CONTEXT_NAME))
				{
					if (group.getGroupName().equals(ADMINISTRATOR_GROUP))
					{
						role = Roles.ADMINISTRATOR;
						break;
					}
					else if (group.getGroupName().equals(SUPERVISOR_GROUP))
					{
						role = Roles.SUPERVISOR;
						break;
					}
					else if (group.getGroupName().equals(TECHNICIAN_GROUP))
					{
						role = Roles.TECHNICIAN;
						break;
					}
					else if (group.getGroupName().equals(PUBLIC_GROUP))
					{
						role = Roles.SCIENTIST;
						break;
					}
				}
			}
		}
		catch (CSException exception)
		{
			logger.debug("Unable to get roles: Exception: " + exception.getMessage(), exception);
			throw new SMException(exception.getMessage(), exception);
		}
		return role;
	}

	/**
	 * Modifies an entry for an existing User in the database based on the data
	 * passed.
	 *
	 * @param user -the User object that needs to be modified in the database
	 * @throws SMException if there is any exception in modifying the User in the database
	 */
	public void modifyUser(User user) throws SMException
	{
		try
		{
			getUserProvisioningManager().modifyUser(user);
		}
		catch (CSException exception)
		{
			logger.debug("Unable to modify user: Exception: " + exception.getMessage(), exception);
			throw new SMException(exception.getMessage(), exception);
		}
	}

	/**
	 * Returns the User object for the passed User id.
	 *
	 * @param userId -The id of the User object which is to be obtained
	 * @return The User object from the database for the passed User id
	 * @throws SMException -if the User object is not found for the given id
	 */
	public User getUserById(String userId) throws SMException
	{
		try
		{
			return getUserProvisioningManager().getUserById(userId);
		}
		catch (CSException exception)
		{
			logger.debug("Unable to get user by Id for : " + userId, exception);
			throw new SMException(exception.getMessage(), exception);
		}
	}

	/**
	 * Returns list of the User objects for the passed email address.
	 *
	 * @param emailAddress -Email Address for which users need to be searched
	 * @return List Returns list of the User objects for the passed email address.
	 * @throws SMException if there is any exception while querying the database
	 */
	public List getUsersByEmail(String emailAddress) throws SMException
	{
		try
		{
			User user = new User();
			user.setEmailId(emailAddress);
			SearchCriteria searchCriteria = new UserSearchCriteria(user);
			return getUserProvisioningManager().getObjects(searchCriteria);
		}
		catch (CSException exception)
		{
			logger
					.debug("Unable to get users by emailAddress for email:" + emailAddress,
							exception);
			throw new SMException(exception.getMessage(), exception);
		}
	}

	/**
	 * This method gets users.
	 * @throws SMException SMException
	 * @return users list
	 */
	public List getUsers() throws SMException
	{
		try
		{
			User user = new User();
			SearchCriteria searchCriteria = new UserSearchCriteria(user);
			return getUserProvisioningManager().getObjects(searchCriteria);
		}
		catch (CSException exception)
		{
			logger.debug("Unable to get all users: Exception: " + exception.getMessage());
			throw new SMException(exception.getMessage(), exception);
		}
	}

	/**
	 * Returns list of objects corresponding to the searchCriteria passed.
	 *
	 * @param searchCriteria searchCriteria
	 * @return List of resultant objects
	 * @throws SMException
	 *             if searchCriteria passed is null or if search results in no
	 *             results
	 * @throws CSException CSException
	 */
	public List getObjects(SearchCriteria searchCriteria) throws SMException, CSException
	{
		if (null == searchCriteria)
		{
			logger.debug("searchCriteria is null");
			throw new SMException("Null Parameters passed");
		}
		UserProvisioningManager userProvisioningManager = getUserProvisioningManager();
		List list = userProvisioningManager.getObjects(searchCriteria);
		if (null == list || list.size() <= 0)
		{
			logger.warn("Search resulted in no results");
		}
		return list;
	}

	/**
	 * This method assign user to group.
	 * @param userGroupname user Group name
	 * @param userId user Id
	 * @throws SMException userId
	 */
	public void assignUserToGroup(String userGroupname, String userId) throws SMException
	{
		if (userId == null || userGroupname == null)
		{
			logger.debug(" Null or insufficient Parameters passed");
			throw new SMException("Null or insufficient Parameters passed");
		}

		try
		{
			Group group = getUserGroup(userGroupname);
			if (group == null)
			{
				logger.debug("No user group with name " + userGroupname + " is present");
			}
			else
			{
				String[] groupIds = {group.getGroupId().toString()};
				assignAdditionalGroupsToUser(userId, groupIds);
			}
		}
		catch (CSException exception)
		{
			String mess = "The Security Service encountered a fatal exception.";
			logger.fatal(mess, exception);
			throw new SMException(mess, exception);
		}
	}

	/**
	 * This method removes user from group.
	 * @param userGroupname user Group name
	 * @param userId user Id
	 * @throws SMException SMException
	 */
	public void removeUserFromGroup(String userGroupname, String userId) throws SMException
	{
		if (userId == null || userGroupname == null)
		{
			logger.debug(" Null or insufficient Parameters passed");
			throw new SMException("Null or insufficient Parameters passed");
		}

		try
		{
			UserProvisioningManager userProvisioningManager = getUserProvisioningManager();
			Group group = getUserGroup(userGroupname);
			if (group != null)
			{
				userProvisioningManager.removeUserFromGroup(group.getGroupId().toString(), userId);
			}
			else
			{
				logger.debug("No user group with name " + userGroupname + " is present");
			}
		}
		catch (CSException ex)
		{
			String mess = "The Security Service encountered a fatal exception.";
			logger.fatal(mess, ex);
			throw new SMException(mess, ex);
		}
	}

	/**
	 * This method gets user group.
	 * @param userGroupname user Group name
	 * @return group
	 * @throws SMException SMException
	 * @throws CSException CSException
	 */
	private Group getUserGroup(String userGroupname) throws SMException, CSException
	{
		Group group = new Group();
		group.setGroupName(userGroupname);
		SearchCriteria searchCriteria = new GroupSearchCriteria(group);
		List list = getObjects(searchCriteria);
		if (list.isEmpty())
		{
			group = null;
		}
		else
		{
			group = (Group) list.get(0);
		}

		return group;
	}

	/**
	 * This method assign Additional Groups To User.
	 * @param userId user Id
	 * @param groupIds group Ids
	 * @throws SMException SMException
	 */
	public void assignAdditionalGroupsToUser(String userId, String[] groupIds) throws SMException
	{
		if (userId == null || groupIds == null || groupIds.length < 1)
		{
			String mesg = " Null or insufficient Parameters passed";
			logger.debug(mesg);
			throw new SMException(mesg);
		}
		Set consolidatedGroupIds = new HashSet();
		Set consolidatedGroups;
		String[] finalUserGroupIds;
		UserProvisioningManager userProvisioningManager;
		Group group = null;
		try
		{
			userProvisioningManager = getUserProvisioningManager();
			consolidatedGroups = userProvisioningManager.getGroups(userId);
			if (null != consolidatedGroups)
			{
				Iterator it = consolidatedGroups.iterator();
				while (it.hasNext())
				{
					group = (Group) it.next();
					consolidatedGroupIds.add(String.valueOf(group.getGroupId()));
				}
			}
			//Consolidating all the Groups
			for (int i = 0; i < groupIds.length; i++)
			{
				consolidatedGroupIds.add(groupIds[i]);
			}
			finalUserGroupIds = new String[consolidatedGroupIds.size()];
			Iterator it = consolidatedGroupIds.iterator();
			for (int i = 0; it.hasNext(); i++)
			{
				finalUserGroupIds[i] = (String) it.next();
			}
			//Setting groups for user and updating it
			userProvisioningManager.assignGroupsToUser(userId, finalUserGroupIds);
		}
		catch (CSException exception)
		{
			String mesg = "The Security Service encountered a fatal exception.";
			logger.fatal(mesg, exception);
			throw new SMException(mesg, exception);
		}
	}

	/**
	 * This method checks user is authorized or not.
	 * @param userName user Name
	 * @param objectId object Id
	 * @param privilegeName privilege Name
	 * @return returns true if authorized else false
	 * @throws SMException SMException
	 */
	public boolean isAuthorized(String userName, String objectId, String privilegeName)
			throws SMException
	{
		try
		{
			return getAuthorizationManager().checkPermission(userName, objectId, privilegeName);
		}
		catch (CSException e)
		{
			logger.debug(e.getMessage(), e);
			throw new SMException(e.getMessage(), e);
		}
	}

	/**
	 * This method checks permissions for user.
	 * @param userName user Name
	 * @param objectType object Type
	 * @param objectIdentifier object Identifier
	 * @param privilegeName privilege Name
	 * @return returns true if authorized else false
	 * @throws SMException SMException
	 */
	public boolean checkPermission(String userName, String objectType, String objectIdentifier,
			String privilegeName) throws SMException
	{
		boolean isAuthorized = true;
		if (Boolean.parseBoolean(XMLPropertyHandler.getValue(Constants.ISCHECKPERMISSION)))
		{
			try
			{
				isAuthorized = getAuthorizationManager().checkPermission(userName,
						objectType + "_" + objectIdentifier, privilegeName);
			}
			catch (CSException exception)
			{
				logger.debug("Unable tocheck permissionn", exception);
				throw new SMException("Unable to check permission", exception);
			}
		}
		return isAuthorized;
	}

	/**
	 * This method returns name of the Protection groupwhich consists of obj as
	 * Protection Element and whose name consists of string nameConsistingOf.
	 *
	 * @param obj AbstractDomainObject
	 * @param nameConsistingOf nameConsistingOf
	 * @return returns name of the Protection group which consists of obj as
	 * Protection Element and whose name consists of string nameConsistingOf
	 * @throws SMException SMException
	 */
	public String getProtectionGroupByName(AbstractDomainObject obj, String nameConsistingOf)
			throws SMException
	{
		Set protectionGroups;
		ProtectionGroup protectionGroup;
		ProtectionElement protectionElement;
		String name = null;
		String protectionElementName = obj.getObjectId();
		try
		{
			AuthorizationManager authManager = getAuthorizationManager();
			protectionElement = authManager.getProtectionElement(protectionElementName);
			protectionGroups = authManager.getProtectionGroups(protectionElement
					.getProtectionElementId().toString());
			Iterator<ProtectionGroup> it = protectionGroups.iterator();
			while (it.hasNext())
			{
				protectionGroup = (ProtectionGroup) it.next();
				name = protectionGroup.getProtectionGroupName();
				if (name.indexOf(nameConsistingOf) != -1)
				{
					break;
				}
			}
		}
		catch (CSException exception)
		{
			String mess = new StringBuffer("Unable to get protection group by name").append(
					nameConsistingOf).append(" for Protection Element ").append(
					protectionElementName).toString();
			logger.debug(mess, exception);
			throw new SMException(mess, exception);
		}
		return name;

	}

	/**
	 * This method returns name of the Protection groupwhich consists of obj as
	 * Protection Element and whose name consists of string nameConsistingOf.
	 *
	 * @param obj AbstractDomainObject
	 * @return returns name of the Protection group
	 * @throws SMException SMException
	 */
	public String[] getProtectionGroupByName(AbstractDomainObject obj) throws SMException
	{
		Set protectionGroups;
		Iterator it;
		ProtectionGroup protectionGroup;
		ProtectionElement protectionElement;
		String[] names = null;
		String protectionElementName = obj.getObjectId();
		try
		{
			AuthorizationManager authManager = getAuthorizationManager();
			protectionElement = authManager.getProtectionElement(protectionElementName);
			protectionGroups = authManager.getProtectionGroups(protectionElement
					.getProtectionElementId().toString());
			it = protectionGroups.iterator();
			names = new String[protectionGroups.size()];
			int i = 0;
			while (it.hasNext())
			{
				protectionGroup = (ProtectionGroup) it.next();
				names[i++] = protectionGroup.getProtectionGroupName();

			}
		}
		catch (CSException exception)
		{
			String mess = "Unable to get protection group for Protection Element "
					+ protectionElementName;
			logger.debug(mess, exception);
			throw new SMException(mess, exception);
		}
		return names;

	}

	/**
	 * Returns name value beans corresponding to all privileges that can be
	 * assigned for Assign Privileges Page.
	 * 
	 * @param roleName role name of user logged in
	 * @return
	 */
	public Vector getPrivilegesForAssignPrivilege(String roleName)
	{
		Vector privileges = new Vector();
		NameValueBean nameValueBean;
		nameValueBean = new NameValueBean(Permissions.READ, Permissions.READ);
		privileges.add(nameValueBean);
		//Use privilege only provided to Administrtor in Assing privileges page.
		if (roleName.equals(Constants.ADMINISTRATOR))
		{
			nameValueBean = new NameValueBean(Permissions.USE, Permissions.USE);
			privileges.add(nameValueBean);
		}
		return privileges;
	}

	/**
	 * This method returns NameValueBeans for all the objects of type objectType
	 * on which user with identifier userID has privilege ASSIGN_ <
	 * <privilegeName>>.
	 * 
	 * @param userID
	 * @param objectType
	 * @param privilegeName
	 * @return @throws
	 *         SMException thrown if any error occurs while retreiving
	 *         ProtectionElementPrivilegeContextForUser
	 */
	private Set<NameValueBean> getObjectsForAssignPrivilege(Collection privilegeMap,
			String objectType, String privilegeName) throws SMException
	{
		Set<NameValueBean> objects = new HashSet<NameValueBean>();
		NameValueBean nameValueBean;
		ObjectPrivilegeMap objectPrivilegeMap;
		Collection privileges;
		Iterator iterator;
		String objectId;
		Privilege privilege;

		if (privilegeMap != null)
		{
			iterator = privilegeMap.iterator();
			while (iterator.hasNext())
			{
				objectPrivilegeMap = (ObjectPrivilegeMap) iterator.next();
				objectId = objectPrivilegeMap.getProtectionElement().getObjectId();
				if (objectId.indexOf(objectType + "_") != -1)
				{
					privileges = objectPrivilegeMap.getPrivileges();
					Iterator it = privileges.iterator();
					String name;
					while (it.hasNext())
					{
						privilege = (Privilege) it.next();
						if (privilege.getName().equals("ASSIGN_" + privilegeName))
						{
							name = objectId.substring(objectId.lastIndexOf('_') + 1);
							nameValueBean = new NameValueBean(name, name);
							objects.add(nameValueBean);
							break;
						}
					}
				}
			}
		}

		return objects;
	}

	/**
	 * This method returns name value beans of the object ids for types
	 * identified by objectTypes on which user can assign privileges identified
	 * by privilegeNames User needs to have ASSIGN_ {privilegeName}privilege
	 * on these objects to assign corresponding privilege on them identified by
	 * userID has.
	 *
	 * @param userID
	 * @param objectTypes
	 * @param privilegeNames
	 * @return @throws SMException
	 */
	public Set<NameValueBean> getObjectsForAssignPrivilege(String userID, String[] objectTypes,
			String[] privilegeNames) throws SMException
	{
		Set<NameValueBean> objects = null;

		try
		{
			User user = new User();
			user = getUserById(userID);
			if (objectTypes == null || privilegeNames == null || user == null)
			{
				logger.debug(" User not found");
				objects = new HashSet<NameValueBean>();
			}
			else
			{
				objects = getAssignedPrivilege(objectTypes, privilegeNames, user);
			}
		}
		catch (CSException exception)
		{
			logger.debug("Unable to get objects: ", exception);
			throw new SMException("Unable to get objects: ", exception);
		}
		return objects;

	}

	/**
	 * @param objectTypes
	 * @param privilegeNames
	 * @param objects
	 * @param user
	 * @throws CSException
	 */
	private Set<NameValueBean> getAssignedPrivilege(String[] objectTypes, String[] privilegeNames,
			User user) throws CSException
	{
		Set<NameValueBean> objects = new HashSet<NameValueBean>();
		Collection privilegeMap;
		List list;
		ProtectionElement protectionElement;
		ProtectionElementSearchCriteria protectionElementSearchCriteria;
		AuthorizationManager authorizationManager = getAuthorizationManager();
		for (int i = 0; i < objectTypes.length; i++)
		{
			for (int j = 0; j < privilegeNames.length; j++)
			{
				try
				{
					protectionElement = new ProtectionElement();
					protectionElement.setObjectId(objectTypes[i] + "_*");
					protectionElementSearchCriteria = new ProtectionElementSearchCriteria(
							protectionElement);
					list = getObjects(protectionElementSearchCriteria);
					privilegeMap = authorizationManager.getPrivilegeMap(user.getLoginName(), list);
					for (int k = 0; k < list.size(); k++)
					{
						protectionElement = (ProtectionElement) list.get(k);
					}

					objects.addAll(getObjectsForAssignPrivilege(privilegeMap, objectTypes[i],
							privilegeNames[j]));
				}
				catch (SMException smex)
				{
					logger.debug(" Exception in getting object of privileges", smex);
				}
			}
		}
		return objects;
	}

	/**
	 * @param sessionDataBean
	 * @param queryResultObjectDataMap
	 * @param aList
	 */
	@Deprecated
	public void filterRow(SessionDataBean sessionDataBean, Map queryResultObjectDataMap, List aList)
	{
		// boolean that indicated whether user has privilege on main object
		boolean isAuthorizedForMain = false;

		// boolean that indicated whether user has privilege on related object
		boolean isAuthorizedForRelated = false;

		// boolean that indicates whether user has privilege on identified data
		boolean hasPrivilegeOnIdentifiedData = false;

		Set keySet = queryResultObjectDataMap.keySet();
		Iterator keyIterator = keySet.iterator();
		QueryResultObjectData queryResultObjectData2;
		QueryResultObjectData queryResultObjectData3;
		List queryObjects;

		while (keyIterator.hasNext())
		{
			queryResultObjectData2 = (QueryResultObjectData) queryResultObjectDataMap
					.get(keyIterator.next());
			isAuthorizedForMain = checkPermission(sessionDataBean.getUserName(),
					queryResultObjectData2.getAliasName(), aList.get(queryResultObjectData2
							.getIdentifierColumnId()), Permissions.READ_DENIED);

			isAuthorizedForMain = !isAuthorizedForMain;
			logger.debug("Main object:" + queryResultObjectData2.getAliasName()
					+ " isAuthorizedForMain:" + isAuthorizedForMain);

			//Remove the data from the fields directly related to main object
			if (!isAuthorizedForMain)
			{
				logger.debug("Removed Main Object Fields...................");
				removeUnauthorizedFieldsData(aList, queryResultObjectData2, false);
			}
			else
			{
				logger.debug("For Identified Data : User : " + sessionDataBean.getUserName()
						+ " Alias Name : " + queryResultObjectData2.getAliasName()
						+ "Identifed Column : "
						+ aList.get(queryResultObjectData2.getIdentifierColumnId()));
				hasPrivilegeOnIdentifiedData = checkPermission(sessionDataBean.getUserName(),
						queryResultObjectData2.getAliasName(), aList.get(queryResultObjectData2
								.getIdentifierColumnId()), Permissions.IDENTIFIED_DATA_ACCESS);

				logger.debug("hasPrivilegeOnIdentifiedData:" + hasPrivilegeOnIdentifiedData);

				if (!hasPrivilegeOnIdentifiedData)
				{
					removeUnauthorizedFieldsData(aList, queryResultObjectData2, true);
				}
			}

			logger.debug("isAuthorizedForMain***********************" + isAuthorizedForMain);
			// Check the privilege on related objects when the privilege on main object is de-assigned.
			logger.debug("Check Permission of Related Objects..................");
			queryObjects = queryResultObjectData2.getRelatedQueryResultObjects();
			for (int j = 0; j < queryObjects.size(); j++)
			{
				queryResultObjectData3 = (QueryResultObjectData) queryObjects.get(j);

				//If authorized to see the main object then check for
				// authorization on dependent object
				if (isAuthorizedForMain)
				{
					isAuthorizedForRelated = checkPermission(sessionDataBean.getUserName(),
							queryResultObjectData3.getAliasName(), aList.get(queryResultObjectData3
									.getIdentifierColumnId()), Permissions.READ_DENIED);
					isAuthorizedForRelated = !isAuthorizedForRelated;
				}
				//else set it false
				else
				{
					isAuthorizedForRelated = false;
				}

				logger.debug("Related object:" + queryResultObjectData3.getAliasName()
						+ " isAuthorizedForRelated:" + isAuthorizedForRelated);

				//If not authorized to see related objects
				//remove the data from the fields directly related to related
				// object
				if (!isAuthorizedForRelated)
				{
					removeUnauthorizedFieldsData(aList, queryResultObjectData3, false);
				}
				else
				{
					hasPrivilegeOnIdentifiedData = checkPermission(sessionDataBean.getUserName(),
							queryResultObjectData3.getAliasName(), aList.get(queryResultObjectData3
									.getIdentifierColumnId()), Permissions.IDENTIFIED_DATA_ACCESS);

					if (!hasPrivilegeOnIdentifiedData)
					{
						removeUnauthorizedFieldsData(aList, queryResultObjectData3, true);
					}
				}
			}
		}
	}

	/**
	 * This method removes data from list aList.
	 * It could be all data related to QueryResultObjectData
	 * or only the identified fields depending on 
	 * the value of boolean removeOnlyIdentifiedData
	 * user
	 * @param aList
	 * @param queryResultObjectData3
	 * @param removeOnlyIdentifiedData
	 */
	@Deprecated
	private void removeUnauthorizedFieldsData(List aList,
			QueryResultObjectData queryResultObjectData3, boolean removeOnlyIdentifiedData)
	{

		logger.debug(" Table:" + queryResultObjectData3.getAliasName()
				+ " removeOnlyIdentifiedData:" + removeOnlyIdentifiedData);
		List objectColumnIds;

		//If removeOnlyIdentifiedData is true then get Identified data column
		// ids
		//else get all column Ids to remove them
		if (removeOnlyIdentifiedData)
		{
			objectColumnIds = queryResultObjectData3.getIdentifiedDataColumnIds();
		}
		else
		{
			objectColumnIds = queryResultObjectData3.getDependentColumnIds();
		}
		logger.debug("objectColumnIds:" + objectColumnIds);
		if (objectColumnIds != null)
		{
			for (int k = 0; k < objectColumnIds.size(); k++)
			{
				aList.set(((Integer) objectColumnIds.get(k)).intValue() - 1, "##");
			}
		}
	}

	/**
	 * This method checks whether user identified by userName has given
	 * permission on object identified by identifier of table identified by
	 * tableAlias
	 * 
	 * @param userName
	 * @param tableAlias
	 * @param identifier
	 * @param permission
	 * @return
	 */
	@Deprecated
	public boolean checkPermission(String userName, String tableAlias, Object identifier,
			String permission)
	{
		if (Boolean.parseBoolean(XMLPropertyHandler.getValue(Constants.ISCHECKPERMISSION)))
		{
			boolean isAuthorized = false;
			String tableName = (String) AbstractClient.objectTableNames.get(tableAlias);
			logger.debug(" AliasName:" + tableAlias + " tableName:" + tableName + " Identifier:"
					+ identifier + " Permission:" + permission + " userName" + userName);

			String securityDataPrefixForTable;

			//Aarti: Security Data in database might be on the basis of classname/table name/table alias name
			//Depending on the option that an application chooses corresponding prefix is used to check permissions
			if (securityDataPrefix.equals(CLASS_NAME))
			{
				securityDataPrefixForTable = HibernateMetaData.getClassName(tableName);
				if (tableName.equals(Constants.CATISSUE_SPECIMEN))
				{
					try
					{
						Class classObject = Class.forName(securityDataPrefixForTable);
						securityDataPrefixForTable = classObject.getSuperclass().getName();
					}
					catch (ClassNotFoundException classNotExp)
					{
						logger.debug("Class " + securityDataPrefixForTable + " not present.");
					}
				}

				//Get classname mapping to tableAlias
				if (securityDataPrefixForTable == null)
				{
					return isAuthorized;
				}
			}
			else if (securityDataPrefix.equals(TABLE_ALIAS_NAME))
			{
				securityDataPrefixForTable = tableAlias;
			}
			else if (securityDataPrefix.equals(TABLE_NAME))
			{
				securityDataPrefixForTable = tableName;
			}
			else
			{
				securityDataPrefixForTable = "";
			}

			//checking privilege type on class.
			//whether it is class level / object level / no privilege
			int privilegeType = Integer.parseInt((String) AbstractClient.privilegeTypeMap
					.get(tableAlias));
			logger.debug(" privilege type:" + privilegeType);

			try
			{
				//If type of privilege is class level check user's privilege on
				// class
				if (privilegeType == Constants.CLASS_LEVEL_SECURE_RETRIEVE)
				{
					isAuthorized = SecurityManager.getInstance(this.getClass()).isAuthorized(
							userName, securityDataPrefixForTable, permission);
				}
				//else if it is object level check user's privilege on object
				// identifier
				else if (privilegeType == Constants.OBJECT_LEVEL_SECURE_RETRIEVE)
				{
					isAuthorized = SecurityManager.getInstance(this.getClass()).checkPermission(
							userName, securityDataPrefixForTable, String.valueOf(identifier),
							permission);
				}
				//else no privilege needs to be checked
				else if (privilegeType == Constants.INSECURE_RETRIEVE)
				{
					isAuthorized = true;
				}

			}
			catch (SMException e)
			{
				logger.debug(" Exception while checking permission:" + e.getMessage(), e);
				return isAuthorized;
			}
			return isAuthorized;
		}
		return true;
	}

	/**
	 * This method returns true if user has privilege on identified data in list
	 * else false
	 * @author aarti_sharma
	 * @param sessionDataBean
	 * @param queryResultObjectDataMap
	 * @param list
	 * @return
	 */
	@Deprecated
	public boolean hasPrivilegeOnIdentifiedData(SessionDataBean sessionDataBean,
			Map queryResultObjectDataMap, List aList)
	{
		// boolean that indicates whether user has privilege on identified data
		boolean hasPrivilegeOnIdentifiedData = true;

		Set keySet = queryResultObjectDataMap.keySet();
		Iterator keyIterator = keySet.iterator();
		QueryResultObjectData queryResultObjectData2;

		while (keyIterator.hasNext())
		{

			queryResultObjectData2 = (QueryResultObjectData) queryResultObjectDataMap
					.get(keyIterator.next());

			if (hasAssociatedIdentifiedData(queryResultObjectData2.getAliasName()))
			{

				hasPrivilegeOnIdentifiedData = checkPermission(sessionDataBean.getUserName(),
						queryResultObjectData2.getAliasName(), aList.get(queryResultObjectData2
								.getIdentifierColumnId()), Permissions.IDENTIFIED_DATA_ACCESS);
				//if user does not have privilege on even a single identified
				// data in row,user does not have privilege on all the identified data in that row
				if (!hasPrivilegeOnIdentifiedData)
				{
					hasPrivilegeOnIdentifiedData = false;
				}
			}
		}

		return hasPrivilegeOnIdentifiedData;
	}

	/**
	 * Checks whether an object type has any identified data associated with
	 * it or not.
	 * @param aliasName
	 * @return
	 */
	private boolean hasAssociatedIdentifiedData(String aliasName)
	{
		boolean hasIdentifiedData = false;
		Vector identifiedData = new Vector();
		identifiedData = (Vector) AbstractClient.identifiedDataMap.get(aliasName);
		if (identifiedData != null)
		{
			hasIdentifiedData = true;
		}
		return hasIdentifiedData;
	}

	public static String getSecurityDataPrefix()
	{
		return securityDataPrefix;
	}

	public static void setSecurityDataPrefix(String securityDataPrefix)
	{
		SecurityManager.securityDataPrefix = securityDataPrefix;
	}

	/**
	 * Description: This method checks user's privilege on identified data.
	 * Name : Aarti Sharma
	 * Reviewer: Sachin Lale
	 * Bug ID: 4111
	 * Patch ID: 4111_2
	 * See also: 4111_1
	 * @param userId User's Identifier
	 * @return true if user has privilege on identified data else false
	 * @throws SMException
	 */
	public boolean hasIdentifiedDataAccess(Long userId) throws SMException
	{
		boolean hasIdentifiedDataAccess = false;
		try
		{
			//Get user's role
			Role role = getUserRole(userId.longValue());
			UserProvisioningManager userProvisioningManager = getUserProvisioningManager();

			//Get privileges the user has based on his role
			Set<Privilege> privileges = userProvisioningManager.getPrivileges(String.valueOf(role
					.getId()));
			Iterator<Privilege> privIterator = privileges.iterator();
			Privilege privilege;

			// If user has Identified data access set hasIdentifiedDataAccess true
			for (int i = 0; i < privileges.size(); i++)
			{
				privilege = (Privilege) privIterator.next();
				if (privilege.getName().equals(Permissions.IDENTIFIED_DATA_ACCESS))
				{
					hasIdentifiedDataAccess = true;
					break;
				}
			}
		}
		catch (CSException exception)
		{
			logger.debug("Exception in hasIdentifiedDataAccess method", exception);
			throw new SMException(exception.getMessage(), exception);
		}
		return hasIdentifiedDataAccess;

	}
}
