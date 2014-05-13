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

import com.sun.xml.ws.policy.subject.WsdlBindingSubject.WsdlMessageType;
import javax.xml.namespace.QName;
import junit.framework.TestCase;

/**
 *
 * @author Fabian Ritzmann
 */
public class WsdlBindingSubjectTest extends TestCase {

    private QName serviceName;
    private QName portName;

    public WsdlBindingSubjectTest(String testName) {
        super(testName);
    }            

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        serviceName = new QName("namesp", "service");
        portName = new QName("namesp", "port");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of createBindingSubject method, of class WsdlRoot.
     */
    public void testCreateBindingSubject() {
        QName bindingName = new QName("namesp", "binding");
        WsdlBindingSubject result = WsdlBindingSubject.createBindingSubject(bindingName);
        assertEquals(bindingName, result.getName());
    }

    /**
     * Test of createBindingOperationSubject method, of class WsdlRoot.
     */
    public void testCreateBindingOperationSubject() {
        QName bindingName = new QName("namesp", "binding");
        QName operationName = new QName("namesp", "operation");
        WsdlBindingSubject result = WsdlBindingSubject.createBindingOperationSubject(bindingName, operationName);
        assertEquals(operationName, result.getName());
    }

    /**
     * Test of createBindingMessageSubject method, of class WsdlRoot.
     */
    public void testCreateBindingMessageSubjectInput() {
        QName bindingName = new QName("namesp", "binding");
        QName operationName = new QName("namesp", "operation");
        QName messageName = new QName("namesp", "message");
        WsdlMessageType messageType = WsdlMessageType.INPUT;
        WsdlBindingSubject result = WsdlBindingSubject.createBindingMessageSubject(bindingName, operationName, messageName, messageType);
        assertEquals(messageName, result.getName());
    }

    /**
     * Test of createBindingMessageSubject method, of class WsdlRoot.
     */
    public void testCreateBindingMessageSubjectOutput() {
        QName bindingName = new QName("namesp", "binding");
        QName operationName = new QName("namesp", "operation");
        QName messageName = new QName("namesp", "message");
        WsdlMessageType messageType = WsdlMessageType.OUTPUT;
        WsdlBindingSubject result = WsdlBindingSubject.createBindingMessageSubject(bindingName, operationName, messageName, messageType);
        assertEquals(messageName, result.getName());
    }

    /**
     * Test of createBindingMessageSubject method, of class WsdlRoot.
     */
    public void testCreateBindingMessageSubjectFault() {
        QName bindingName = new QName("namesp", "binding");
        QName operationName = new QName("namesp", "operation");
        QName messageName = new QName("namesp", "message");
        WsdlMessageType messageType = WsdlMessageType.FAULT;
        WsdlBindingSubject result = WsdlBindingSubject.createBindingMessageSubject(bindingName, operationName, messageName, messageType);
        assertEquals(messageName, result.getName());
    }

    /**
     * Test of createBindingMessageSubject method, of class WsdlRoot.
     */
    public void testCreateBindingMessageSubjectNoMessage() {
        QName bindingName = new QName("namesp", "binding");
        QName operationName = new QName("namesp", "operation");
        QName messageName = new QName("namesp", "message");
        WsdlMessageType messageType = WsdlMessageType.NO_MESSAGE;
        try {
            WsdlBindingSubject result = WsdlBindingSubject.createBindingMessageSubject(bindingName, operationName, messageName, messageType);
            fail("Expected an IllegalArgumentException, instead got result = " + result);
        } catch (IllegalArgumentException e) {
            // Expected this exception
        }
    }

    /**
     * Test of createBindingMessageSubject method, of class WsdlRoot.
     */
    public void testCreateBindingMessageSubjectNull() {
        QName bindingName = new QName("namesp", "binding");
        QName operationName = new QName("namesp", "operation");
        QName messageName = new QName("namesp", "message");
        WsdlMessageType messageType = null;
        try {
            WsdlBindingSubject result = WsdlBindingSubject.createBindingMessageSubject(bindingName, operationName, messageName, messageType);
            fail("Expected an IllegalArgumentException, instead got result = " + result);
        } catch (IllegalArgumentException e) {
            // Expected this exception
        }
    }

