package com.vodafone.ebuisness.service;

import com.vodafone.ebuisness.exception.NoSuchCategoryException;
import com.vodafone.ebuisness.exception.NoSuchProductException;
import com.vodafone.ebuisness.model.main.Category;

import java.util.Set;

public interface SubscriptionService {

    void subscribeInCategory(String email, Category category) throws NoSuchCategoryException;

    void subscribeInCategory(String email, String name) throws NoSuchCategoryException;

    void removeSubscriptionFromCategory(String email, Category category) throws NoSuchCategoryException;

    void removeSubscriptionFromCategory(String email, String categoryName) throws NoSuchCategoryException;

    Set<String> getEmailsRelatedToProduct(String productId) throws NoSuchProductException;

}
