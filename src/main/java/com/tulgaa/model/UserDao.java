package com.tulgaa.model;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class UserDao {

	@Autowired
	private SessionFactory _sessionFactory;

	private Session getSession() {
		return _sessionFactory.getCurrentSession();
	}

	public void save(MyUser user) {
		getSession().save(user);
		return;
	}

	public void delete(MyUser user) {
		getSession().delete(user);
		return;
	}

	@SuppressWarnings("unchecked")
	public List<MyUser> getAll() {
		return getSession().createQuery("from User").list();
	}

	public MyUser getByEmail(String email) {
		return (MyUser) getSession().createQuery(
				"from User where email = :email")
				.setParameter("email", email)
				.uniqueResult();
	}

	public MyUser getByUserID(long id) {
		return (MyUser) getSession().createQuery(
				"from User where id = :id")
				.setParameter("id", id)
				.uniqueResult();
	}

	public MyUser getById(long id) {
		return (MyUser) getSession().load(MyUser.class, id);
	}

	public void update(MyUser user) {
		getSession().update(user);
		return;
	}
	
	public Object getGlobal(String model, String where_clause){
		if (where_clause.length() > 0){
			return getSession().createQuery("from " + model +" where " + where_clause).list();
		}
		else{
			return getSession().createQuery("from " + model).list();
		}
	}
	
	public Object getByIdGlobal(long id, String model) throws ClassNotFoundException {
		return getSession().load(Class.forName("com.tulgaa.model."+model), id);
	}
	
	public void deleteGlobal(Object obj){
		getSession().delete(obj);
	}
	
	public void updateGlobal(Object obj){
		getSession().update(obj);
	}
	
	public void saveGlobal(Object obj) {
		getSession().save(obj);
		return;
	}
}