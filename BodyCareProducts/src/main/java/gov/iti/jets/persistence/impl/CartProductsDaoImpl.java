package gov.iti.jets.persistence.impl;

import gov.iti.jets.persistence.CartProductsDao;
import gov.iti.jets.persistence.entities.CartProducts;
import gov.iti.jets.persistence.entities.Category;
import gov.iti.jets.persistence.entities.Product;
import gov.iti.jets.persistence.util.ManagerFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;

import java.util.List;

public class CartProductsDaoImpl implements CartProductsDao {
    private final static EntityManagerFactory entityManagerFactory = ManagerFactory.getEntityManagerFactory();

    @Override
    public boolean addCartProduct(CartProducts cartProducts) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(cartProducts);
        transaction.commit();
        entityManager.close();
        return true;
    }

    @Override
    public boolean chechIfProductExist(Integer userId, Integer productId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = entityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<CartProducts> query = cb.createQuery(CartProducts.class);
        Root<CartProducts> cartProducts = query.from(CartProducts.class);
        Predicate predicate = cb.and(cb.equal (cartProducts.get("user").<Integer>get("id"),userId),cb.equal ( cartProducts.get("product").<String>get("id"),productId));
        query.select(cartProducts).where(predicate);

        List<CartProducts> result = entityManager.createQuery(query).getResultList();
        entityManager.close();

        return result.size() > 0;
    }

    @Override
    public boolean updateProduct(int productId, int userId, int quantity) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaUpdate<CartProducts> cu = cb.createCriteriaUpdate(CartProducts.class);
        Root<CartProducts> c = cu.from(CartProducts.class);
        cu.set(c.get("quantity"), quantity).where(cb.and(cb.equal (c.get("user").<Integer>get("id"),userId),cb.equal ( c.get("product").<String>get("id"),productId)));
        entityManager.createQuery(cu).executeUpdate();
        entityManager.close();
        return false;
    }

    @Override
    public boolean addProductToCart(CartProducts cartProducts) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        entityManager.persist(cartProducts);

        transaction.commit();

        return true;
    }
}