/*
 *  eXist Open Source Native XML Database
 *  Copyright (C) 2010 The eXist Project
 *  http://exist-db.org
 *  
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *  
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *  
 *  $Id$
 */
package org.exist.security.realm.activedirectory;

import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;

import org.apache.log4j.Logger;
import org.exist.config.Configuration;
import org.exist.config.annotation.ConfigurationClass;
import org.exist.config.annotation.ConfigurationField;
import org.exist.security.AuthenticationException;
import org.exist.security.User;
import org.exist.security.UserImpl;
import org.exist.security.realm.ldap.LDAPRealm;
import org.exist.security.realm.ldap.LdapContextFactory;

/**
 * @author <a href="mailto:shabanovd@gmail.com">Dmitriy Shabanov</a>
 *
 */
@ConfigurationClass("ActiveDirectory")
public class ActiveDirectoryRealm extends LDAPRealm {
	
	private final static Logger LOG = Logger.getLogger(LDAPRealm.class);

	public ActiveDirectoryRealm(Configuration config) {
		super(config);
	}

    protected LdapContextFactory ensureContextFactory() {
        if (this.ldapContextFactory == null) {

            if (LOG.isDebugEnabled()) {
            	LOG.debug("No LdapContextFactory specified - creating a default instance.");
            }

            LdapContextFactory factory = new ContextFactory(configuration);

            this.ldapContextFactory = factory;
        }
        return this.ldapContextFactory;
    }

    /* (non-Javadoc)
	 * @see org.exist.security.Realm#getId()
	 */
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.exist.security.Realm#authenticate(java.lang.String, java.lang.Object)
	 */
	public User authenticate(String username, Object credentials) throws AuthenticationException {

		String returnedAtts[] ={ "sn", "givenName", "mail" };   
        String searchFilter = "(&(objectClass=user)(sAMAccountName=" + username + "))";   

        //Create the search controls   
        SearchControls searchCtls = new SearchControls();   
        searchCtls.setReturningAttributes(returnedAtts);   

        //Specify the search scope   
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);   

        LdapContext ctxGC = null;   
        boolean ldapUser = false;   

        try   
        {   
              ctxGC = ensureContextFactory().getLdapContext(username, String.valueOf(credentials));
              
              //Search objects in GC using filters   
              NamingEnumeration answer = ctxGC.search(
            		  ((ContextFactory)ensureContextFactory()).getSearchBase(),
            		  searchFilter, 
            		  searchCtls); 
              
              while (answer.hasMoreElements())   
              {   
                    SearchResult sr = (SearchResult) answer.next();   
                    Attributes attrs = sr.getAttributes();   
                    Map amap = null;   
                    if (attrs != null)   
                    {   
                          amap = new HashMap();   
                          NamingEnumeration ne = attrs.getAll();   
                          while (ne.hasMore())   
                          {   
                                Attribute attr = (Attribute) ne.next();   
                                amap.put(attr.getID(), attr.get());   
                                ldapUser = true;   
                          }   
                          ne.close();   
                    }   
              }   
        } catch (NamingException e) {   
			throw new AuthenticationException(
					AuthenticationException.UNNOWN_EXCEPTION, 
					e.getMessage());
        }   

        return new UserImpl(this, username);
	}
}