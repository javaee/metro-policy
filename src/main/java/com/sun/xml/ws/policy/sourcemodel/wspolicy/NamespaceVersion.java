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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;

/**
 *
 * @author Marek Potociar (marek.potociar at sun.com)
 */
public enum NamespaceVersion {
    v1_2("http://schemas.xmlsoap.org/ws/2004/09/policy", "wsp1_2", new XmlToken[] {
    XmlToken.Policy,
    XmlToken.ExactlyOne,
    XmlToken.All,
    XmlToken.PolicyReference,
    XmlToken.UsingPolicy,
    XmlToken.Name,
    XmlToken.Optional,
    XmlToken.Ignorable,
    XmlToken.PolicyUris,
    XmlToken.Uri,
    XmlToken.Digest,
    XmlToken.DigestAlgorithm
    }), 
    v1_5("http://www.w3.org/ns/ws-policy", "wsp", new XmlToken[] {
    XmlToken.Policy,
    XmlToken.ExactlyOne,
    XmlToken.All,
    XmlToken.PolicyReference,
    XmlToken.UsingPolicy,
    XmlToken.Name,
    XmlToken.Optional,
    XmlToken.Ignorable,
    XmlToken.PolicyUris,
    XmlToken.Uri,
    XmlToken.Digest,
    XmlToken.DigestAlgorithm
    });    
    
    /**
     * Resolves URI represented as a String into an enumeration value. If the URI 
     * doesn't represent any existing enumeration value, method returns 
     * {@code null}.
     * 
     * @param uri WS-Policy namespace URI
     * @return Enumeration value that represents given URI or {@code null} if 
     * no enumeration value exists for given URI.
     */
    public static NamespaceVersion resolveVersion(String uri) {
        for (NamespaceVersion namespaceVersion : NamespaceVersion.values()) {
            if (namespaceVersion.toString().equalsIgnoreCase(uri)) {
                return namespaceVersion;
            }
        }

        return null;
    }

     /**
     * Resolves fully qualified name defined in the WS-Policy namespace into an 
      * enumeration value. If the URI in the name doesn't represent any existing 
      * enumeration value, method returns {@code null}
     * 
     * @param name fully qualified name defined in the WS-Policy namespace 
     * @return Enumeration value that represents given namespace or {@code null} if 
     * no enumeration value exists for given namespace.
     */
    public static NamespaceVersion resolveVersion(QName name) {
        return resolveVersion(name.getNamespaceURI());
    }

    /**
     * Returns latest supported version of the policy namespace
     * 
     * @return latest supported policy namespace version.
     */
    public static NamespaceVersion getLatestVersion() {
        return v1_5;
    }
    
    /**
     * Resolves FQN into a policy XML token. The version of the token can be determined
     * by invoking {@link #resolveVersion(QName)}.
     * 
     * @param name fully qualified name defined in the WS-Policy namespace
     * @return XML token enumeration that represents this fully qualified name. 
     * If the token or the namespace is not resolved {@link XmlToken#UNKNOWN} value 
     * is returned.
     */
    public static XmlToken resolveAsToken(QName name) {  
        NamespaceVersion nsVersion = resolveVersion(name);
        if (nsVersion != null) {
            XmlToken token = XmlToken.resolveToken(name.getLocalPart());            
            if (nsVersion.tokenToQNameCache.containsKey(token)) {
                return token;
            }
        }        
        return XmlToken.UNKNOWN;
    }        
    
    private final String nsUri;
    private final String defaultNsPrefix;
    private final Map<XmlToken, QName> tokenToQNameCache;
    
    private NamespaceVersion(String uri, String prefix, XmlToken... supportedTokens) {
        nsUri = uri;
        defaultNsPrefix = prefix;
        
        Map<XmlToken, QName> temp = new HashMap<XmlToken, QName>();
        for (XmlToken token : supportedTokens) {
            temp.put(token, new QName(nsUri, token.toString()));
        }
        tokenToQNameCache = Collections.unmodifiableMap(temp);        
    }

    /**
     * Method returns default namespace prefix for given namespace version.
     * 
     * @return default namespace prefix for given namespace version
     */            
    public String getDefaultNamespacePrefix() {
        return defaultNsPrefix;
    }
    
    /**
     * Resolves XML token into a fully qualified name within given namespace version.
     * 
     * @param token XML token enumeration value.
     * @return fully qualified name of the {@code token} within given namespace 
     * version. Method returns {@code null} in case the token is not supported in 
     * given namespace version or in case {@link XmlToken#UNKNOWN} was used as 
     * an input parameter. 
     */
    public QName asQName(XmlToken token) throws IllegalArgumentException {
        return tokenToQNameCache.get(token);
    }

    @Override
    public String toString() {
        return nsUri;
    }
}
