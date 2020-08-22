package com.vodafone.ebuisness.controller.stateless;

import com.vodafone.ebuisness.exception.EmailDoesNotExistException;
import com.vodafone.ebuisness.exception.NoSuchCategoryException;
import com.vodafone.ebuisness.exception.NoSuchProductException;
import com.vodafone.ebuisness.model.main.Category;
import com.vodafone.ebuisness.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@RestController
@RequestMapping("/shared/subscriptions")
public class SubscriptionClass {

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping("/add")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addSubscription(@RequestParam @NotBlank String email,
                                @RequestParam @NotBlank String categoryName)
            throws NoSuchCategoryException {
        subscriptionService.subscribeInCategory(email, categoryName);
    }

    @DeleteMapping("/remove")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void removeSubscription(@RequestParam @NotBlank @Email String email,
                                   @RequestParam @NotBlank String categoryName)
            throws NoSuchCategoryException {
        subscriptionService.removeSubscriptionFromCategory(email, categoryName);
    }

    @GetMapping("/product/subscribers/{productId}")
    @ResponseStatus(value = HttpStatus.OK)
    public Set<String> getAllSubscriptionsOfProduct(@PathVariable @NotBlank String productId)
            throws NoSuchProductException {
        return subscriptionService.getEmailsRelatedToProduct(productId);
    }

}
