package com.modulus.uno

import grails.converters.JSON
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import com.modulus.uno.businessEntity.BusinessEntitiesGroup

@EqualsAndHashCode(includes='username')
@ToString(includes='username', includeNames=true, includePackage=false)
class User implements Serializable {

	private static final long serialVersionUID = 1

	transient springSecurityService

  String uuid = UuidGenerator.generateUuid()
	String username
	String password
	boolean enabled
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired
  String key2FA
  boolean enable2FA

  Profile profile

  static hasMany = [businessEntitiesGroups:BusinessEntitiesGroup]

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this)*.role
	}

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
	}

	static transients = ['springSecurityService']

	static constraints = {
		password blank: false, password: true
		username blank: false, unique: true
    key2FA nullable:true
	}

	static mapping = {
		password column: '`password`'
	}

  String getName() {
    profile.fullName
  }

	static marshaller = {
		JSON.registerObjectMarshaller(User, 1) { m ->
			return [
			id: m.id,
			uuid: m.uuid,
			username: m.username,
			profile: m.profile
			]
		}
	}

}
