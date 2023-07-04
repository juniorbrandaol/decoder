package com.ead.payment.services.impl;

import com.ead.payment.enums.PaymentControl;
import com.ead.payment.models.CreditCardModel;
import com.ead.payment.models.PaymentModel;
import com.ead.payment.services.PaymentStripeService;
import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.PaymentIntent;

import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentStripeServiceImpl implements PaymentStripeService {

    @Value(value = "${ead.stripe.secretKey}")
    private String secretKeySripe;

    @Override
    public PaymentModel processStripePayment(PaymentModel paymentModel, CreditCardModel creditCardModel) {
        //conecta na platarforma stripe
        Stripe.apiKey = secretKeySripe;
        String paymentIntentId = null;
        String cardNumber;

        try{

            //1º paymentIntent
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(paymentModel.getValuePaid().multiply(new BigDecimal("100")).longValue())
                .setCurrency("brl")
                .setPaymentMethod("pm_card_visa")
                .build();
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            paymentIntentId = paymentIntent.getId();

            //3º confirmation paymentIntent
            Map<String, Object> paramsPaymentConfirm = new HashMap<>();
            PaymentIntent confirmPaymentIntent =  paymentIntent.confirm(paramsPaymentConfirm);

            if(confirmPaymentIntent.getStatus().equals("succeeded")){
                paymentModel.setPaymentControl(PaymentControl.EFFECTED);
                paymentModel.setPaymentMessage("Payment effected - paymentIntent: "+paymentIntentId);
                paymentModel.getPaymentCompletionDate(LocalDateTime.now(ZoneId.of("UTC")));
            }else{
                paymentModel.setPaymentControl(PaymentControl.ERROR);
                paymentModel.setPaymentMessage("Payment error v1 - paymentIntent: " +paymentIntentId);
            }

        } catch (CardException e) {
            // Since it's a decline, CardException will be caught
            System.out.println("Payment refused {}" + e.getMessage());
            try{
                paymentModel.setPaymentControl(PaymentControl.REFUSED);
                PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
                paymentModel.setPaymentMessage("Payment refused v1 - paymentIntent: " +paymentIntentId+
                        " cause: " +paymentIntent.getLastPaymentError().getCode() +
                        " message: " + paymentIntent.getLastPaymentError().getMessage() );
                System.out.println("Status is: " + e.getCode());
                System.out.println("Message is: " + e.getMessage());

            }catch (Exception exception){
                paymentModel.setPaymentControl(PaymentControl.ERROR);
                paymentModel.setPaymentMessage("Payment error v2 - paymentIntent: " +paymentIntentId);
            }

        } catch (RateLimitException e) {
            // Too many requests made to the API too quickly
            System.out.println("Message(1) is: " + e.getMessage());

        } catch (InvalidRequestException e) {
            // Invalid parameters were supplied to Stripe's API
            System.out.println("Message(2) is: " + e.getMessage());
        } catch (AuthenticationException e) {
            // Authentication with Stripe's API failed
            // (maybe you changed API keys recently)
            System.out.println("Message(3) is: " + e.getMessage());

        } catch (StripeException e) {
            // Display a very generic error to the user, and maybe send
            // yourself an email
            System.out.println("Message(4) is: " + e.getMessage());

        } catch (Exception e){
            System.out.println("Message(5) is: " + e.getMessage());
            paymentModel.setPaymentControl(PaymentControl.ERROR);
            paymentModel.setPaymentMessage("Payment error v2 - paymentIntent: " +paymentIntentId);
        }
        return paymentModel;
    }
}
