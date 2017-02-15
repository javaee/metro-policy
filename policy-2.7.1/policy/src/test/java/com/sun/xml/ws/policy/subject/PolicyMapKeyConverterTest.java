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

package com.sun.xml.ws.policy.subject;

import com.sun.xml.ws.policy.PolicyMap;
import com.sun.xml.ws.policy.PolicyMapKey;
import javax.xml.namespace.QName;
import junit.framework.TestCase;

/**
 *
 * @author Fabian Ritzmann
 */
public class PolicyMapKeyConverterTest extends TestCase {
    
    private QName serviceName;
    private QName portName;
    private QName bindingName;
    private QName operationName;
    private QName portTypeName;
    private QName faultName;
    private QName messageName;
    private PolicyMapKeyConverter converter;

    public PolicyMapKeyConverterTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        serviceName = new QName("namesp", "service");
        portName = new QName("namesp", "port");
        bindingName = new QName("namesp", "binding");
        operationName = new QName("namesp", "bindingoperation");
        portTypeName = new QName("namesp", "porttype");
        faultName = new QName("namesp", "fault");
        messageName = new QName("namesp", "message");
        this.converter = new PolicyMapKeyConverter(serviceName, portName);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getPolicyMapKey method, of class PolicyMapKeyConverter.
     */
    public void testGetPolicyMapKeyBinding() {
        WsdlBindingSubject subject = WsdlBindingSubject.createBindingSubject(bindingName);
        PolicyMapKey expResult = PolicyMap.createWsdlEndpointScopeKey(serviceName, portName);
        PolicyMapKey result = converter.getPolicyMapKey(subject);
        assertEquals(expResult, result);
    }

    /**
     * Test of getPolicyMapKey method, of class PolicyMapKeyConverter.
     */
    public void testGetPolicyMapKeyBindingOperation() {
        WsdlBindingSubject subject = WsdlBindingSubject.createBindingOperationSubject(bindingName, operationName);
        PolicyMapKey expResult = PolicyMap.createWsdlOperationScopeKey(serviceName, portName, operationName);
        PolicyMapKey result = converter.getPolicyMapKey(subject);
        assertEquals(expResult, result);
    }

    /**
     * Test of getPolicyMapKey method, of class PolicyMapKeyConverter.
     */
    public void testGetPolicyMapKeyBindingInput() {
        WsdlBindingSubject subject = WsdlBindingSubject.createBindingMessageSubject(bindingName, operationName, null, WsdlBindingSubject.WsdlMessageType.INPUT);
        PolicyMapKey expResult = PolicyMap.createWsdlMessageScopeKey(serviceName, portName, operationName);
        PolicyMapKey result = converter.getPolicyMapKey(subject);
        assertEquals(expResult, result);
    }

    /**
     * Test of getPolicyMapKey method, of class PolicyMapKeyConverter.
     */
    public void testGetPolicyMapKeyBindingOutput() {
        WsdlBindingSubject subject = WsdlBindingSubject.createBindingMessageSubject(bindingName, operationName, null, WsdlBindingSubject.WsdlMessageType.OUTPUT);
        PolicyMapKey expResult = PolicyMap.createWsdlMessageScopeKey(serviceName, portName, operationName);
        PolicyMapKey result = converter.getPolicyMapKey(subject);
        assertEquals(expResult, result);
    }

    /**
     * Test of getPolicyMapKey method, of class PolicyMapKeyConverter.
     */
    public void testGetPolicyMapKeyBindingFault() {
        WsdlBindingSubject subject = WsdlBindingSubject.createBindingMessageSubject(bindingName, operationName, faultName, WsdlBindingSubject.WsdlMessageType.FAULT);
        PolicyMapKey expResult = PolicyMap.createWsdlFaultMessageScopeKey(serviceName, portName, operationName, faultName);
        PolicyMapKey result = converter.getPolicyMapKey(subject);
        assertEquals(expResult, result);
    }

}
