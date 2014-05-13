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

package com.sun.xml.ws.policy.sourcemodel.wspolicy;

import javax.xml.namespace.QName;
import junit.framework.TestCase;

/**
 *
 * @author Fabian Ritzmann
 */
public class NamespaceVersionTest extends TestCase {
    
    public NamespaceVersionTest(String testName) {
        super(testName);
    }            

    /**
     * Test of resolveVersion method, of class NamespaceVersion.
     */
    public void testResolveVersion_String() {
        String uri = "http://schemas.xmlsoap.org/ws/2004/09/policy";
        NamespaceVersion expResult = NamespaceVersion.v1_2;
        NamespaceVersion result = NamespaceVersion.resolveVersion(uri);
        assertEquals(expResult, result);
    }
    
    public void testResolveVersion_StringNull() {
        String uri = "http://metro.test/";
        NamespaceVersion result = NamespaceVersion.resolveVersion(uri);
        assertNull(result);
    }

    /**
     * Test of resolveVersion method, of class NamespaceVersion.
     */
    public void testResolveVersion_QName() {
        QName name = new QName("http://www.w3.org/ns/ws-policy", "Policy");
        NamespaceVersion expResult = NamespaceVersion.v1_5;
        NamespaceVersion result = NamespaceVersion.resolveVersion(name);
        assertEquals(expResult, result);
    }

    /**
     * Test of getLatestVersion method, of class NamespaceVersion.
     */
    public void testGetLatestVersion() {
        NamespaceVersion expResult = NamespaceVersion.v1_5;
        NamespaceVersion result = NamespaceVersion.getLatestVersion();
        assertEquals(expResult, result);
    }

    /**
     * Test of resolveAsToken method, of class NamespaceVersion.
     */
    public void testResolveAsToken() {
        QName name = new QName("http://www.w3.org/ns/ws-policy", "Policy");
        XmlToken expResult = XmlToken.Policy;
        XmlToken result = NamespaceVersion.resolveAsToken(name);
        assertEquals(expResult, result);
    }
    
    public void testResolveAsTokenWrongNamespace() {
        QName name = new QName("http://metro.test/", "Policy");
        XmlToken expResult = XmlToken.UNKNOWN;
        XmlToken result = NamespaceVersion.resolveAsToken(name);
        assertEquals(expResult, result);
    }

    public void testResolveAsTokenWrongLocalName() {
        QName name = new QName("http://schemas.xmlsoap.org/ws/2004/09/policy", "test");
        XmlToken expResult = XmlToken.UNKNOWN;
        XmlToken result = NamespaceVersion.resolveAsToken(name);
        assertEquals(expResult, result);
    }

    /**
     * Test of getDefaultNamespacePrefix method, of class NamespaceVersion.
     */
    public void testGetDefaultNamespacePrefix() {
        NamespaceVersion instance = NamespaceVersion.v1_2;
        String expResult = "wsp1_2";
        String result = instance.getDefaultNamespacePrefix();
        assertEquals(expResult, result);
    }

    /**
     * Test of asQName method, of class NamespaceVersion.
     */
    public void testAsQName() {
        XmlToken token = XmlToken.ExactlyOne;
        NamespaceVersion instance = NamespaceVersion.v1_5;
        QName expResult = new QName("http://www.w3.org/ns/ws-policy", "ExactlyOne");
        QName result = instance.asQName(token);
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class NamespaceVersion.
     */
    public void testToString() {
        NamespaceVersion instance = NamespaceVersion.v1_2;
        String result = instance.toString();
        assertNotNull(result);
    }

}
