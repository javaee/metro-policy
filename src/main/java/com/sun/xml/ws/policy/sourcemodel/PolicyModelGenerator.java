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

import com.sun.xml.ws.policy.AssertionSet;
import com.sun.xml.ws.policy.NestedPolicy;
import com.sun.xml.ws.policy.Policy;
import com.sun.xml.ws.policy.PolicyAssertion;
import com.sun.xml.ws.policy.PolicyException;
import com.sun.xml.ws.policy.privateutil.LocalizationMessages;
import com.sun.xml.ws.policy.privateutil.PolicyLogger;

import java.util.Iterator;

/**
 * Translate a policy into a PolicySourceModel.
 *
 * Code that depends on JAX-WS should use com.sun.xml.ws.api.policy.ModelGenerator
 * instead of this class.
 *
 * @author Marek Potociar
 * @author Fabian Ritzmann
 */
public abstract class PolicyModelGenerator {

    private static final PolicyLogger LOGGER = PolicyLogger.getLogger(PolicyModelGenerator.class);
    
    /**
     * This protected constructor avoids direct instantiation from outside of the class
     */
    protected PolicyModelGenerator() {
        // nothing to initialize
    }
    
    /**
     * Factory method that returns a {@link PolicyModelGenerator} instance.
     *
     * @return {@link PolicyModelGenerator} instance
     */
    public static PolicyModelGenerator getGenerator() {
        return getNormalizedGenerator(new PolicySourceModelCreator());
    }

    /**
     * Allows derived classes to create instances of the package private
     * CompactModelGenerator.
     *
     * @param creator An implementation of the PolicySourceModelCreator.
     * @return An instance of CompactModelGenerator.
     */
    protected static PolicyModelGenerator getCompactGenerator(PolicySourceModelCreator creator) {
        return new CompactModelGenerator(creator);
    }

    /**
     * Allows derived classes to create instances of the package private
     * NormalizedModelGenerator.
     *
     * @param creator An implementation of the PolicySourceModelCreator.
     * @return An instance of NormalizedModelGenerator.
     */
    protected static PolicyModelGenerator getNormalizedGenerator(PolicySourceModelCreator creator) {
        return new NormalizedModelGenerator(creator);
    }

    /**
     * This method translates a {@link Policy} into a
     * {@link com.sun.xml.ws.policy.sourcemodel policy infoset}. The resulting
     * PolicySourceModel is disconnected from the input policy, thus any
     * additional changes in the policy will have no effect on the PolicySourceModel.
     *
     * @param policy The policy to be translated into an infoset. May be null.
     * @return translated The policy infoset. May be null if the input policy was
     * null.
     * @throws PolicyException in case Policy translation fails.
     */
    public abstract PolicySourceModel translate(final Policy policy) throws PolicyException;

    /**
     * Iterates through a nested policy and returns the corresponding policy info model.
     *
     * @param parentAssertion The parent node.
     * @param policy The nested policy.
     * @return The nested policy translated to the policy info model.
     */
    protected abstract ModelNode translate(final ModelNode parentAssertion, final NestedPolicy policy);

    /**
     * Add the contents of the assertion set as child node to the given model node.
     *
     * @param node The content of this assertion set are added as child nodes to this node.
     *     May not be null.
     * @param assertions The assertions that are to be added to the node. May not be null.
     */
    protected void translate(final ModelNode node, final AssertionSet assertions) {
        for (PolicyAssertion assertion : assertions) {
            final AssertionData data = AssertionData.createAssertionData(assertion.getName(), assertion.getValue(), assertion.getAttributes(), assertion.isOptional(), assertion.isIgnorable());
            final ModelNode assertionNode = node.createChildAssertionNode(data);
            if (assertion.hasNestedPolicy()) {
                translate(assertionNode, assertion.getNestedPolicy());
            }
            if (assertion.hasParameters()) {
                translate(assertionNode, assertion.getParametersIterator());
            }
        }
    }

    /**
     * Iterates through all contained assertions and adds them to the info model.
     *
     * @param assertionParametersIterator The contained assertions.
     * @param assertionNode The node to which the assertions are added as child nodes
     */
    protected void translate(final ModelNode assertionNode, final Iterator<PolicyAssertion> assertionParametersIterator) {
        while (assertionParametersIterator.hasNext()) {
            final PolicyAssertion assertionParameter = assertionParametersIterator.next();
            final AssertionData data = AssertionData.createAssertionParameterData(assertionParameter.getName(), assertionParameter.getValue(), assertionParameter.getAttributes());
            final ModelNode assertionParameterNode = assertionNode.createChildAssertionParameterNode(data);
            if (assertionParameter.hasNestedPolicy()) {
                throw LOGGER.logSevereException(new IllegalStateException(LocalizationMessages.WSP_0005_UNEXPECTED_POLICY_ELEMENT_FOUND_IN_ASSERTION_PARAM(assertionParameter)));
            }
            if (assertionParameter.hasNestedAssertions()) {
                translate(assertionParameterNode, assertionParameter.getNestedAssertionsIterator());
            }
        }
    }


    /**
     * Allows derived classes to pass their own implementation of PolicySourceModelCreator
     * into the PolicyModelGenerator instance.
     */
    protected static class PolicySourceModelCreator {

        /**
         * Create an instance of the PolicySourceModel.
         *
         * @param policy The policy that underlies the created PolicySourceModel.
         * @return An instance of the PolicySourceModel.
         */
        protected PolicySourceModel create(final Policy policy) {
            return PolicySourceModel.createPolicySourceModel(policy.getNamespaceVersion(),
                    policy.getId(), policy.getName());
        }

    }

}
