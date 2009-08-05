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
import com.sun.xml.ws.policy.sourcemodel.ModelNode;
import com.sun.xml.ws.policy.sourcemodel.PolicyModelTranslator;
import com.sun.xml.ws.policy.sourcemodel.PolicySourceModel;
import com.sun.xml.ws.policy.sourcemodel.wspolicy.NamespaceVersion;
import java.util.Arrays;
import java.util.Collection;
import javax.xml.namespace.QName;
import junit.framework.TestCase;

/**
 *
 * @author Fabian Ritzmann
 */
public class EffectiveAlternativeSelectorTest extends TestCase {
    
    public EffectiveAlternativeSelectorTest(String testName) {
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
     * Test of doSelection method, of class EffectiveAlternativeSelector.
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
     */
    public void testDoSelectionAlternatives() throws PolicyException {
        final PolicySourceModel model = PolicySourceModel.createPolicySourceModel(NamespaceVersion.v1_5, "id", null);
        final ModelNode root = model.getRootNode();
        final ModelNode alternatives = root.createChildExactlyOneNode();
        final ModelNode alternative1 = alternatives.createChildAllNode();
        final ModelNode alternative2 = alternatives.createChildAllNode();
        final QName name1 = new QName("test1", "test1");
        final AssertionData assertion1 = AssertionData.createAssertionData(name1);
        alternative1.createChildAssertionNode(assertion1);
        final QName name2 = new QName("test2", "test2");
        final AssertionData assertion2 = AssertionData.createAssertionData(name2);
        alternative2.createChildAssertionNode(assertion2);
        final PolicyModelTranslator translator = PolicyModelTranslator.getTranslator();
        final Policy policy = translator.translate(model);

        final PolicyMapExtender extender = PolicyMapExtender.createPolicyMapExtender();
        final PolicyMap map = PolicyMap.createPolicyMap(Arrays.asList(new PolicyMapMutator[] {extender}));

        final PolicySubject subject = new PolicySubject("dummy", policy);

        final PolicyMapKey key = PolicyMap.createWsdlServiceScopeKey(new QName("1"));
        extender.putServiceSubject(key, subject);

        final EffectivePolicyModifier modifier = EffectivePolicyModifier.createEffectivePolicyModifier();
        modifier.connect(map);
        EffectiveAlternativeSelector.doSelection(modifier);

        final Policy result = map.getServiceEffectivePolicy(key);
        if (result.contains(name1)) {
            assertFalse(result.contains(name2));
        }
        else if (result.contains(name2)) {
            assertFalse(result.contains(name1));
        }
        else {
            fail("Expected exactly one assertion in the resulting policy.");
        }
    }

}