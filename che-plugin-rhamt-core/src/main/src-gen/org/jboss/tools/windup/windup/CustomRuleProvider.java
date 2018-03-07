/**
 */
package org.jboss.tools.windup.windup;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Custom Rule Provider</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.jboss.tools.windup.windup.CustomRuleProvider#getLocationURI <em>Location URI</em>}</li>
 *   <li>{@link org.jboss.tools.windup.windup.CustomRuleProvider#getWorkspaceResourceLocation <em>Workspace Resource Location</em>}</li>
 * </ul>
 *
 * @see org.jboss.tools.windup.windup.WindupPackage#getCustomRuleProvider()
 * @model
 * @generated
 */
public interface CustomRuleProvider extends EObject {
	/**
	 * Returns the value of the '<em><b>Location URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Location URI</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Location URI</em>' attribute.
	 * @see #setLocationURI(String)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getCustomRuleProvider_LocationURI()
	 * @model
	 * @generated
	 */
	String getLocationURI();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.CustomRuleProvider#getLocationURI <em>Location URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Location URI</em>' attribute.
	 * @see #getLocationURI()
	 * @generated
	 */
	void setLocationURI(String value);

	/**
	 * Returns the value of the '<em><b>Workspace Resource Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Workspace Resource Location</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Workspace Resource Location</em>' attribute.
	 * @see #setWorkspaceResourceLocation(String)
	 * @see org.jboss.tools.windup.windup.WindupPackage#getCustomRuleProvider_WorkspaceResourceLocation()
	 * @model
	 * @generated
	 */
	String getWorkspaceResourceLocation();

	/**
	 * Sets the value of the '{@link org.jboss.tools.windup.windup.CustomRuleProvider#getWorkspaceResourceLocation <em>Workspace Resource Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Workspace Resource Location</em>' attribute.
	 * @see #getWorkspaceResourceLocation()
	 * @generated
	 */
	void setWorkspaceResourceLocation(String value);

} // CustomRuleProvider
