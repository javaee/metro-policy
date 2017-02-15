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

import com.sun.xml.ws.policy.Policy;
import com.sun.xml.ws.policy.PolicyException;
import com.sun.xml.ws.policy.sourcemodel.PolicyModelGenerator.PolicySourceModelCreator;
import com.sun.xml.ws.policy.sourcemodel.wspolicy.NamespaceVersion;
import com.sun.xml.ws.policy.testutils.PolicyResourceLoader;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

/**
 *
 * @author Fabian Ritzmann
 */
public class PolicyModelGeneratorTest extends TestCase {

    private static final String COMPACT_MODEL_SUFFIX = ".xml";
    private static final String NORMALIZED_MODEL_SUFFIX = "_normalized.xml";
    private static final Map<String, Integer> COMPLEX_POLICIES;

    static {
        Map<String, Integer> tempMap = new HashMap<String, Integer>();
        tempMap.put("complex_policy/nested_assertions_with_alternatives", 3);
        tempMap.put("complex_policy/single_choice1", 3);
        tempMap.put("complex_policy/single_choice2", 5);
        tempMap.put("complex_policy/nested_choice1", 3);
        tempMap.put("complex_policy/nested_choice2", 3);
        tempMap.put("complex_policy/assertion_parameters1", 1);
        tempMap.put("complex_policy/assertion_parameters2", 1);
        COMPLEX_POLICIES = Collections.unmodifiableMap(tempMap);
    }

    private PolicyModelTranslator translator;
    private PolicyModelGenerator normalGenerator = PolicyModelGenerator.getGenerator();
    private PolicyModelGenerator compactGenerator = PolicyModelGenerator.getCompactGenerator(
            new PolicySourceModelCreator());
    
    
    public PolicyModelGeneratorTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        translator = PolicyModelTranslator.getTranslator();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getGenerator method, of class PolicyModelGenerator.
     */
    public void testGetGenerator() {
        PolicyModelGenerator result = PolicyModelGenerator.getGenerator();
        assertNotNull(result);
    }

    /**
     * Test of translate method, of class PolicyModelGenerator.
     * @throws PolicyException
     */
    public void testTranslateEmpty() throws PolicyException {
        String expResult = "id";
        Policy policy = Policy.createEmptyPolicy(NamespaceVersion.v1_5, null, expResult);
        PolicyModelGenerator instance = PolicyModelGenerator.getGenerator();
        PolicySourceModel model = instance.translate(policy);
        String result = model.getPolicyId();
        assertEquals(expResult, result);
    }

    /**
     * Test of translate method, of class PolicyModelGenerator.
     * @throws PolicyException
     */
    public void testTranslateNull() throws PolicyException {
        PolicyModelGenerator instance = PolicyModelGenerator.getGenerator();
        PolicySourceModel model = instance.translate(null);
        assertNull(model);
    }

    public void testGenerate() throws Exception {
        for (Map.Entry<String, Integer> entry : COMPLEX_POLICIES.entrySet()) {
            String compactResourceName = entry.getKey() + COMPACT_MODEL_SUFFIX;
            String normalizedResouceName = entry.getKey() + NORMALIZED_MODEL_SUFFIX;
            int expectedNumberOfAlternatives = entry.getValue();

            PolicySourceModel compactModel = PolicyResourceLoader.unmarshallModel(compactResourceName);
            PolicySourceModel normalizedModel = PolicyResourceLoader.unmarshallModel(normalizedResouceName);
            Policy compactModelPolicy = translator.translate(compactModel);
            Policy normalizedModelPolicy = translator.translate(normalizedModel);

            PolicySourceModel generatedCompactModel = compactGenerator.translate(compactModelPolicy);
            PolicySourceModel generatedNormalizedModel = normalGenerator.translate(normalizedModelPolicy);

            Policy generatedCompactModelPolicy = translator.translate(generatedCompactModel);
            Policy generatedNormalizedModelPolicy = translator.translate(generatedNormalizedModel);

            assertEquals("Generated compact policy should contain '" + expectedNumberOfAlternatives + "' alternatives",
                    expectedNumberOfAlternatives, generatedCompactModelPolicy.getNumberOfAssertionSets());
            assertEquals("Generated and translated compact model policy instances should contain equal number of alternatives",
                    compactModelPolicy.getNumberOfAssertionSets(), generatedCompactModelPolicy.getNumberOfAssertionSets());
            assertEquals("Generated and translated compact policy expression should form equal Policy instances",
                    compactModelPolicy, generatedCompactModelPolicy);

            assertEquals("Generated normalized policy should contain '" + expectedNumberOfAlternatives + "' alternatives",
                    expectedNumberOfAlternatives, generatedNormalizedModelPolicy.getNumberOfAssertionSets());
            assertEquals("Generated and translated normalized model policy instances should contain equal number of alternatives",
                    normalizedModelPolicy.getNumberOfAssertionSets(), generatedNormalizedModelPolicy.getNumberOfAssertionSets());
            assertEquals("Generated and translated normalized policy expression should form equal Policy instances",
                    normalizedModelPolicy, generatedNormalizedModelPolicy);

            // TODO: somehow compare models, because now the test only checks if the translation does not end in some exception...
        }
    }

    public void testPreserveOriginalNamespaceInformation() throws Exception {
        PolicySourceModel model = normalGenerator.translate(PolicyResourceLoader.loadPolicy("namespaces/policy-v1.2.xml"));
        assertEquals("Namespace does not match original", NamespaceVersion.v1_2, model.getNamespaceVersion());
        model = normalGenerator.translate(PolicyResourceLoader.loadPolicy("namespaces/policy-v1.5.xml"));
        assertEquals("Namespace does not match original", NamespaceVersion.v1_5, model.getNamespaceVersion());
    }

    public void testPreserveOriginalNamespaceInformationCompact() throws Exception {
        PolicySourceModel model = compactGenerator.translate(PolicyResourceLoader.loadPolicy("namespaces/policy-v1.2.xml"));
        assertEquals("Namespace does not match original", NamespaceVersion.v1_2, model.getNamespaceVersion());
        model = compactGenerator.translate(PolicyResourceLoader.loadPolicy("namespaces/policy-v1.5.xml"));
        assertEquals("Namespace does not match original", NamespaceVersion.v1_5, model.getNamespaceVersion());
    }

}
