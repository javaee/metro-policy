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

package com.sun.xml.ws.policy.spi;

import com.sun.xml.ws.policy.PolicyAssertion;
import com.sun.xml.ws.policy.sourcemodel.AssertionData;
import com.sun.xml.ws.policy.spi.PolicyAssertionValidator.Fitness;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import javax.xml.namespace.QName;
import junit.framework.TestCase;

/**
 *
 * @author Fabian Ritzmann
 */
public class AbstractQNameValidatorTest extends TestCase {
    
    private AbstractQNameValidator mockValidator;
    
    public AbstractQNameValidatorTest(String testName) {
        super(testName);
    }            

    @Override
    protected void setUp() throws Exception {
        Collection<QName> serverSideAssertions = new ArrayList<QName>();
        Collection<QName> clientSideAssertions = new ArrayList<QName>();
        serverSideAssertions.add(new QName("testns", "testlocal"));
        clientSideAssertions.add(new QName("testclientns", "testclientlocal"));
        mockValidator = new MockQNameValidator(serverSideAssertions, clientSideAssertions);
    }

    /**
     * Test of declareSupportedDomains method, of class AbstractQNameValidator.
     */
    public void testDeclareSupportedDomains() {
        AbstractQNameValidator instance = mockValidator;
        String[] expResult = { "testns", "testclientns" };
        Arrays.sort(expResult);
        String[] result = instance.declareSupportedDomains();
        Arrays.sort(result);
        assertEquals(Arrays.asList(expResult), Arrays.asList(result));
    }

    /**
     * Test of validateClientSide method, of class AbstractQNameValidator.
     */
    public void testValidateClientSide() {
        PolicyAssertion assertion = new MockPolicyAssertion(AssertionData.createAssertionData(new QName("test1", "test2")));
        AbstractQNameValidator instance = mockValidator;
        Fitness expResult = PolicyAssertionValidator.Fitness.UNKNOWN;
        Fitness result = instance.validateClientSide(assertion);
        assertEquals(expResult, result);
    }

    public void testValidateClientSideEmpty() {
        PolicyAssertion assertion = new MockPolicyAssertion(AssertionData.createAssertionData(new QName("test1", "test2")));
        AbstractQNameValidator instance = new MockQNameValidator(null, null);
        Fitness expResult = PolicyAssertionValidator.Fitness.UNKNOWN;
        Fitness result = instance.validateClientSide(assertion);
        assertEquals(expResult, result);
    }

    public void testValidateClientSideSupported() {
        PolicyAssertion assertion = new MockPolicyAssertion(AssertionData.createAssertionData(MockPolicyAssertionValidator.CLIENT_ASSERTION_NAME));
        AbstractQNameValidator instance = new MockPolicyAssertionValidator();
        Fitness expResult = PolicyAssertionValidator.Fitness.SUPPORTED;
        Fitness result = instance.validateClientSide(assertion);
        assertEquals(expResult, result);
    }

    /**
     * Test of validateServerSide method, of class AbstractQNameValidator.
     */
    public void testValidateServerSide() {
        PolicyAssertion assertion = new MockPolicyAssertion(AssertionData.createAssertionData(new QName("testa", "testb")));
        AbstractQNameValidator instance = mockValidator;
        Fitness expResult = PolicyAssertionValidator.Fitness.UNKNOWN;
        Fitness result = instance.validateServerSide(assertion);
        assertEquals(expResult, result);
    }

    public void testValidateServerSideEmpty() {
        PolicyAssertion assertion = new MockPolicyAssertion(AssertionData.createAssertionData(new QName("testa", "testb")));
        AbstractQNameValidator instance = new MockQNameValidator(null, null);
        Fitness expResult = PolicyAssertionValidator.Fitness.UNKNOWN;
        Fitness result = instance.validateServerSide(assertion);
        assertEquals(expResult, result);
    }

    public void testValidateServerSideSupported() {
        PolicyAssertion assertion = new MockPolicyAssertion(AssertionData.createAssertionData(MockPolicyAssertionValidator.SERVER_ASSERTION_NAME));
        AbstractQNameValidator instance = new MockPolicyAssertionValidator();
        Fitness expResult = PolicyAssertionValidator.Fitness.SUPPORTED;
        Fitness result = instance.validateServerSide(assertion);
        assertEquals(expResult, result);
    }

    
    private static class MockQNameValidator extends AbstractQNameValidator {
    
        MockQNameValidator(Collection<QName> serverSideAssertions, Collection<QName> clientSideAssertions) {
            super(serverSideAssertions, clientSideAssertions);
        }

    }
    
    
    private static class MockPolicyAssertion extends PolicyAssertion {
        
        MockPolicyAssertion(AssertionData assertion) {
            super(assertion, null);
        }
    }

}
