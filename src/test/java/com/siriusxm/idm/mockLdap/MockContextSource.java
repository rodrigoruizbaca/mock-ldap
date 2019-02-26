package com.siriusxm.idm.mockLdap;

import javax.naming.directory.DirContext;
import javax.naming.ldap.LdapName;

import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;

@SuppressWarnings("deprecation")
public class MockContextSource implements BaseLdapPathContextSource {
	private LdapName baseName = null;
	private MockLdapContext instance;
	
	public MockContextSource(MockLdapContext instance) {
		this.instance = instance;
	}

	public DirContext getReadOnlyContext() throws NamingException {
		return instance;
	}

	public DirContext getReadWriteContext() throws NamingException {
		return instance;
	}

	public DirContext getContext(String principal, String credentials) throws NamingException {
		return instance;
	}

	public DistinguishedName getBaseLdapPath() {
		return new DistinguishedName(baseName);
	}

	public LdapName getBaseLdapName() {
		return baseName;
	}

	public String getBaseLdapPathAsString() {
		return baseName.toString();
	}

	public void setBaseLdapName(LdapName baseName) {
		this.baseName = baseName;
	}

}
