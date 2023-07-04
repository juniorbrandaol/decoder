package com.ead.payment.services;

import com.ead.payment.models.CreditCardModel;
import com.ead.payment.models.PaymentModel;
import com.stripe.exception.StripeException;

public interface PaymentStripeService {

    PaymentModel processStripePayment(PaymentModel paymentModel, CreditCardModel creditCardModel) ;
}
