package com.siriusxm.idm.mockLdap;

import static org.junit.Assert.assertEquals;
import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.util.List;

import javax.naming.Name;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.odm.core.ObjectDirectoryMapper;
import org.springframework.ldap.odm.core.impl.DefaultObjectDirectoryMapper;
import org.springframework.ldap.support.LdapNameBuilder;

public class LdapTest {
	
	LdapTemplate template;
	
	String baseDn = "ou=radio,ou=customers,dc=siriusxm,dc=com";
	
	MockLdapContext context = null;
	
	@Before
	public void setUp() throws Exception {
		DefaultObjectDirectoryMapper mapper = new DefaultObjectDirectoryMapper();
		mapper.manageClass(Radio.class);
				
		context = new MockLdapContext(){
			@Override
			public ObjectDirectoryMapper getObjectDirectoryMapper() {
				return mapper;
			}
		};
			
		MockContextSource contextSource = new MockContextSource(context);
		template = new LdapTemplate(contextSource);
		template.setObjectDirectoryMapper(mapper);
		
	}
	
	//@Test
	public void testAddRario() throws Exception {
		
		Radio radio = new Radio();
		Name dn = LdapNameBuilder.newInstance(baseDn).add("uid", "1234").build();
		radio.setDn(dn);
		radio.setHwId("123");
			
		context.addElementByFilter("(&(&(objectclass=sxm17device)(objectclass=top))(sxm17hwid=123))", radio);
		
		List<Radio> radios = template.find(
				query().base(LdapNameBuilder.newInstance(baseDn).build()).where("sxm17hwid").is("123"),
			Radio.class);
		
		Radio result = radios.get(0);
		
		assertEquals(radio, result);
		
	}
	
	@Test
	public void testUpdateRario() throws Exception {
		
		Radio radio = new Radio();
		Name dn = LdapNameBuilder.newInstance(baseDn).add("uid", "1234").build();
		radio.setDn(dn);
		radio.setHwId("123");
			
		template.create(radio);
		
		
		Radio result = template.findByDn(dn, Radio.class);
		
		assertEquals(radio, result);
		
		ModificationItem[] items = new ModificationItem[] {
					new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("sxm17DeviceNonce", "mynonce"))
		};
		
		template.modifyAttributes(dn, items);
		
		result = template.findByDn(dn, Radio.class);
		
		assertEquals("mynonce", result.getNonce());
		
	}
	
}
