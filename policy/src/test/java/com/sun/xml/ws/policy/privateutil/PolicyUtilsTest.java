/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2013 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.xml.ws.policy.privateutil;

import com.sun.xml.ws.policy.PolicyException;
import com.sun.xml.ws.policy.spi.PolicyAssertionCreator;

import java.io.Closeable;
import javax.xml.stream.XMLStreamReader;

import junit.framework.TestCase;

/**
 *
 * @author Marek Potociar (marek.potociar at sun.com)
 */
public class PolicyUtilsTest extends TestCase {
    
    public PolicyUtilsTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
    }
    
    @Override
    protected void tearDown() throws Exception {
    }

    /**
     * Test of getStackMethodName method, of class com.sun.xml.ws.policy.privateutil.PolicyUtils.Commons.
     */
    public void testCommonsGetStackMethodName() {
        int index;
        String expResult, result;
        
        index = 0;
        result = PolicyUtils.Commons.getStackMethodName(index);
        // On Mac OS X, getStackMethodName returns getStackTrace. On other systems,
        // this method first returns dumpThreads and then getStackTrace.
        if (result.equals("dumpThreads")) {
            index++;
            expResult = "getStackTrace";
            result = PolicyUtils.Commons.getStackMethodName(index);
            assertEquals(expResult, result);
        }
        else if (!result.equals("getStackTrace")) {
            fail("Expected \"dumpThreads\" or \"getStackTrace\", but got instead \"" + result + "\"");
        }
        
        index++;
        expResult = "getStackMethodName";
        result = PolicyUtils.Commons.getStackMethodName(index);
        assertEquals(expResult, result);
        
        index++;
        expResult = "testCommonsGetStackMethodName";
        result = PolicyUtils.Commons.getStackMethodName(index);
        assertEquals(expResult, result);
    }
    
    public void testGetCallerMethodName() {
        class TestCall {
            public void testCall() {
                String expResult, result;
                
                expResult = "testGetCallerMethodName";
                result = PolicyUtils.Commons.getCallerMethodName();
                assertEquals(expResult, result);                
            }
        }
        
        TestCall tc = new TestCall();
        tc.testCall();
    }
    
    /**
     * Test of closeResource method, of class com.sun.xml.ws.policy.privateutil.PolicyUtils.IO.
     */
    public void testIOCloseResourceCloseableNull() {
        PolicyUtils.IO.closeResource((Closeable) null);
    }
    
    /**
     * Test of closeResource method, of class com.sun.xml.ws.policy.privateutil.PolicyUtils.IO.
     */
    public void testIOCloseResourceCloseable() {
        final MockCloseable closeable = new MockCloseable();
        PolicyUtils.IO.closeResource(closeable);
        assertTrue(closeable.isClosed());
    }

    /**
     * Test of closeResource method, of class com.sun.xml.ws.policy.privateutil.PolicyUtils.IO.
     */
    public void testIOCloseResourceXMLStreamReaderNull() {
        PolicyUtils.IO.closeResource((XMLStreamReader) null);
    }

    /**
     * Test of createIndent method, of class com.sun.xml.ws.policy.privateutil.PolicyUtils.Text.
     */
    public void testTextCreateIndent() {
        int indentLevel;
        String expResult, result;
        
        indentLevel = 0;
        expResult = "";
        result = PolicyUtils.Text.createIndent(indentLevel);
        assertEquals(expResult, result);
        
        
        indentLevel = 1;
        expResult = "    ";
        result = PolicyUtils.Text.createIndent(indentLevel);
        assertEquals(expResult, result);
        
        indentLevel = 2;
        expResult = "        ";
        result = PolicyUtils.Text.createIndent(indentLevel);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of compareBoolean method, of class com.sun.xml.ws.policy.privateutil.PolicyUtils.Comparison.
     */
    public void testComparisonCompareBoolean() {
        boolean b1, b2;
        int expResult, result;
        
        b1 = true;
        b2 = true;
        expResult = 0;
        result = PolicyUtils.Comparison.compareBoolean(b1, b2);
        assertEquals(expResult, result);
        
        b1 = false;
        b2 = false;
        expResult = 0;
        result = PolicyUtils.Comparison.compareBoolean(b1, b2);
        assertEquals(expResult, result);
        
        b1 = false;
        b2 = true;
        expResult = -1;
        result = PolicyUtils.Comparison.compareBoolean(b1, b2);
        assertEquals(expResult, result);
        
        b1 = true;
        b2 = false;
        expResult = 1;
        result = PolicyUtils.Comparison.compareBoolean(b1, b2);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of compareNullableStrings method, of class com.sun.xml.ws.policy.privateutil.PolicyUtils.Comparison.
     */
    public void testComparisonCompareNullableStrings() {
        String s1, s2;
        int expResult, result;
        
        s1 = null;
        s2 = null;
        expResult = 0;
        result = PolicyUtils.Comparison.compareNullableStrings(s1, s2);
        assertEquals(expResult, result);
        
        s1 = "";
        s2 = "";
        expResult = 0;
        result = PolicyUtils.Comparison.compareNullableStrings(s1, s2);
        assertEquals(expResult, result);
        
        s1 = "abc";
        s2 = "abc";
        expResult = 0;
        result = PolicyUtils.Comparison.compareNullableStrings(s1, s2);
        assertEquals(expResult, result);
        
        s1 = null;
        s2 = "";
        expResult = -1;
        result = PolicyUtils.Comparison.compareNullableStrings(s1, s2);
        assertEquals(expResult, result);
        
        s1 = null;
        s2 = "abc";
        expResult = -1;
        result = PolicyUtils.Comparison.compareNullableStrings(s1, s2);
        assertEquals(expResult, result);
        
        s1 = "abc";
        s2 = "abd";
        expResult = -1;
        result = PolicyUtils.Comparison.compareNullableStrings(s1, s2);
        assertEquals(expResult, result);
        
        s1 = "";
        s2 = null;
        expResult = 1;
        result = PolicyUtils.Comparison.compareNullableStrings(s1, s2);
        assertEquals(expResult, result);
        
        s1 = "abc";
        s2 = null;
        expResult = 1;
        result = PolicyUtils.Comparison.compareNullableStrings(s1, s2);
        assertEquals(expResult, result);
        
        s1 = "abd";
        s2 = "abc";
        expResult = 1;
        result = PolicyUtils.Comparison.compareNullableStrings(s1, s2);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of combine method, of class com.sun.xml.ws.policy.privateutil.PolicyUtils.Collections.
     */
    public void testCollectionsCombine() {
        // TODO review the generated test code and remove the default call to fail.
        // fail("The test case is a prototype.");
    }
    
    /**
     * Test of invoke method, of class com.sun.xml.ws.policy.privateutil.PolicyUtils.Reflection.
     */
    public void testReflectionInvoke() {
        final Object target = new NamedObject();
        final String result = PolicyUtils.Reflection.invoke(target, "hum", String.class, null, null);
        assertEquals("hum", result);
    }
    
    /**
     * Test of invoke method, of class com.sun.xml.ws.policy.privateutil.PolicyUtils.Reflection.
     */
    public void testReflectionInvokeFail() {
        final Object target = new Object() { public String hum() { return "hum"; } };
        try {
            final String result = PolicyUtils.Reflection.invoke(target, "humv", String.class, null, null);
            fail("Expected RuntimePolicyUtilsException, instead got " + result);
        } catch (RuntimePolicyUtilsException e) {
            // expected
        }
    }

    /**
     * Test of generateFullName method, of class com.sun.xml.ws.policy.privateutil.PolicyUtils.ConfigFile.
     * @throws PolicyException
     */
    public void testConfigFileGenerateFullName() throws PolicyException {
        String configFileIdentifier = "test";
        
        String expResult = "wsit-test.xml";
        String result = PolicyUtils.ConfigFile.generateFullName(configFileIdentifier);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of generateFullName method, of class com.sun.xml.ws.policy.privateutil.PolicyUtils.ConfigFile.
     */
    public void testConfigFileGenerateFullNameNull() {
        String configFileIdentifier = null;
        try {
           String result = PolicyUtils.ConfigFile.generateFullName(configFileIdentifier);
           fail("Expected PolicyException, got result = " + result);
        } catch (PolicyException e) {
            // expected an exception
        }
    }
    
    /**
     * Test of load method, of class com.sun.xml.ws.policy.privateutil.PolicyUtils.ServiceProvider.
     */
    public void testServiceProviderLoad() {
        PolicyAssertionCreator[] result = PolicyUtils.ServiceProvider.load(PolicyAssertionCreator.class, this.getClass().getClassLoader());
        assertEquals(1, result.length);
        assertEquals(MockPolicyAssertionCreator.class, result[0].getClass());
    }
    
    public void testRtf2396Unquote() {
        assertEquals("hello Vasku", PolicyUtils.Rfc2396.unquote("hello%20Vasku"));
    }
    
    public void testRtf2396UnquoteCutOff() {
        try {
            final String result = PolicyUtils.Rfc2396.unquote("hello%2");
            fail("Expected RuntimePolicyUtilsException, got " + result);
        } catch (RuntimePolicyUtilsException e) {
            // expected
        }
    }


    private class MockCloseable implements Closeable {

        private boolean isClosed = false;

        public void close() {
            isClosed = true;
        }

        public boolean isClosed() {
            return isClosed;
        }

    }

    public static class NamedObject {
        public String hum() { return "hum"; };
    }

}
