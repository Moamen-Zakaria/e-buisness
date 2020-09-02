package com.vodafone.ebuisness.service.impl;

import com.vodafone.ebuisness.exception.NoSuchCategoryException;
import com.vodafone.ebuisness.exception.NoSuchProductException;
import com.vodafone.ebuisness.model.main.Category;
import com.vodafone.ebuisness.model.main.Product;
import com.vodafone.ebuisness.repository.CategoryRepository;
import com.vodafone.ebuisness.service.ProductsAndCategoriesService;
import com.vodafone.ebuisness.service.SubscriptionService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class SubscriptionServiceImplTest {

    public SubscriptionServiceImplTest() {
        SubscriptionServiceImpl subscriptionServiceImpl = new SubscriptionServiceImpl();
        categoryRepository = spy(CategoryRepository.class);
        productsAndCategoriesService = (spy(spy(ProductsAndCategoriesService.class)));
        subscriptionServiceImpl.setCategoryRepository(categoryRepository);
        subscriptionServiceImpl.setProductsAndCategoriesService(productsAndCategoriesService);
        subscriptionService = subscriptionServiceImpl;
    }

    private SubscriptionService subscriptionService;

    private CategoryRepository categoryRepository;
    private ProductsAndCategoriesService productsAndCategoriesService;


    @Test
    void subscribeInCategory() throws NoSuchCategoryException {

        //happy scenario
        Category category = new Category("cosmetics", "perfumes and so on", new HashSet<>());
        category.setObjectId(new ObjectId());

        subscriptionService.subscribeInCategory("moamen.zakaria@gmail.com", category);

        verify(productsAndCategoriesService).saveOrUpdateCategory(category);

    }

    @Test
    void subscribeInCategoryIfUnknownCategoryUsed() {

        Category nullCategory = null;
        Assertions.assertThrows(NoSuchCategoryException.class,
                () -> subscriptionService.subscribeInCategory("moamen.zakaria@gmail.com", nullCategory));
        verify(productsAndCategoriesService, never()).saveOrUpdateCategory(any());

    }

    @Test
    void testSubscribeInCategoryByCategoryName() {

        Category category = new Category("technology", "perfumes and so on", new HashSet<>());
        when(categoryRepository.findByNameLike("technology")).thenReturn(category);

        Assertions.assertDoesNotThrow(() ->
                subscriptionService.subscribeInCategory("moamen.zakaria@gmail.com", "technology"));

    }

    @Test
    void removeSubscriptionFromCategoryIfCategoryNameUsed() throws NoSuchCategoryException {

        Category category = new Category("technology", "perfumes and so on", new HashSet<>());
        category.setObjectId(new ObjectId());
        when(categoryRepository.findByNameLike("technology")).thenReturn(category);

        Assertions.assertDoesNotThrow(() ->
                subscriptionService.removeSubscriptionFromCategory("moamen.zakaria@gmail.com", category.getName()));

    }

    @Test
    void testRemoveSubscriptionFromCategory() {

        Category category = new Category("technology", "perfumes and so on", new HashSet<>());
        category.setObjectId(new ObjectId());

        Assertions.assertDoesNotThrow(() ->
                subscriptionService.removeSubscriptionFromCategory("moamen.zakaria@gmail.com", category));

    }

    @Test
    void testRemoveSubscriptionFromCategoryIfNullCategoryUsed() {

        Category category = null;

        Assertions.assertThrows(NoSuchCategoryException.class, () ->
                subscriptionService.removeSubscriptionFromCategory("moamen.zakaria@gmail.com", category));

    }

    @Test
    void testRemoveSubscriptionFromCategoryIfNullEmailAndCategoryUsed() {

        Category category = null;

        Assertions.assertDoesNotThrow(() ->
                subscriptionService.removeSubscriptionFromCategory(null, category));

    }

    @Test
    void getEmailsRelatedToProduct() throws NoSuchProductException {

        Product product = new Product();
        product.setObjectId(new ObjectId());
        product.setName("lavender perfume");

        Category category = new Category("technology", "perfumes and so on", new HashSet<>());
        category.setObjectId(new ObjectId());

        Set<String> setOfSubscribers = new HashSet<>();
        setOfSubscribers.add("moamen.zakaria@gmail.com");
        category.setSubscribers(setOfSubscribers);

        List<Category> listOFCategories = new ArrayList<>();
        listOFCategories.add(category);

        product.setCategories(listOFCategories);

        when(productsAndCategoriesService.findProductById(product.getObjectId())).thenReturn(product);

        setOfSubscribers = subscriptionService.getEmailsRelatedToProduct(product.getObjectId().toHexString());
        Assertions.assertTrue(setOfSubscribers.contains("moamen.zakaria@gmail.com"));

    }
}