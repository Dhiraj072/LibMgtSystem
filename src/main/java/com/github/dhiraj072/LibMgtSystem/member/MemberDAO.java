package com.github.dhiraj072.LibMgtSystem.member;

import com.github.dhiraj072.LibMgtSystem.exceptions.UsernameExistsException;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MemberDAO {

  private static final Logger LOGGER = LoggerFactory.getLogger(MemberDAO.class);

  @PersistenceContext
  private EntityManager em;

  private CriteriaBuilder cb;

  @PostConstruct
  public void init() {

    cb = em.getCriteriaBuilder();
  }

  public void addMember(Member m) {

    if (getMember(m.getUserName()) != null)
      throw new UsernameExistsException(m);
    em.persist(m);
  }

  public Member getMember(String userName) {

    return em.find(Member.class, userName);
  }
}
