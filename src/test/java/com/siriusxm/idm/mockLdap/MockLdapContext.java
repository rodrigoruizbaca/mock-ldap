package com.siriusxm.idm.mockLdap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.springframework.LdapDataEntry;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.odm.core.ObjectDirectoryMapper;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.util.Assert;

public abstract class MockLdapContext extends DirContextAdapter {
	private final Map<Name, LdapDataEntry> store = new HashMap<Name, LdapDataEntry>();
	private final Map<String, Name> storeByFilter = new HashMap<String, Name>();

	public abstract ObjectDirectoryMapper getObjectDirectoryMapper();
	
	private LdapDataEntry getEntry(Object obj) {
		DirContextAdapter context = new DirContextAdapter();
		Name id = getObjectDirectoryMapper().getId(obj);
        if(id == null) {
            id = getObjectDirectoryMapper().getCalculatedId(obj);
            getObjectDirectoryMapper().setId(context, id);
        }
        Assert.notNull(id, String.format("Unable to determine id for entry %s", obj.toString()));	
        context.setDn(id);
        getObjectDirectoryMapper().mapToLdapDataEntry(obj, context);	
        return context;
	}
	
	public Object lookup(Name name) {
		return store.get(name);
	}
	
	public Attributes getAttributes(String name, String[] attrIds) {
		Name dn = LdapNameBuilder.newInstance(name).build();
		LdapDataEntry entry = store.get(dn);
		return entry.getAttributes();
	}
	
	public void modifyAttributes(Name name, ModificationItem[] mods) {
		LdapDataEntry entry = store.get(name);
		DirContextAdapter newEntry = new DirContextAdapter(entry.getAttributes(), entry.getDn());
		Arrays.stream(mods).forEach(item -> {
			if (item.getModificationOp() == DirContext.REPLACE_ATTRIBUTE) {
				Attribute attr = item.getAttribute();
				newEntry.setAttribute(attr);
			}
			
		});
		store.put(name, newEntry);		
	}
	
	public void addElementByFilter(String filter, Object element) throws NamingException {
		Name name = getObjectDirectoryMapper().getId(element);
		bind(name, element);
		storeByFilter.put(filter, name);
	}

	
	public void bind(final Name name, final Object obj, final Attributes attributes) {
		if (obj instanceof LdapDataEntry) {
			store.put(name, ( LdapDataEntry ) obj);
		} else {
			LdapDataEntry entry = getEntry(obj);
			store.put(name, entry);
		}
		
	}
	
	public void bind(Name name, Object obj) throws NamingException {	
		bind(name, obj, null);
	}
	
	public NamingEnumeration<SearchResult> search(Name name, String filter, SearchControls cons)
			throws NamingException {
		LdapDataEntry entry = store.get(storeByFilter.get(filter));
		NamingEnumeration<?> attrs = entry.getAttributes().getAll();
		BasicAttributes attributes = new BasicAttributes();
		while (attrs.hasMoreElements()) {
			Attribute attr = (Attribute) attrs.nextElement();
			attributes.put(new BasicAttribute(attr.getID(), attr.get()));
		}
		SearchResult result = new SearchResult(entry.getDn().toString(), entry, attributes);
		return new MockNamingEnumeration<>(result);
	}

}
