/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2012 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.xml.ws.policy.sourcemodel;

import com.sun.xml.ws.policy.PolicyException;
import com.sun.xml.ws.policy.sourcemodel.wspolicy.NamespaceVersion;
import com.sun.xml.ws.policy.testutils.PolicyResourceLoader;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import javax.xml.namespace.QName;

import junit.framework.TestCase;

/**
 *
 * @author Fabian Ritzmann
 */
public class PolicySourceModelTest extends TestCase {
    
    public PolicySourceModelTest(String testName) {
        super(testName);
    }

    /**
     * Test of createPolicySourceModel method, of class PolicySourceModel.
     */
    public void testCreatePolicySourceModel_NamespaceVersion12() {
        NamespaceVersion nsVersion = NamespaceVersion.v1_2;
        NamespaceVersion expResult = NamespaceVersion.v1_2;
        PolicySourceModel result = PolicySourceModel.createPolicySourceModel(nsVersion);
        assertEquals(expResult, result.getNamespaceVersion());
    }

    /**
     * Test of createPolicySourceModel method, of class PolicySourceModel.
     */
    public void testCreatePolicySourceModel_NamespaceVersion15() {
        NamespaceVersion nsVersion = NamespaceVersion.v1_5;
        NamespaceVersion expResult = NamespaceVersion.v1_5;
        PolicySourceModel result = PolicySourceModel.createPolicySourceModel(nsVersion);
        assertEquals(expResult, result.getNamespaceVersion());
    }

    /**
     * Test of getNamespaceToPrefixMapping method, of class PolicySourceModel.
     * @throws PolicyException 
     */
    public void testGetNamespaceToPrefixMapping() throws PolicyException {
        PolicySourceModel instance = PolicySourceModel.createPolicySourceModel(NamespaceVersion.getLatestVersion(), "policy-id", null);
        ModelNode root = instance.getRootNode();
        QName name = new QName("http://java.sun.com/xml/ns/wsit/policy", "local");
        AssertionData assertion = AssertionData.createAssertionData(name);
        ModelNode childNode = root.createChildAssertionNode(assertion);
        AssertionData assertionChild = AssertionData.createAssertionData(name);
        childNode.createChildAssertionParameterNode(assertionChild);
        Map<String, String> result = instance.getNamespaceToPrefixMapping();
        assertEquals("wsp", result.get("http://www.w3.org/ns/ws-policy"));
        assertEquals("wsu", result.get("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd"));
        assertEquals("sunwsp", result.get("http://java.sun.com/xml/ns/wsit/policy"));
    }

    public void testEqualsDifferentClass() {
        PolicySourceModel instance = PolicySourceModel.createPolicySourceModel(NamespaceVersion.v1_2);
        assertFalse(instance.equals(new Object()));
    }

    public void testEqualsSameObject() {
        PolicySourceModel instance = PolicySourceModel.createPolicySourceModel(NamespaceVersion.v1_2);
        assertTrue(instance.equals(instance));
    }

    public void testEqualsDifferentObject() {
        PolicySourceModel instance = PolicySourceModel.createPolicySourceModel(NamespaceVersion.v1_2);
        ModelNode root = instance.getRootNode();
        QName name = new QName("http://java.sun.com/xml/ns/wsit/policy", "local");
        AssertionData assertion = AssertionData.createAssertionData(name);
        root.createChildAssertionNode(assertion);

        PolicySourceModel instance2 = PolicySourceModel.createPolicySourceModel(NamespaceVersion.v1_2);
        ModelNode root2 = instance2.getRootNode();
        QName name2 = new QName("http://java.sun.com/xml/ns/wsit/policy", "local2");
        AssertionData assertion2 = AssertionData.createAssertionData(name2);
        root2.createChildAssertionNode(assertion2);

        assertFalse(instance.equals(instance2));
    }

    public void testToString() {
        PolicySourceModel instance = PolicySourceModel.createPolicySourceModel(NamespaceVersion.v1_2);
        assertNotNull(instance.toString());
    }

    public void testContainsPolicyReferences() {
        PolicySourceModel instance = PolicySourceModel.createPolicySourceModel(NamespaceVersion.v1_2);
        assertFalse(instance.containsPolicyReferences());
    }

    public void testExpand() throws URISyntaxException, PolicyException {
        PolicySourceModel referringModel = PolicySourceModel.createPolicySourceModel(NamespaceVersion.v1_5);
        ModelNode root = referringModel.getRootNode();
        URI policyReference = new URI("#local");
        PolicyReferenceData referenceData = new PolicyReferenceData(policyReference);
        ModelNode referenceNode = root.createChildPolicyReferenceNode(referenceData);

        PolicySourceModel referredModel = PolicySourceModel.createPolicySourceModel(NamespaceVersion.v1_2, "local", null);
        ModelNode root2 = referredModel.getRootNode();
        QName name = new QName("http://java.sun.com/xml/ns/wsit/policy", "local");
        AssertionData assertion = AssertionData.createAssertionData(name);
        root2.createChildAssertionNode(assertion);

        PolicySourceModelContext context = PolicySourceModelContext.createContext();
        context.addModel(policyReference, referredModel);

        assertNull(referenceNode.getReferencedModel());
        referringModel.expand(context);
        assertNotNull(referenceNode.getReferencedModel());
    }

    public void testCloneModel() throws Exception {
        PolicySourceModel model = PolicyResourceLoader.unmarshallModel("complex_policy/nested_assertions_with_alternatives.xml");
        PolicySourceModel clone = model.clone();

        model.toString();
        clone.toString();
        assertEquals(model, clone);
    }

}
