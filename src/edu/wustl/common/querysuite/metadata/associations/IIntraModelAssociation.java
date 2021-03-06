/*L
 * Copyright Washington University in St. Louis, SemanticBits, Persistent Systems, Krishagni.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/wustl-common-package/LICENSE.txt for details.
 */

/**
 * 
 */

package edu.wustl.common.querysuite.metadata.associations;

import edu.common.dynamicextensions.domaininterface.AssociationInterface;

/**
 * @author prafull_kadam
 * Interface to represent Intra model Association.
 */
public interface IIntraModelAssociation extends IAssociation
{

	/**
	 * To get the Dynamic Extenstion Association correpsonding to this class
	 * @return the reference to the Dynamic Extension Attribute.
	 */
	AssociationInterface getDynamicExtensionsAssociation();
	
	/**
	 * @return intramodel association wrapping de association that is reverse of this association.
	 * @throws java.lang.IllegalArgumentException if this association is not bidirectional.
	 */
	public IIntraModelAssociation reverse();
}
