package com.siriusxm.idm.mockLdap;

import java.util.Set;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Entry(objectClasses = { "sxm17device", "top" })
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"dn"})
public class Radio {
	/** the istinguished name that maps to LDAP dn */
	@Id
	private Name dn;

	@Attribute(name = "sxm17devicenoncesigningkey")
	private String nonceKey;

	@Attribute(name = "sxm17statekey")
	private String sxm17statekey;
	
	@Attribute(name = "sxm17deviceid")
	private String deviceId;

	@Attribute(name = "sxm17Hwid")
	private String hwId;

	@Attribute(name = "accountno")
    private String accountNo;

	@Attribute(name = "sxm17DeviceNonce")
	private String nonce;

	@Attribute(name = "sxm17nonceTimeStamp")
	private String nonceTimestamp;

	@Attribute(name = "sxm17LastAuthState")
	private String lastAuthState;

	@Attribute(name = "sxm17AuthStateUpdateInProgress")
	private String authStateUpdateInProgress;
	
	@Attribute(name="sxm17guests")
    private Set<String> guests;
}
