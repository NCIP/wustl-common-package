/*L
 * Copyright Washington University in St. Louis, SemanticBits, Persistent Systems, Krishagni.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/wustl-common-package/LICENSE.txt for details.
 */

/**
 * 
 */

package edu.wustl.common.querysuite.queryobject.util;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.cab2b.server.util.InheritanceUtil;

/**
 * Factory pattern for calling InheritanceUtil class methods.
 * @author prafull_kadam
 */
public class InheritanceUtils implements InheritanceUtilsInterface
{

	private static InheritanceUtilsInterface instance = new InheritanceUtils();

	/**
	 * constructor for singolton implementation.
	 */
	protected InheritanceUtils()
	{
	}

	/**
	 * @return the instance
	 */
	public static InheritanceUtilsInterface getInstance()
	{
		if (instance == null)
		{
			instance = new InheritanceUtils();
		}
		return instance;
	}

	/**
	 * This method will be used for mocking purpose only.
	 * @param instance the instance to set
	 */
	public static void setInstance(InheritanceUtilsInterface instance)
	{

		InheritanceUtils.instance = instance;
	}

	/**
	 * TO check whether given attribute is inherited or not.
	 * @param abstractAttribute reference to AbstractAttributeInterface
	 * @return true if attribute is inherited.
	 */
	public boolean isInherited(AbstractAttributeInterface abstractAttribute)
	{
		return InheritanceUtil.isInherited(abstractAttribute);
	}

	/**
	 * Returns actual attribute if passed attribute is a derieved one. Else
	 * returns the passed attribute
	 * @param attribute Attribute for which actual attribute is expected.
	 * @return The actual attribute
	 */
	public AttributeInterface getActualAttribute(AttributeInterface attribute)
	{
		return InheritanceUtil.getActualAttribute(attribute);
	}

	/**
	 * Returns actual association if passed association is a derieved one. Else
	 * returns the passed association
	 * @param association Attribute for which actual association is expected.
	 * @return The actual association
	 */
	public AssociationInterface getActualAassociation(AssociationInterface association)
	{
		return InheritanceUtil.getActualAassociation(association);
	}
}
