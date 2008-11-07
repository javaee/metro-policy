/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
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
import java.io.StringReader;
import javax.xml.namespace.QName;
import junit.framework.TestCase;

/**
 *
 * @author Fabian Ritzmann
 */
public class XmlPolicyModelUnmarshallerTest extends TestCase {

    private final static String policy = "<wsp:Policy " +
    "xmlns:wsp=\"http://schemas.xmlsoap.org/ws/2004/09/policy\" " +
    "xmlns:foo=\"http://schemas.foo.com/\">" +
    "<foo:TopLevelAssertion_1 />" +
    "<foo:TopLevelAssertion_2 />" +
    "<foo:TopLevelAssertion_3>" +
    "    <foo:TopLevelAssertion_3_Parameter1/>" +
    "    <foo:TopLevelAssertion_3_Parameter2>" +
    "        <foo:TopLevelAssertion_3_Parameter2_1/>" +
    "        <foo:TopLevelAssertion_3_Parameter2_2/>" +
    "    </foo:TopLevelAssertion_3_Parameter2>" +
    "    <wsp:Policy>" +
    "        <wsp:ExactlyOne>" +
    "            <foo:NestedAssertion_1/>" +
    "            <foo:NestedAssertion_2/>" +
    "        </wsp:ExactlyOne>" +
    "    </wsp:Policy>" +
    "</foo:TopLevelAssertion_3 >" +
    "</wsp:Policy>";

    public XmlPolicyModelUnmarshallerTest(String testName) {
        super(testName);
    }

    /**
     * Test of unmarshalModel method, of class XmlPolicyModelUnmarshaller.
     */
    public void testUnmarshalModel() throws PolicyException {
        StringReader storage = new StringReader(policy);
        XmlPolicyModelUnmarshaller instance = new XmlPolicyModelUnmarshaller();
        PolicySourceModel expResult = PolicySourceModel.createPolicySourceModel(NamespaceVersion.v1_2);
        ModelNode policyRoot = expResult.getRootNode();
        AssertionData assertion1 = AssertionData.createAssertionData(new QName("http://schemas.foo.com/", "TopLevelAssertion_1"));
        policyRoot.createChildAssertionNode(assertion1);
        AssertionData assertion2 = AssertionData.createAssertionData(new QName("http://schemas.foo.com/", "TopLevelAssertion_2"));
        policyRoot.createChildAssertionNode(assertion2);
        AssertionData assertion3 = AssertionData.createAssertionData(new QName("http://schemas.foo.com/", "TopLevelAssertion_3"));
        ModelNode assertion3Node = policyRoot.createChildAssertionNode(assertion3);
        AssertionData parameter1 = AssertionData.createAssertionParameterData(new QName("http://schemas.foo.com/", "TopLevelAssertion_3_Parameter1"));
        assertion3Node.createChildAssertionParameterNode(parameter1);
        AssertionData parameter2 = AssertionData.createAssertionParameterData(new QName("http://schemas.foo.com/", "TopLevelAssertion_3_Parameter2"));
        ModelNode parameter2Node = assertion3Node.createChildAssertionParameterNode(parameter2);
        AssertionData parameter21 = AssertionData.createAssertionParameterData(new QName("http://schemas.foo.com/", "TopLevelAssertion_3_Parameter2_1"));
        parameter2Node.createChildAssertionParameterNode(parameter21);
        AssertionData parameter22 = AssertionData.createAssertionParameterData(new QName("http://schemas.foo.com/", "TopLevelAssertion_3_Parameter2_2"));
        parameter2Node.createChildAssertionParameterNode(parameter22);
        ModelNode nestedPolicy = assertion3Node.createChildPolicyNode();
        ModelNode nestedExactlyOne = nestedPolicy.createChildExactlyOneNode();
        AssertionData nestedAssertion1 = AssertionData.createAssertionData(new QName("http://schemas.foo.com/", "NestedAssertion_1"));
        nestedExactlyOne.createChildAssertionNode(nestedAssertion1);
        AssertionData nestedAssertion2 = AssertionData.createAssertionData(new QName("http://schemas.foo.com/", "NestedAssertion_2"));
        nestedExactlyOne.createChildAssertionNode(nestedAssertion2);
        PolicySourceModel result = instance.unmarshalModel(storage);
        assertEquals(expResult, result);
    }

}