    /**
     * Test of getMessageType method, of class WsdlSubject.
     */
    public void testGetMessageTypeOutput() {
        QName bindingName = new QName("namesp", "binding");
        QName operationName = new QName("namesp", "operation");
        QName messageName = new QName("namesp", "message");
        WsdlMessageType expResult = WsdlMessageType.OUTPUT;
        WsdlBindingSubject instance = WsdlBindingSubject.createBindingMessageSubject(bindingName, operationName, messageName, expResult);
        WsdlMessageType result = instance.getMessageType();
        assertEquals(expResult, result);
    }

    /**
     * Test of isBindingSubject method, of class WsdlSubject.
     */
    public void testIsBindingSubject() {
        QName bindingName = new QName("namesp", "binding");
        WsdlBindingSubject instance = WsdlBindingSubject.createBindingSubject(bindingName);
        boolean result = instance.isBindingSubject();
        assertTrue(result);
    }

    /**
     * Test of isBindingOperationSubject method, of class WsdlSubject.
     */
    public void testIsBindingOperationSubject() {
        QName bindingName = new QName("namesp", "binding");
        QName operationName = new QName("namesp", "operation");
        WsdlBindingSubject instance = WsdlBindingSubject.createBindingOperationSubject(bindingName, operationName);
        boolean result = instance.isBindingOperationSubject();
        assertTrue(result);
    }

    /**
     * Test of isBindingMessageSubject method, of class WsdlSubject.
     */
    public void testIsBindingMessageSubject() {
        QName bindingName = new QName("namesp", "binding");
        QName operationName = new QName("namesp", "operation");
        QName messageName = new QName("namesp", "message");
        WsdlMessageType messageType = WsdlMessageType.INPUT;
        WsdlBindingSubject instance = WsdlBindingSubject.createBindingMessageSubject(bindingName, operationName, messageName, messageType);
        boolean result = instance.isBindingMessageSubject();
        assertTrue(result);
    }

    /**
     * Test of equals method, of class WsdlSubject.
     */
    public void testEqualsEqual() {
        QName portTypeName = new QName("namesp", "porttype");
        QName operationName = new QName("namesp", "operation");
        QName messageName = new QName("namesp", "message");
        WsdlMessageType messageType = WsdlMessageType.OUTPUT;
        Object that = WsdlBindingSubject.createBindingMessageSubject(portTypeName, operationName, messageName, messageType);
        WsdlBindingSubject instance = WsdlBindingSubject.createBindingMessageSubject(portTypeName, operationName, messageName, messageType);
        assertEquals(instance, that);
    }

    /**
     * Test of equals method, of class WsdlSubject.
     */
    public void testEqualsNotEqual() {
        QName portTypeName = new QName("namesp", "porttype");
        QName operationName = new QName("namesp", "operation");
        QName messageName = new QName("namesp", "message");
        Object that = WsdlBindingSubject.createBindingMessageSubject(portTypeName, operationName, messageName, WsdlMessageType.OUTPUT);
        WsdlBindingSubject instance = WsdlBindingSubject.createBindingMessageSubject(portTypeName, operationName, messageName, WsdlMessageType.INPUT);
        boolean result = instance.equals(that);
        assertFalse(result);
    }

    /**
     * Test of hashCode method, of class WsdlSubject.
     */
    public void testHashCode() {
        QName bindingName = new QName("namesp", "binding");
        QName operationName = new QName("namesp", "operation");
        WsdlBindingSubject instance1 = WsdlBindingSubject.createBindingOperationSubject(bindingName, operationName);
        WsdlBindingSubject instance2 = WsdlBindingSubject.createBindingOperationSubject(bindingName, operationName);
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }

    /**
     * Test of toString method, of class WsdlSubject.
     */
    public void testToString() {
        QName bindingName = new QName("namesp", "binding");
        QName operationName = new QName("namesp", "operation");
        WsdlBindingSubject instance = WsdlBindingSubject.createBindingOperationSubject(bindingName, operationName);
        String result = instance.toString();
        assertNotNull(result);
    }

}
