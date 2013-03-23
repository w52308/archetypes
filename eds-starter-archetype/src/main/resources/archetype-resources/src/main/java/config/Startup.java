#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.config;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ${package}.entity.QRole;
import ${package}.entity.QUser;
import ${package}.entity.Role;
import ${package}.entity.User;

import com.google.common.collect.Sets;
import com.mysema.query.jpa.impl.JPAQuery;

@Component
public class Startup implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private Environment environment;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (new JPAQuery(entityManager).from(QUser.user).count() == 0) {
			// admin user
			User adminUser = new User();
			adminUser.setUserName("admin");
			adminUser.setEmail("test@test.ch");
			adminUser.setFirstName("admin");
			adminUser.setName("admin");
			adminUser.setLocale("en");
			adminUser.setPasswordHash(passwordEncoder.encode("admin"));
			adminUser.setEnabled(true);

			Role adminRole = new JPAQuery(entityManager).from(QRole.role).where(QRole.role.name.eq("ROLE_ADMIN"))
					.singleResult(QRole.role);
			adminUser.setRoles(Sets.newHashSet(adminRole));

			entityManager.persist(adminUser);

			// normal user
			User normalUser = new User();
			normalUser.setUserName("user");
			normalUser.setEmail("user@test.ch");
			normalUser.setFirstName("user");
			normalUser.setName("user");
			normalUser.setLocale("de");

			normalUser.setPasswordHash(passwordEncoder.encode("user"));
			normalUser.setEnabled(true);

			Role userRole = new JPAQuery(entityManager).from(QRole.role).where(QRole.role.name.eq("ROLE_USER"))
					.singleResult(QRole.role);
			normalUser.setRoles(Sets.newHashSet(userRole));

			entityManager.persist(normalUser);
		}

	}

}
