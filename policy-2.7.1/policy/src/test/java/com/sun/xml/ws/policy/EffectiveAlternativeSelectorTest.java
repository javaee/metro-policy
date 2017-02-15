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
import static com.sun.xml.ws.policy.testutils.PolicyResourceLoader.loadPolicy;

import java.util.Arrays;
import java.util.HashSet;
import javax.xml.namespace.QName;

import junit.framework.TestCase;

/**
 *
 * @author Fabian Ritzmann
 */
public class EffectiveAlternativeSelectorTest extends TestCase {
    
    private final QName assertion1Name = new QName("test1", "test1");
    private final QName assertion2Name = new QName("test2", "test2");

    private Policy multipleAlternativesPolicy;

    
    public EffectiveAlternativeSelectorTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws PolicyException {
        final PolicySourceModel model = PolicySourceModel.createPolicySourceModel(NamespaceVersion.v1_5, "id", null);
        final ModelNode root = model.getRootNode();
        final ModelNode alternatives = root.createChildExactlyOneNode();
        final ModelNode alternative1 = alternatives.createChildAllNode();
        final ModelNode alternative2 = alternatives.createChildAllNode();
        final AssertionData assertion1 = AssertionData.createAssertionData(assertion1Name);
        alternative1.createChildAssertionNode(assertion1);
        final AssertionData assertion2 = AssertionData.createAssertionData(assertion2Name);
        alternative2.createChildAssertionNode(assertion2);
        final PolicyModelTranslator translator = PolicyModelTranslator.getTranslator();
        this.multipleAlternativesPolicy = translator.translate(model);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of doSelection method, of class EffectiveAlternativeSelector.
     * @throws PolicyException
     */
    public void testDoSelectionNull() throws PolicyException {
        final EffectivePolicyModifier modifier = null;
        try {
            EffectiveAlternativeSelector.doSelection(modifier);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    /**
     * Test of doSelection method, of class EffectiveAlternativeSelector.
     * @throws PolicyException
     */
    public void testDoSelectionUnconnected() throws PolicyException {
        final EffectivePolicyModifier modifier = EffectivePolicyModifier.createEffectivePolicyModifier();
        try {
            EffectiveAlternativeSelector.doSelection(modifier);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    /**
     * Test of doSelection method, of class EffectiveAlternativeSelector.
     * @throws PolicyException
     */
    public void testDoSelectionEmpty() throws PolicyException {
        final EffectivePolicyModifier modifier = EffectivePolicyModifier.createEffectivePolicyModifier();
        final PolicyMap map = PolicyMap.createPolicyMap(null);
        modifier.connect(map);
        EffectiveAlternativeSelector.doSelection(modifier);
        assertTrue(map.isEmpty());
    }

    /**
     * Test of doSelection method, of class EffectiveAlternativeSelector.
     * @throws PolicyException
     */
    public void testDoSelectionAlternativesService() throws PolicyException {

        final PolicyMapExtender extender = PolicyMapExtender.createPolicyMapExtender();
        final PolicyMap map = PolicyMap.createPolicyMap(Arrays.asList(new PolicyMapMutator[] {extender}));

        final PolicySubject subject = new PolicySubject("dummy", this.multipleAlternativesPolicy);

        final PolicyMapKey key = PolicyMap.createWsdlServiceScopeKey(new QName("service"));
        extender.putServiceSubject(key, subject);

        final EffectivePolicyModifier modifier = EffectivePolicyModifier.createEffectivePolicyModifier();
        modifier.connect(map);
        EffectiveAlternativeSelector.doSelection(modifier);

        final Policy result = map.getServiceEffectivePolicy(key);
        if (result.contains(this.assertion1Name)) {
            assertFalse(result.contains(this.assertion2Name));
        }
        else if (result.contains(this.assertion2Name)) {
            assertFalse(result.contains(this.assertion1Name));
        }
        else {
            fail("Expected exactly one assertion in the resulting policy.");
        }
    }

    /**
     * Test of doSelection method, of class EffectiveAlternativeSelector.
     * @throws PolicyException
     */
    public void testDoSelectionAlternativesEndpoint() throws PolicyException {

        final PolicyMapExtender extender = PolicyMapExtender.createPolicyMapExtender();
        final PolicyMap map = PolicyMap.createPolicyMap(Arrays.asList(new PolicyMapMutator[] {extender}));

        final PolicySubject subject = new PolicySubject("dummy", this.multipleAlternativesPolicy);

        final PolicyMapKey key = PolicyMap.createWsdlEndpointScopeKey(new QName("service"), new QName("port"));
        extender.putEndpointSubject(key, subject);

        final EffectivePolicyModifier modifier = EffectivePolicyModifier.createEffectivePolicyModifier();
        modifier.connect(map);
        EffectiveAlternativeSelector.doSelection(modifier);

        final Policy result = map.getEndpointEffectivePolicy(key);
        if (result.contains(this.assertion1Name)) {
            assertFalse(result.contains(this.assertion2Name));
        }
        else if (result.contains(this.assertion2Name)) {
            assertFalse(result.contains(this.assertion1Name));
        }
        else {
            fail("Expected exactly one assertion in the resulting policy.");
        }
    }

    /**
     * Test of doSelection method, of class EffectiveAlternativeSelector.
     * @throws PolicyException
     */
    public void testDoSelectionAlternativesOperation() throws PolicyException {

        final PolicyMapExtender extender = PolicyMapExtender.createPolicyMapExtender();
        final PolicyMap map = PolicyMap.createPolicyMap(Arrays.asList(new PolicyMapMutator[] {extender}));

        final PolicySubject subject = new PolicySubject("dummy", this.multipleAlternativesPolicy);

        final PolicyMapKey key = PolicyMap.createWsdlOperationScopeKey(
                new QName("service"), new QName("port"), new QName("operation"));
        extender.putOperationSubject(key, subject);

        final EffectivePolicyModifier modifier = EffectivePolicyModifier.createEffectivePolicyModifier();
        modifier.connect(map);
        EffectiveAlternativeSelector.doSelection(modifier);

        final Policy result = map.getOperationEffectivePolicy(key);
        if (result.contains(this.assertion1Name)) {
            assertFalse(result.contains(this.assertion2Name));
        }
        else if (result.contains(this.assertion2Name)) {
            assertFalse(result.contains(this.assertion1Name));
        }
        else {
            fail("Expected exactly one assertion in the resulting policy.");
        }
    }

    /**
     * Test of doSelection method, of class EffectiveAlternativeSelector.
     * @throws PolicyException
     */
    public void testDoSelectionAlternativesInput() throws PolicyException {

        final PolicyMapExtender extender = PolicyMapExtender.createPolicyMapExtender();
        final PolicyMap map = PolicyMap.createPolicyMap(Arrays.asList(new PolicyMapMutator[] {extender}));

        final PolicySubject subject = new PolicySubject("dummy", this.multipleAlternativesPolicy);

        final PolicyMapKey key = PolicyMap.createWsdlMessageScopeKey(
                new QName("service"), new QName("port"), new QName("operation"));
        extender.putInputMessageSubject(key, subject);

        final EffectivePolicyModifier modifier = EffectivePolicyModifier.createEffectivePolicyModifier();
        modifier.connect(map);
        EffectiveAlternativeSelector.doSelection(modifier);

        final Policy result = map.getInputMessageEffectivePolicy(key);
        if (result.contains(this.assertion1Name)) {
            assertFalse(result.contains(this.assertion2Name));
        }
        else if (result.contains(this.assertion2Name)) {
            assertFalse(result.contains(this.assertion1Name));
        }
        else {
            fail("Expected exactly one assertion in the resulting policy.");
        }
    }

    public void testDoSelectionAlternativesOutput() throws PolicyException {

        final PolicyMapExtender extender = PolicyMapExtender.createPolicyMapExtender();
        final PolicyMap map = PolicyMap.createPolicyMap(Arrays.asList(new PolicyMapMutator[] {extender}));

        final PolicySubject subject = new PolicySubject("dummy", this.multipleAlternativesPolicy);

        final PolicyMapKey key = PolicyMap.createWsdlMessageScopeKey(
                new QName("service"), new QName("port"), new QName("operation"));
        extender.putOutputMessageSubject(key, subject);

        final EffectivePolicyModifier modifier = EffectivePolicyModifier.createEffectivePolicyModifier();
        modifier.connect(map);
        EffectiveAlternativeSelector.doSelection(modifier);

        final Policy result = map.getOutputMessageEffectivePolicy(key);
        if (result.contains(this.assertion1Name)) {
            assertFalse(result.contains(this.assertion2Name));
        }
        else if (result.contains(this.assertion2Name)) {
            assertFalse(result.contains(this.assertion1Name));
        }
        else {
            fail("Expected exactly one assertion in the resulting policy.");
        }
    }

    public void testDoSelectionAlternativesFault() throws PolicyException {

        final PolicyMapExtender extender = PolicyMapExtender.createPolicyMapExtender();
        final PolicyMap map = PolicyMap.createPolicyMap(Arrays.asList(new PolicyMapMutator[] {extender}));

        final PolicySubject subject = new PolicySubject("dummy", this.multipleAlternativesPolicy);

        final PolicyMapKey key = PolicyMap.createWsdlFaultMessageScopeKey(
                new QName("service"), new QName("port"), new QName("operation"), new QName("fault"));
        extender.putFaultMessageSubject(key, subject);

        final EffectivePolicyModifier modifier = EffectivePolicyModifier.createEffectivePolicyModifier();
        modifier.connect(map);
        EffectiveAlternativeSelector.doSelection(modifier);

        final Policy result = map.getFaultMessageEffectivePolicy(key);
        if (result.contains(this.assertion1Name)) {
            assertFalse(result.contains(this.assertion2Name));
        }
        else if (result.contains(this.assertion2Name)) {
            assertFalse(result.contains(this.assertion1Name));
        }
        else {
            fail("Expected exactly one assertion in the resulting policy.");
        }
    }

    /**
     * Test of doSelection method, of class com.sun.xml.ws.policy.EffectiveAlternativeSelector.
     * @throws Exception
     */
    public void testDoPositiveSelection() throws Exception {
        HashSet<PolicyMapMutator> mutators = new HashSet<PolicyMapMutator>();
        EffectivePolicyModifier myModifier = EffectivePolicyModifier.createEffectivePolicyModifier();
        mutators.add(myModifier);
        PolicyMapExtender myExtender = PolicyMapExtender.createPolicyMapExtender();
        mutators.add(myExtender);
        PolicyMap policyMap = PolicyMap.createPolicyMap(mutators);

        //Policy pol1 = PolicyModelTranslator.getTranslator()
        //                .translate(unmarshalModel("single_alternative_policy/policy3.xml"));
        Policy pol2 = loadPolicy("complex_policy/nested_assertions_with_alternatives.xml");

        PolicyMapKey aKey = PolicyMap.createWsdlEndpointScopeKey(new QName("service"),new QName("port"));

        //myExtender.putEndpointSubject(aKey,new PolicySubject("one",pol1));
        myExtender.putEndpointSubject(aKey,new PolicySubject("two",pol2));

        //System.out.println(myExtender.getMap());

        if(2>myExtender.getMap().getEndpointEffectivePolicy(aKey).getNumberOfAssertionSets()) {
            fail("Insufficient number of alternatives found. At least 2 of them needed.");
        }

        EffectiveAlternativeSelector.doSelection(myModifier);

        if(1!=myExtender.getMap().getEndpointEffectivePolicy(aKey).getNumberOfAssertionSets()) {
            fail("Too many alternatives has left.");
        }
    }

    public void testDoNegativeSelection() throws Exception {
        HashSet<PolicyMapMutator> mutators = new HashSet<PolicyMapMutator>();
        EffectivePolicyModifier myModifier = EffectivePolicyModifier.createEffectivePolicyModifier();
        mutators.add(myModifier);
        PolicyMapExtender myExtender = PolicyMapExtender.createPolicyMapExtender();
        mutators.add(myExtender);
        PolicyMap policyMap = PolicyMap.createPolicyMap(mutators);

        //Policy pol1 = PolicyModelTranslator.getTranslator()
        //                .translate(unmarshalModel("single_alternative_policy/policy3.xml"));
        Policy pol2 = loadPolicy("complex_policy/nested_assertions_with_alternatives.xml");

        PolicyMapKey aKey = PolicyMap.createWsdlEndpointScopeKey(new QName("service"),new QName("port"));

        //myExtender.putEndpointSubject(aKey,new PolicySubject("one",pol1));
        myExtender.putEndpointSubject(aKey,new PolicySubject("two",pol2));

        //System.out.println(myExtender.getMap());

        if(2>myExtender.getMap().getEndpointEffectivePolicy(aKey).getNumberOfAssertionSets()) {
            fail("Insufficient number of alternatives found. At least 2 of them needed.");
        }

        EffectiveAlternativeSelector.doSelection(myModifier);

        if(1!=myExtender.getMap().getEndpointEffectivePolicy(aKey).getNumberOfAssertionSets()) {
            fail("Too many alternatives has left.");
        }

        EffectiveAlternativeSelector.doSelection(myModifier);

        if(1!=myExtender.getMap().getEndpointEffectivePolicy(aKey).getNumberOfAssertionSets()) {
            fail("Too many alternatives has left.");
        }
    }

    public void testDoEmptySelection() throws Exception {
        HashSet<PolicyMapMutator> mutators = new HashSet<PolicyMapMutator>();
        EffectivePolicyModifier modifier = EffectivePolicyModifier.createEffectivePolicyModifier();
        mutators.add(modifier);
        PolicyMapExtender extender = PolicyMapExtender.createPolicyMapExtender();
        mutators.add(extender);
        PolicyMap.createPolicyMap(mutators);

        Policy emptyPolicy = Policy.createEmptyPolicy();
        PolicyMapKey key = PolicyMap.createWsdlEndpointScopeKey(new QName("service"), new QName("port"));
        extender.putEndpointSubject(key, new PolicySubject("two", emptyPolicy));

        EffectiveAlternativeSelector.doSelection(modifier);

        assertTrue(extender.getMap().getEndpointEffectivePolicy(key).isEmpty());
    }

    public void testDoNullSelection() throws Exception {
        HashSet<PolicyMapMutator> mutators = new HashSet<PolicyMapMutator>();
        EffectivePolicyModifier modifier = EffectivePolicyModifier.createEffectivePolicyModifier();
        mutators.add(modifier);
        PolicyMapExtender extender = PolicyMapExtender.createPolicyMapExtender();
        mutators.add(extender);
        PolicyMap.createPolicyMap(mutators);

        Policy nullPolicy = Policy.createNullPolicy();
        PolicyMapKey key = PolicyMap.createWsdlEndpointScopeKey(new QName("service"), new QName("port"));
        extender.putEndpointSubject(key, new PolicySubject("two", nullPolicy));

        EffectiveAlternativeSelector.doSelection(modifier);

        assertTrue(extender.getMap().getEndpointEffectivePolicy(key).isNull());
    }

}
