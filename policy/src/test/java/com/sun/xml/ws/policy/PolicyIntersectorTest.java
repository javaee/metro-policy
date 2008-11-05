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

package com.sun.xml.ws.policy;

import com.sun.xml.ws.policy.sourcemodel.AssertionData;
import java.util.LinkedList;
import javax.xml.namespace.QName;
import junit.framework.TestCase;

/**
 *
 * @author Fabian Ritzmann
 */
public class PolicyIntersectorTest extends TestCase {
    
    public PolicyIntersectorTest(String testName) {
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
     * Test of createStrictPolicyIntersector method, of class PolicyIntersector.
     */
    public void testCreateStrictPolicyIntersector() {
        PolicyIntersector result = PolicyIntersector.createStrictPolicyIntersector();
        assertNotNull(result);
    }

    /**
     * Test of createLaxPolicyIntersector method, of class PolicyIntersector.
     */
    public void testCreateLaxPolicyIntersector() {
        PolicyIntersector result = PolicyIntersector.createLaxPolicyIntersector();
        assertNotNull(result);
    }

    /**
     * Test of intersect method, of class PolicyIntersector.
     */
    public void testIntersectLax() {
        AssertionData data1 = AssertionData.createAssertionData(new QName("testns", "name1"));
        PolicyAssertion assertion1 = new MockAssertion(data1);
        AssertionData data2 = AssertionData.createAssertionData(new QName("testns", "name2"));
        PolicyAssertion assertion2 = new MockAssertion(data2);
        AssertionData data3 = AssertionData.createAssertionData(new QName("testns", "name3"));
        PolicyAssertion assertion3 = new MockAssertion(data3);
        LinkedList<PolicyAssertion> assertions1 = new LinkedList<PolicyAssertion>();
        assertions1.add(assertion1);
        assertions1.add(assertion2);
        assertions1.add(assertion3);
        AssertionSet set1 = AssertionSet.createAssertionSet(assertions1);
        LinkedList<AssertionSet> sets1 = new LinkedList<AssertionSet>();
        sets1.add(set1);
        Policy policy1 = Policy.createPolicy(null, "policy1", sets1);
        LinkedList<PolicyAssertion> assertions2 = new LinkedList<PolicyAssertion>();
        assertions2.add(assertion1);
        assertions2.add(assertion2);
        assertions2.add(assertion3);
        AssertionSet set2 = AssertionSet.createAssertionSet(assertions2);
        LinkedList<AssertionSet> sets2 = new LinkedList<AssertionSet>();
        sets2.add(set2);
        Policy policy2 = Policy.createPolicy(null, "policy2", sets2);
        Policy[] policies = new Policy[] {policy1, policy2};
        LinkedList<PolicyAssertion> assertions3 = new LinkedList<PolicyAssertion>();
        assertions3.add(assertion1);
        assertions3.add(assertion2);
        assertions3.add(assertion3);
        AssertionSet set3 = AssertionSet.createAssertionSet(assertions3);
        LinkedList<AssertionSet> sets3 = new LinkedList<AssertionSet>();
        sets3.add(set3);
        Policy expResult = Policy.createPolicy(null, "policy3", sets3);
        PolicyIntersector instance = PolicyIntersector.createLaxPolicyIntersector();
        Policy result = instance.intersect(policies);
        assertEquals(expResult, result);
    }

    public void testIntersectStrict() {
        AssertionData data1 = AssertionData.createAssertionData(new QName("testns", "name1"));
        PolicyAssertion assertion1 = new MockAssertion(data1);
        AssertionData data2 = AssertionData.createAssertionData(new QName("testns", "name2"));
        PolicyAssertion assertion2 = new MockAssertion(data2);
        AssertionData data3 = AssertionData.createAssertionData(new QName("testns", "name3"));
        PolicyAssertion assertion3 = new MockAssertion(data3);
        LinkedList<PolicyAssertion> assertions1 = new LinkedList<PolicyAssertion>();
        assertions1.add(assertion1);
        assertions1.add(assertion2);
        assertions1.add(assertion3);
        AssertionSet set1 = AssertionSet.createAssertionSet(assertions1);
        LinkedList<AssertionSet> sets1 = new LinkedList<AssertionSet>();
        sets1.add(set1);
        Policy policy1 = Policy.createPolicy(null, "policy1", sets1);
        LinkedList<PolicyAssertion> assertions2 = new LinkedList<PolicyAssertion>();
        assertions2.add(assertion1);
        assertions2.add(assertion2);
        assertions2.add(assertion3);
        AssertionSet set2 = AssertionSet.createAssertionSet(assertions2);
        LinkedList<AssertionSet> sets2 = new LinkedList<AssertionSet>();
        sets2.add(set2);
        Policy policy2 = Policy.createPolicy(null, "policy2", sets2);
        Policy[] policies = new Policy[] {policy1, policy2};
        LinkedList<PolicyAssertion> assertions3 = new LinkedList<PolicyAssertion>();
        assertions3.add(assertion1);
        assertions3.add(assertion2);
        assertions3.add(assertion3);
        AssertionSet set3 = AssertionSet.createAssertionSet(assertions3);
        LinkedList<AssertionSet> sets3 = new LinkedList<AssertionSet>();
        sets3.add(set3);
        Policy expResult = Policy.createPolicy(null, "policy3", sets3);
        PolicyIntersector instance = PolicyIntersector.createStrictPolicyIntersector();
        Policy result = instance.intersect(policies);
        assertEquals(expResult, result);
    }


    private static class MockAssertion extends PolicyAssertion {

        MockAssertion(AssertionData data) {
            super(data, null);
        }

    }
}
