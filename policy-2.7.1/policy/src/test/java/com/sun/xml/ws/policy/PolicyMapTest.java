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

import java.util.Arrays;
import java.util.LinkedList;
import javax.xml.namespace.QName;
import junit.framework.TestCase;

/**
 *
 * @author Marek Potociar (marek.potociar at sun.com)
 */
public class PolicyMapTest extends TestCase {
    
    public PolicyMapTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
    }

    @Override
    protected void tearDown() throws Exception {
    }
    
    public void testCreatePolicyMapWithNullMutatorCollection() throws Exception {
        assertNotNull("Policy map instance should not be null", PolicyMap.createPolicyMap(null));
    }
    
    public void testCreatePolicyMapWithEmptyMutatorCollection() throws Exception {
        assertNotNull("Policy map instance should not be null", PolicyMap.createPolicyMap(new LinkedList<PolicyMapMutator>()));        
    }

    public void testCreatePolicyMapWithNonemptyMutatorCollection() throws Exception {
        assertNotNull("Policy map instance should not be null", PolicyMap.createPolicyMap(Arrays.asList(new PolicyMapMutator[] {PolicyMapExtender.createPolicyMapExtender()})));                
    }
    
    public void testPolicyMapIterator() throws Exception {
        PolicyMapExtender extender = PolicyMapExtender.createPolicyMapExtender();
        PolicyMap map = PolicyMap.createPolicyMap(Arrays.asList(new PolicyMapMutator[] {extender}));        

        PolicySubject subject = new PolicySubject("dummy", Policy.createEmptyPolicy());
        
        PolicyMapKey key = PolicyMap.createWsdlServiceScopeKey(new QName("1"));
        extender.putServiceSubject(key, subject);
        key = PolicyMap.createWsdlServiceScopeKey(new QName("2"));
        extender.putServiceSubject(key, subject);
        
        key = PolicyMap.createWsdlEndpointScopeKey(new QName("3"), new QName("port"));
        extender.putEndpointSubject(key, subject);
        key = PolicyMap.createWsdlEndpointScopeKey(new QName("4"), new QName("port"));
        extender.putEndpointSubject(key, subject);
        key = PolicyMap.createWsdlEndpointScopeKey(new QName("5"), new QName("port"));
        extender.putEndpointSubject(key, subject);

        key = PolicyMap.createWsdlMessageScopeKey(new QName("6"), new QName("port"), new QName("operation"));
        extender.putInputMessageSubject(key, subject);

        key = PolicyMap.createWsdlMessageScopeKey(new QName("7"), new QName("port"), new QName("operation"));
        extender.putOutputMessageSubject(key, subject);
        
        key = PolicyMap.createWsdlMessageScopeKey(new QName("8"), new QName("port"), new QName("operation"));
        extender.putFaultMessageSubject(key, subject);
        
        int counter = 0;
        for (Policy policy : map) {
            counter++;
        }
        
        assertEquals("Did not iterate over expected number of policies.", 8, counter);        
    }
    
    public void testIsEmpty() {
        PolicyMap map = PolicyMap.createPolicyMap(null);
        assertTrue(map.isEmpty());
        
        map = PolicyMap.createPolicyMap(new LinkedList<PolicyMapMutator>());
        assertTrue(map.isEmpty());
        
        map = PolicyMap.createPolicyMap(Arrays.asList(new PolicyMapMutator[] {PolicyMapExtender.createPolicyMapExtender()}));
        assertTrue(map.isEmpty());
        
        PolicyMapExtender extender = PolicyMapExtender.createPolicyMapExtender();
        PolicyMapMutator[] mutators = new PolicyMapMutator[] {extender};
        PolicyMapKey key = PolicyMap.createWsdlServiceScopeKey(new QName("service"));
        map = PolicyMap.createPolicyMap(Arrays.asList(mutators));
        extender.putServiceSubject(key, null);
        assertFalse(map.isEmpty());

        mutators[0].disconnect();
        key = PolicyMap.createWsdlEndpointScopeKey(new QName("service"), new QName("port"));
        map = PolicyMap.createPolicyMap(Arrays.asList(new PolicyMapMutator[] {extender}));
        extender.putEndpointSubject(key, null);
        assertFalse(map.isEmpty());

        mutators[0].disconnect();
        key = PolicyMap.createWsdlOperationScopeKey(new QName("service"), new QName("port"), new QName("operation"));
        map = PolicyMap.createPolicyMap(Arrays.asList(new PolicyMapMutator[] {extender}));
        extender.putOperationSubject(key, null);
        assertFalse(map.isEmpty());

        mutators[0].disconnect();
        key = PolicyMap.createWsdlMessageScopeKey(new QName("service"), new QName("port"), new QName("operation"));
        map = PolicyMap.createPolicyMap(Arrays.asList(new PolicyMapMutator[] {extender}));
        extender.putInputMessageSubject(key, null);
        assertFalse(map.isEmpty());

        mutators[0].disconnect();
        key = PolicyMap.createWsdlMessageScopeKey(new QName("service"), new QName("port"), new QName("operation"));
        map = PolicyMap.createPolicyMap(Arrays.asList(new PolicyMapMutator[] {extender}));
        extender.putOutputMessageSubject(key, null);
        assertFalse(map.isEmpty());

        mutators[0].disconnect();
        key = PolicyMap.createWsdlMessageScopeKey(new QName("service"), new QName("port"), new QName("operation"));
        map = PolicyMap.createPolicyMap(Arrays.asList(new PolicyMapMutator[] {extender}));
        extender.putFaultMessageSubject(key, null);
        assertFalse(map.isEmpty());
    }
    
}
