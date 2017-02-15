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

import com.sun.xml.txw2.TXW;
import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.output.DomSerializer;
import com.sun.xml.ws.policy.PolicyConstants;
import com.sun.xml.ws.policy.PolicyException;
import com.sun.xml.ws.policy.sourcemodel.wspolicy.NamespaceVersion;
import java.io.StringWriter;
import java.util.Collection;
import java.util.LinkedList;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import junit.framework.TestCase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 *
 * @author Fabian Ritzmann
 */
public class PolicyModelMarshallerTest extends TestCase {

    private DocumentBuilder builder;
    private XMLOutputFactory factory;


    public PolicyModelMarshallerTest(String testName) {
        super(testName);
    }            

    @Override
    protected void setUp() throws ParserConfigurationException {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        documentFactory.setNamespaceAware(true);
        builder = documentFactory.newDocumentBuilder();
        factory = XMLOutputFactory.newInstance();
    }

    /**
     * Test of getXmlMarshaller method, of class PolicyModelMarshaller.
     */
    public void testGetXmlMarshallerVisible() {
        boolean marshallInvisible = false;
        PolicyModelMarshaller result = PolicyModelMarshaller.getXmlMarshaller(marshallInvisible);
        assertNotNull(result);
    }

    /**
     * Test of getXmlMarshaller method, of class PolicyModelMarshaller.
     */
    public void testGetXmlMarshallerInvisible() {
        boolean marshallInvisible = true;
        PolicyModelMarshaller result = PolicyModelMarshaller.getXmlMarshaller(marshallInvisible);
        assertNotNull(result);
    }

    /**
     * Test of marshal method, of class PolicyModelMarshaller.
     * @throws PolicyException
     */
    public void testMarshal_PolicySourceModel_Object() throws PolicyException {
        PolicySourceModel model = PolicySourceModel.createPolicySourceModel(NamespaceVersion.v1_2);
        Document document = builder.newDocument();
        TypedXmlWriter storage = TXW.create(new QName("root"), TypedXmlWriter.class, new DomSerializer(document));
        PolicyModelMarshaller instance = PolicyModelMarshaller.getXmlMarshaller(false);
        instance.marshal(model, storage);
        storage.commit();
        Element element = document.getDocumentElement();
        Node policyElement = element.getFirstChild();
        assertEquals(policyElement.getLocalName(), "Policy");
    }

    /**
     * Test of marshal method, of class PolicyModelMarshaller.
     * @throws PolicyException
     */
    public void testMarshal_Collection_Object() throws PolicyException {
        Collection<PolicySourceModel> models = new LinkedList<PolicySourceModel>();
        PolicySourceModel model = PolicySourceModel.createPolicySourceModel(NamespaceVersion.v1_2);
        PolicySourceModel model2 = PolicySourceModel.createPolicySourceModel(NamespaceVersion.v1_5);
        models.add(model);
        models.add(model2);
        Document document = builder.newDocument();
        TypedXmlWriter storage = TXW.create(new QName("policies"), TypedXmlWriter.class, new DomSerializer(document));
        PolicyModelMarshaller instance = PolicyModelMarshaller.getXmlMarshaller(false);
        instance.marshal(models, storage);
        storage.commit();
        Element element = document.getDocumentElement();
        Node policyElement = element.getFirstChild();
        assertEquals(policyElement.getLocalName(), "Policy");
    }

    public void testMarshalPrefixStream() throws PolicyException, XMLStreamException {
        PolicySourceModel model = PolicySourceModel.createPolicySourceModel(NamespaceVersion.v1_2, "id", null);
        ModelNode root = model.getRootNode();
        ModelNode all = root.createChildAllNode();
        AssertionData assertion = AssertionData.createAssertionData(new QName("http://schemas.foo.com/", "Assertion"));
        all.createChildAssertionNode(assertion);

        StringWriter writer = new StringWriter();
        XMLStreamWriter storage = factory.createXMLStreamWriter(writer);
        PolicyModelMarshaller instance = PolicyModelMarshaller.getXmlMarshaller(false);

        instance.marshal(model, storage);
        storage.flush();

        String result = writer.toString();
        assertTrue(result.contains("xmlns:wsp1_2"));
        assertTrue(result.contains("xmlns:wsu"));
    }

    public void testMarshalPrefixWrite() throws PolicyException {
        PolicySourceModel model = PolicySourceModel.createPolicySourceModel(NamespaceVersion.v1_5, "id", null);
        ModelNode root = model.getRootNode();
        ModelNode all = root.createChildAllNode();
        AssertionData assertion = AssertionData.createAssertionData(new QName("http://schemas.foo.com/", "Assertion"));
        all.createChildAssertionNode(assertion);

        Document document = builder.newDocument();
        TypedXmlWriter storage = TXW.create(new QName("root"), TypedXmlWriter.class, new DomSerializer(document));
        PolicyModelMarshaller instance = PolicyModelMarshaller.getXmlMarshaller(false);

        instance.marshal(model, storage);
        storage.commit();

        Element element = document.getDocumentElement();
        Node policyElement = element.getFirstChild();
        assertEquals(NamespaceVersion.v1_5.getDefaultNamespacePrefix(), policyElement.getPrefix());

        NamedNodeMap map = policyElement.getAttributes();
        Node id = map.getNamedItemNS(PolicyConstants.WSU_ID.getNamespaceURI(), PolicyConstants.WSU_ID.getLocalPart());
        assertEquals(PolicyConstants.WSU_NAMESPACE_PREFIX, id.getPrefix());
    }

}
