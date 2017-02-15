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

package com.sun.xml.ws.policy.privateutil;

import com.sun.istack.logging.Logger;
import java.lang.reflect.Field;

/**
 * This is a helper class that provides some conveniece methods wrapped around the
 * standard {@link java.util.logging.Logger} interface.
 *
 * @author Marek Potociar
 * @author Fabian Ritzmann
 */
public final class PolicyLogger extends Logger {

    /**
     * If we run with JAX-WS, we are using its logging domain (appended with ".wspolicy").
     * Otherwise we default to "wspolicy".
     */
    private static final String POLICY_PACKAGE_ROOT = "com.sun.xml.ws.policy";

    /**
     * Make sure this class cannot be instantiated by client code.
     *
     * @param policyLoggerName The name of the subsystem to be logged.
     * @param className The fully qualified class name.
     */
    private PolicyLogger(final String policyLoggerName, final String className) {
        super(policyLoggerName, className);
    }

    /**
     * The factory method returns preconfigured PolicyLogger wrapper for the class. Since there is no caching implemented,
     * it is advised that the method is called only once per a class in order to initialize a final static logger variable,
     * which is then used through the class to perform actual logging tasks.
     *
     * @param componentClass class of the component that will use the logger instance. Must not be {@code null}.
     * @return logger instance preconfigured for use with the component
     * @throws NullPointerException if the componentClass parameter is {@code null}.
     */
    public static PolicyLogger getLogger(final Class<?> componentClass) {
        final String componentClassName = componentClass.getName();

        if (componentClassName.startsWith(POLICY_PACKAGE_ROOT)) {
            return new PolicyLogger(getLoggingSubsystemName() + componentClassName.substring(POLICY_PACKAGE_ROOT.length()),
                    componentClassName);
        } else {
            return new PolicyLogger(getLoggingSubsystemName() + "." + componentClassName, componentClassName);
        }
    }

    private static String getLoggingSubsystemName() {
        String loggingSubsystemName = "wspolicy";
        try {
            // Looking up JAX-WS class at run-time, so that we don't need to depend
            // on it at compile-time.
            Class jaxwsConstants = Class.forName("com.sun.xml.ws.util.Constants");
            Field loggingDomainField = jaxwsConstants.getField("LoggingDomain");
            Object loggingDomain = loggingDomainField.get(null);
            loggingSubsystemName = loggingDomain.toString().concat(".wspolicy");
        } catch (RuntimeException e) {
            // if we catch an exception, we stick with the default name
            // this catch is redundant but works around a Findbugs warning
        } catch (Exception e) {
            // if we catch an exception, we stick with the default name
        }
        return loggingSubsystemName;
    }

}
