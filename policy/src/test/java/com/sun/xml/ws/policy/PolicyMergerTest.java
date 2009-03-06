/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2009 Sun Microsystems, Inc. All rights reserved.
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

package com.sun.xml.ws.policy;

import com.sun.xml.ws.policy.sourcemodel.AssertionData;
import com.sun.xml.ws.policy.sourcemodel.wspolicy.NamespaceVersion;
import java.util.Collection;
import java.util.LinkedList;
import javax.xml.namespace.QName;
import junit.framework.TestCase;

/**
 *
 * @author Fabian Ritzmann
 */
public class PolicyMergerTest extends TestCase {
    
    public PolicyMergerTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getMerger method, of class PolicyMerger.
     */
    public void testGetMerger() {
        final PolicyMerger result = PolicyMerger.getMerger();
        assertNotNull(result);
    }

    /**
     * Test of merge method, of class PolicyMerger.
     */
    public void testMergeNull() {
        final Collection<Policy> policies = null;
        final PolicyMerger instance = PolicyMerger.getMerger();
        final Policy result = instance.merge(policies);
        assertNull(result);
    }

    /**
     * Test of merge method, of class PolicyMerger.
     */
    public void testMergeNone() {
        final Collection<Policy> policies = new LinkedList<Policy>();
        final PolicyMerger instance = PolicyMerger.getMerger();
        final Policy result = instance.merge(policies);
        assertNull(result);
    }

    /**
     * Test of merge method, of class PolicyMerger.
     */
    public void testMergeEmpty() {
        final Collection<Policy> policies = new LinkedList<Policy>();
        policies.add(Policy.createEmptyPolicy());
        final PolicyMerger instance = PolicyMerger.getMerger();
        final Policy result = instance.merge(policies);
        assertTrue(result.isEmpty());
    }

    /**
     * Test of merge method, of class PolicyMerger.
     */
    public void testFourEmpty() {
        final Collection<Policy> policies = new LinkedList<Policy>();
        policies.add(Policy.createEmptyPolicy());
        policies.add(Policy.createEmptyPolicy());
        policies.add(Policy.createEmptyPolicy());
        policies.add(Policy.createEmptyPolicy());
        final PolicyMerger instance = PolicyMerger.getMerger();
        final Policy result = instance.merge(policies);
        assertTrue(result.isEmpty());
    }

    /**
     * Test of merge method, of class PolicyMerger.
     */
    public void testMergeEmptyAndNonEmpty() {
        final Collection<Policy> policies = new LinkedList<Policy>();
        policies.add(Policy.createEmptyPolicy());
        AssertionData assertionData = AssertionData.createAssertionData(new QName("http://example.test/", "Assertion1"));
        PolicyAssertion assertion = new PolicyAssertion(assertionData, null) { };
        Collection<PolicyAssertion> assertions = new LinkedList<PolicyAssertion>();
        assertions.add(assertion);
        AssertionSet assertionSet = AssertionSet.createAssertionSet(assertions);
        Collection<AssertionSet> assertionSets = new LinkedList<AssertionSet>();
        assertionSets.add(assertionSet);
        Policy expResult = Policy.createPolicy(assertionSets);
        policies.add(expResult);
        final PolicyMerger instance = PolicyMerger.getMerger();
        final Policy result = instance.merge(policies);
        assertEquals(expResult, result);
    }

    /**
     * Test of merge method, of class PolicyMerger.
     */
    public void testMergeTwo() {
        AssertionData assertionData1 = AssertionData.createAssertionData(new QName("http://example.test/", "Assertion1"));
        PolicyAssertion assertion1 = new PolicyAssertion(assertionData1, null) { };
        Collection<PolicyAssertion> assertions1 = new LinkedList<PolicyAssertion>();
        assertions1.add(assertion1);
        AssertionSet assertionSet1 = AssertionSet.createAssertionSet(assertions1);
        Collection<AssertionSet> assertionSets1 = new LinkedList<AssertionSet>();
        assertionSets1.add(assertionSet1);
        Policy policy1 = Policy.createPolicy(assertionSets1);

        AssertionData assertionData2 = AssertionData.createAssertionData(new QName("http://example.test/", "Assertion2"));
        PolicyAssertion assertion2 = new PolicyAssertion(assertionData2, null) { };
        Collection<PolicyAssertion> assertions2 = new LinkedList<PolicyAssertion>();
        assertions2.add(assertion2);
        AssertionSet assertionSet2 = AssertionSet.createAssertionSet(assertions2);
        Collection<AssertionSet> assertionSets2 = new LinkedList<AssertionSet>();
        assertionSets2.add(assertionSet2);
        Policy policy2 = Policy.createPolicy(assertionSets2);

        final Collection<Policy> policies = new LinkedList<Policy>();
        policies.add(policy1);
        policies.add(policy2);

        Collection<PolicyAssertion> assertionsExp = new LinkedList<PolicyAssertion>();
        assertionsExp.add(assertion1);
        assertionsExp.add(assertion2);
        AssertionSet assertionSetExp = AssertionSet.createAssertionSet(assertionsExp);
        Collection<AssertionSet> assertionSetsExp = new LinkedList<AssertionSet>();
        assertionSetsExp.add(assertionSetExp);
        Policy expResult = Policy.createPolicy(assertionSetsExp);

        final PolicyMerger instance = PolicyMerger.getMerger();
        final Policy result = instance.merge(policies);
        assertEquals(expResult, result);
    }

