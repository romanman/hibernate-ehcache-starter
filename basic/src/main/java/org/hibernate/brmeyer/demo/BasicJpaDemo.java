/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * JBoss, Home of Professional Open Source
 * Copyright 2014 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package org.hibernate.brmeyer.demo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Hibernate;
import org.hibernate.brmeyer.demo.entity.Skill;
import org.hibernate.brmeyer.demo.entity.Tool;
import org.hibernate.brmeyer.demo.entity.User;

/**
 * @author Brett Meyer
 */
public class BasicJpaDemo {

	public static void main(String[] args) {
		try {
			Tool tool = new Tool();
			// Note: id generated by Hibernate
			tool.setName( "Hammer" );
			insertTool( tool );
			List<Tool> tools = new ArrayList<Tool>();
			tools.add( tool );

			Skill skill = new Skill();
			// Note: id generated by Hibernate
			skill.setName( "Hammering Things" );
			insertSkill( skill );
			List<Skill> skills = new ArrayList<Skill>();
			skills.add( skill );

			User user = new User();
			// Note: id generated by Hibernate
			user.setName( "Brett Meyer" );
			user.setEmail( "foo@foo.com" );
			user.setPhone( "123-456-7890" );
			user.setTools( tools );
			user.setSkills( skills );

			insertUser( user );

			user = getUser(1);
			System.out.println( user.toString() );
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void insertUser(User user) throws Exception {
		EntityManager entityManager = openEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist( user ); // cascades the tool & skill relationships
		entityManager.getTransaction().commit();
	}

	private static void insertTool(Tool tool) throws SQLException {
		EntityManager entityManager = openEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist( tool );
//		entityManager.getTransaction().commit();
	}

	private static void insertSkill(Skill skill) throws SQLException {
		EntityManager entityManager = openEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist( skill );
//		entityManager.getTransaction().commit();
	}

	private static User getUser(int id) throws SQLException {
		EntityManager entityManager = openEntityManager();

		User user = entityManager.find( User.class, id );

//		Query query = entityManager.createQuery( "SELECT u FROM User u WHERE u.id=:id" );
//		query.setParameter( "id", id );
//		User user = (User) query.getSingleResult();

//		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//		CriteriaQuery<User> criteria = builder.createQuery( User.class );
//		Root<User> root = criteria.from( User.class );
//		criteria.select( root );
//		criteria.where( builder.equal( root.get( "id" ), id ) );
//		User user = entityManager.createQuery( criteria ).getSingleResult();

		Hibernate.initialize( user.getTools() );
		Hibernate.initialize( user.getSkills() );

		entityManager.close();

		return user;
	}

	private static EntityManagerFactory entityManagerFactory = null;
	private static EntityManager openEntityManager() {
		if (entityManagerFactory == null) {
			entityManagerFactory = Persistence.createEntityManagerFactory( "Demo" );
		}
		return entityManagerFactory.createEntityManager();
	}
}
