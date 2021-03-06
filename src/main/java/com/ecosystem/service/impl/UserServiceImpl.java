package com.ecosystem.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecosystem.model.User;
import com.ecosystem.service.UserService;


@Service
public class UserServiceImpl implements UserService {

	@PersistenceContext
	EntityManager em;
	
	@Transactional
	public void addUser(User user) {
		em.persist(user);
	}
	
	@Transactional
	public List<User> getUserList() {
		CriteriaQuery<User> u = em.getCriteriaBuilder().createQuery(User.class);
		u.from(User.class);
		return em.createQuery(u).getResultList();
	}
	
	@Transactional
	public void removeUser(Integer id) {
		User u = em.find(User.class, id);
		if( null != u)
			em.remove(u);
	}
	
	@Transactional
	public void updateUser(User user) {
		em.merge(user);
	}
	
	@Transactional
	public User find(Integer userId) {
		return em.find(User.class, userId);
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public User findByEmail(String email) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User> criteria = cb.createQuery( User.class );
		Root<User> personRoot = criteria.from( User.class );
		criteria.select( personRoot );
		ParameterExpression<String> emailParam = cb.parameter( String.class );
		criteria.where( cb.equal( personRoot.get( "email" ), emailParam ) );
		TypedQuery<User> query = em.createQuery( criteria );
		query.setParameter( emailParam, email );
		List<User> userList = query.getResultList();
		return userList.size() > 0 ? userList.get(0) : null;
	}
}
