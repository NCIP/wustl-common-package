/*L
 * Copyright Washington University in St. Louis, SemanticBits, Persistent Systems, Krishagni.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/wustl-common-package/LICENSE.txt for details.
 */

package edu.wustl.common.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * Class intercepts the struts action call and performs authorization to ensure
 * the user has an EXECUTE privilege on the subclassing Action. If the User does
 * not have the EXECUTE privilege then access is denied to execute the business
 * logic. No coding is needed to implement this authorization. To implement this
 * solution in the authorization schema: 1. Create a protection element with the
 * subclassing action's fully qualified class name (e.g.,
 * gov.nih.nci.action.MyAction ) 2. Create a protection group(s) and assoicate
 * the element from step one. 3. Create a privilege named 'EXECUTE' and assign
 * it to the target role. 4. Associate the user or user_group with the
 * protection group in the context of the role created in step 3. 5. Subclass
 * the SecureAction and implement executeSecureWorkflow
 * 
 * @author Aarti Sharma
 *  
 */
public abstract class SecureAction extends BaseAction
{
    /*
     * Authorizes the user and executes the secure workflow. If authorization
     * fails, the user is denied access to the secured action
     * 
     */
    protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        if (isAuthorizedToExecute(request))
        {
        	//call to check AddNew operation
            checkAddNewOperation(request);
            
            return executeSecureAction(mapping, form, request, response);
        }

        Logger.out.debug("The Access was denied for the User "+ getUserLoginName(request)
                + "to Execute this Action "+this.getClass().getName());

        ActionErrors errors = new ActionErrors();

        ActionError error = new ActionError("access.execute.action.denied");
        errors.add(ActionErrors.GLOBAL_ERROR, error);
        saveErrors(request, errors);

        return getActionForward(request,mapping);
    }

    /**
     * @param mapping
     * @return
     */
    protected ActionForward getActionForward(HttpServletRequest request,ActionMapping mapping)
    {
        return mapping.findForward(Constants.ACCESS_DENIED);
    }

    /**
     * @param request
     * @return
     * @throws Exception
     */
    protected boolean isAuthorizedToExecute(HttpServletRequest request) throws Exception
    {
        return SecurityManager.getInstance(this.getClass())
                .isAuthorizedToExecuteAction(getUserLoginName(request),getObjectIdForSecureMethodAccess(request));
    }
    
    /**
     * Returns the object id of the protection element that represents
     * the Action that is being requested for invocation.
     * @param clazz
     * @return
     */
    protected String getObjectIdForSecureMethodAccess(HttpServletRequest request)
    {
        return this.getClass().getName();
    }

    /**
     * Subclasses should implement this method to execute the Action logic.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    protected abstract ActionForward executeSecureAction(ActionMapping mapping,
            ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception;
}