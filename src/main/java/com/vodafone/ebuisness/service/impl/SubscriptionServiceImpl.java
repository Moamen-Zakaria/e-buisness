package com.vodafone.ebuisness.service.impl;

import com.vodafone.ebuisness.exception.NoSuchCategoryException;
import com.vodafone.ebuisness.exception.NoSuchProductException;
import com.vodafone.ebuisness.model.main.Category;
import com.vodafone.ebuisness.repository.CategoryRepository;
import com.vodafone.ebuisness.service.ProductsAndCategoriesService;
import com.vodafone.ebuisness.service.SubscriptionService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductsAndCategoriesService productsAndCategoriesService;

    @Override
    public void subscribeInCategory(String email, Category category)
            throws NoSuchCategoryException {
        if (category == null) {
            throw new NoSuchCategoryException();
        }
        if (category.getSubscribers() == null) {
            category.setSubscribers(new HashSet<>());
        }
        category.getSubscribers().add(email);
        productsAndCategoriesService.saveOrUpdateCategory(category);
    }

    @Override
    public void subscribeInCategory(String email, String categoryName)
            throws NoSuchCategoryException {
        Category category = categoryRepository.findByNameLike(categoryName);
        subscribeInCategory(email, category);
    }

    @Override
    public void removeSubscriptionFromCategory(String email, Category category)
            throws NoSuchCategoryException {
        if (email == null) {
            return;
        }
        if (category == null) {
            throw new NoSuchCategoryException();
        }
        var subscribers = category.getSubscribers();
        if (subscribers == null) {
            return;
        } else {
            var optionalEmail
                    = subscribers.stream().parallel()
                    .filter(sub -> sub.equals(email)).findFirst();
            if (optionalEmail.isPresent()) {
                subscribers.remove(optionalEmail.get());
            }
        }
        productsAndCategoriesService.saveOrUpdateCategory(category);
    }

    @Override
    public void removeSubscriptionFromCategory(String email, String categoryName)
            throws NoSuchCategoryException {
        Category category = categoryRepository.findByNameLike(categoryName);
        removeSubscriptionFromCategory(email, category);
    }

    @Override
    public Set<String> getEmailsRelatedToProduct(String productId)
            throws NoSuchProductException {
        var product
                = productsAndCategoriesService.findProductById(new ObjectId(productId));
        var categories = product.getCategories();
        if (categories == null) {
            return new HashSet<>();
        }
        var setOfEmails = new HashSet<String>();
        categories.stream().parallel().map(cat -> cat.getSubscribers())
                .forEach(set -> {
                    if (set != null) {
                        setOfEmails.addAll(set);
                    }
                });
        return setOfEmails;
    }

}
