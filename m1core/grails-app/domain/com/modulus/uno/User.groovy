package com.modulus.uno

import grails.converters.JSON

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

  Profile profile

	User(String username, String password) {
		this()
		this.username = username
		this.password = password
	}

	@Override
	int hashCode() {
		username?.hashCode() ?: 0
	}

	@Override
	boolean equals(other) {
		is(other) || (other instanceof User && other.username == username)
	}

	@Override
	String toString() {
		username
	}

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
		username blank: false, unique: true
		password blank: false
	}

	static mapping = {
		password column: '`password`'
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
