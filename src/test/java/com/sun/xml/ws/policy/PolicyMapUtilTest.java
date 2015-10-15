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

package com.sun.xml.ws.policy;

import com.sun.xml.ws.policy.sourcemodel.AssertionData;
import com.sun.xml.ws.policy.sourcemodel.ModelNode;
import com.sun.xml.ws.policy.sourcemodel.PolicyModelTranslator;
import com.sun.xml.ws.policy.sourcemodel.PolicySourceModel;
import com.sun.xml.ws.policy.sourcemodel.wspolicy.NamespaceVersion;

import java.util.Arrays;
import javax.xml.namespace.QName;

import junit.framework.TestCase;

/**
 *
 * @author Fabian Ritzmann
 */
public class PolicyMapUtilTest extends TestCase {
    
    public PolicyMapUtilTest(String testName) {
        super(testName);
    }            

    /**
     * Test of rejectAlternatives method, of class PolicyMapUtil.
     */
    public void testRejectAlternativesSimple() throws PolicyException {
        PolicyMapExtender extender = PolicyMapExtender.createPolicyMapExtender();
        PolicyMap map = PolicyMap.createPolicyMap(Arrays.asList(new PolicyMapMutator[] {extender}));        

        PolicySubject subject = new PolicySubject("dummy", Policy.createEmptyPolicy());
        
        PolicyMapKey key = PolicyMap.createWsdlServiceScopeKey(new QName("1"));
        extender.putServiceSubject(key, subject);
        key = PolicyMap.createWsdlServiceScopeKey(new QName("2"));
        extender.putServiceSubject(key, subject);
        
        PolicyMapUtil.rejectAlternatives(map);
    }

    public void testRejectAlternativesComplex() throws PolicyException {
        PolicySourceModel model = PolicySourceModel.createPolicySourceModel(NamespaceVersion.v1_5, "id", null);
        ModelNode root = model.getRootNode();
        ModelNode alternatives = root.createChildExactlyOneNode();
        ModelNode alternative1 = alternatives.createChildAllNode();
        ModelNode alternative2 = alternatives.createChildAllNode();
        AssertionData assertion1 = AssertionData.createAssertionData(new QName("test1", "test1"));
        alternative1.createChildAssertionNode(assertion1);
        AssertionData assertion2 = AssertionData.createAssertionData(new QName("test2", "test2"));
        alternative2.createChildAssertionNode(assertion2);
        PolicyModelTranslator translator = PolicyModelTranslator.getTranslator();
        Policy policy = translator.translate(model);
        
        PolicyMapExtender extender = PolicyMapExtender.createPolicyMapExtender();
        PolicyMap map = PolicyMap.createPolicyMap(Arrays.asList(new PolicyMapMutator[] {extender}));        

        PolicySubject subject = new PolicySubject("dummy", policy);
        
        PolicyMapKey key = PolicyMap.createWsdlServiceScopeKey(new QName("1"));
        extender.putServiceSubject(key, subject);
        key = PolicyMap.createWsdlServiceScopeKey(new QName("2"));
        extender.putServiceSubject(key, subject);

        try {
            PolicyMapUtil.rejectAlternatives(map);
            fail("Expected a PolicyException");
        } catch (PolicyException e) {
        }
    }

}