    /**
     * Test of merge method, of class PolicyMerger.
     */
    public void testMergeId() {
        AssertionData assertionData1 = AssertionData.createAssertionData(new QName("http://example.test/", "Assertion1"));
        PolicyAssertion assertion1 = new PolicyAssertion(assertionData1, null) { };
        Collection<PolicyAssertion> assertions1 = new LinkedList<PolicyAssertion>();
        assertions1.add(assertion1);
        AssertionSet assertionSet1 = AssertionSet.createAssertionSet(assertions1);
        Collection<AssertionSet> assertionSets1 = new LinkedList<AssertionSet>();
        assertionSets1.add(assertionSet1);
        Policy policy1 = Policy.createPolicy(null, "id1", assertionSets1);

        AssertionData assertionData2 = AssertionData.createAssertionData(new QName("http://example.test/", "Assertion2"));
        PolicyAssertion assertion2 = new PolicyAssertion(assertionData2, null) { };
        Collection<PolicyAssertion> assertions2 = new LinkedList<PolicyAssertion>();
        assertions2.add(assertion2);
        AssertionSet assertionSet2 = AssertionSet.createAssertionSet(assertions2);
        Collection<AssertionSet> assertionSets2 = new LinkedList<AssertionSet>();
        assertionSets2.add(assertionSet2);
        Policy policy2 = Policy.createPolicy(null, "id2", assertionSets2);

        final Collection<Policy> policies = new LinkedList<Policy>();
        policies.add(policy1);
        policies.add(policy2);

        final String expResult1 = "id1-id2";
        final String expResult2 = "id2-id1";
        final PolicyMerger instance = PolicyMerger.getMerger();
        final Policy mergedPolicy = instance.merge(policies);
        final String result = mergedPolicy.getId();
        if (!expResult1.equals(result)) {
            assertEquals(expResult2, result);
        }
    }

    /**
     * Test of merge method, of class PolicyMerger.
     */
    public void testMergePolicyNamespace() {
        AssertionData assertionData1 = AssertionData.createAssertionData(new QName("http://example.test/", "Assertion1"));
        PolicyAssertion assertion1 = new PolicyAssertion(assertionData1, null) { };
        Collection<PolicyAssertion> assertions1 = new LinkedList<PolicyAssertion>();
        assertions1.add(assertion1);
        AssertionSet assertionSet1 = AssertionSet.createAssertionSet(assertions1);
        Collection<AssertionSet> assertionSets1 = new LinkedList<AssertionSet>();
        assertionSets1.add(assertionSet1);
        Policy policy1 = Policy.createPolicy(NamespaceVersion.v1_2, null, null, assertionSets1);

        AssertionData assertionData2 = AssertionData.createAssertionData(new QName("http://example.test/", "Assertion2"));
        PolicyAssertion assertion2 = new PolicyAssertion(assertionData2, null) { };
        Collection<PolicyAssertion> assertions2 = new LinkedList<PolicyAssertion>();
        assertions2.add(assertion2);
        AssertionSet assertionSet2 = AssertionSet.createAssertionSet(assertions2);
        Collection<AssertionSet> assertionSets2 = new LinkedList<AssertionSet>();
        assertionSets2.add(assertionSet2);
        Policy policy2 = Policy.createPolicy(NamespaceVersion.v1_5, null, null, assertionSets2);

        final Collection<Policy> policies = new LinkedList<Policy>();
        policies.add(policy1);
        policies.add(policy2);

        Collection<PolicyAssertion> assertionsExp = new LinkedList<PolicyAssertion>();
        assertionsExp.add(assertion1);
        assertionsExp.add(assertion2);
        AssertionSet assertionSetExp = AssertionSet.createAssertionSet(assertionsExp);
        Collection<AssertionSet> assertionSetsExp = new LinkedList<AssertionSet>();
        assertionSetsExp.add(assertionSetExp);
        Policy expResult = Policy.createPolicy(assertionSetsExp);

        final PolicyMerger instance = PolicyMerger.getMerger();
        final Policy result = instance.merge(policies);
        assertEquals(expResult, result);
    }

    /**
     * Test of merge method, of class PolicyMerger.
     */
    public void testMergeEmptyAlternatives() {
        final Collection<Policy> policies = new LinkedList<Policy>();
        final Collection<PolicyAssertion> assertions = new LinkedList<PolicyAssertion>();

        AssertionSet assertionSet1 = AssertionSet.createAssertionSet(assertions);
        AssertionSet assertionSet2 = AssertionSet.createAssertionSet(assertions);
        Collection<AssertionSet> assertionSets1 = new LinkedList<AssertionSet>();
        assertionSets1.add(assertionSet1);
        assertionSets1.add(assertionSet2);
        Policy policy1 = Policy.createPolicy(assertionSets1);

        AssertionSet assertionSet3 = AssertionSet.createAssertionSet(assertions);
        AssertionSet assertionSet4 = AssertionSet.createAssertionSet(assertions);
        Collection<AssertionSet> assertionSets2 = new LinkedList<AssertionSet>();
        assertionSets1.add(assertionSet3);
        assertionSets1.add(assertionSet4);
        Policy policy2 = Policy.createPolicy(assertionSets2);

        policies.add(policy1);
        policies.add(policy2);

        Policy expResult = Policy.createNullPolicy();
        final PolicyMerger instance = PolicyMerger.getMerger();
        final Policy result = instance.merge(policies);
        assertEquals(expResult, result);
    }

}
