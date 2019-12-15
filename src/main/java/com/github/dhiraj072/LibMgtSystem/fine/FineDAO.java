package com.github.dhiraj072.LibMgtSystem.fine;

import static com.github.dhiraj072.LibMgtSystem.book.BookCheckout.MEMBER;
import static com.github.dhiraj072.LibMgtSystem.fine.Fine.PAYMENT_DATE;

import com.github.dhiraj072.LibMgtSystem.book.BookCheckout;
import com.github.dhiraj072.LibMgtSystem.member.Member;
import java.time.LocalDate;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FineDAO {

  private static final Logger LOGGER = LoggerFactory.getLogger(FineDAO.class);

  @PersistenceContext
  private EntityManager em;

  private CriteriaBuilder cb;

  @PostConstruct
  public void init() {

    cb = em.getCriteriaBuilder();
  }

  public Fine imposeFine(BookCheckout checkout) {

    Fine fine = new Fine(checkout);
    em.persist(fine);
    LOGGER.info("Fine of {} imposed on {}", fine.getAmount(), checkout.getMember());
    return fine;
  }

  public List<Fine> getPendingFines(Member m) {

    CriteriaQuery<Fine> searchQuery = cb.createQuery(Fine.class);
    Root<Fine> fine = searchQuery.from(Fine.class);
    Join<Fine, BookCheckout> fineBookCheckoutJoin = fine.join("bookCheckout");
    searchQuery.select(fine);
    Predicate checkedOutByMember = cb.equal(fineBookCheckoutJoin.get(MEMBER), m);
    Predicate fineNotPaid = cb.isNull(fine.get(PAYMENT_DATE));
    searchQuery.where(checkedOutByMember, fineNotPaid);
    return em.createQuery(searchQuery).getResultList();
  }

  public void payFine(Fine fine) {

    fine.setPaymentDate(LocalDate.now());
    em.persist(fine);
    LOGGER.info("Fine of {} on {} has been paid", fine.getAmount(), fine.getBookCheckout());
  }
}
