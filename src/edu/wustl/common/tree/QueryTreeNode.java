/*L
 * Copyright Washington University in St. Louis, SemanticBits, Persistent Systems, Krishagni.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/wustl-common-package/LICENSE.txt for details.
 */

/*
 * Created on Aug 8, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.common.tree;

import javax.swing.tree.DefaultMutableTreeNode;


/**
 * @author gautam_shetty
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface QueryTreeNode
{
    
    public void initialiseRoot();
    
    public void initialiseRoot(String rootName);
    
    public QueryTreeNode getParentTreeNode();
    
    public boolean isChildOf(QueryTreeNode treeNode);
    
    public boolean hasEqualParents(QueryTreeNode treeNode);
    
    public Object getParentIdentifier();
    
    public Object getIdentifier();
    
    public boolean isPresentIn(DefaultMutableTreeNode parentNode);
    
}
