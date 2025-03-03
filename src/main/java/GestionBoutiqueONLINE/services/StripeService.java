package GestionBoutiqueONLINE.services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

public class StripeService {

    // Clé secrète Stripe (à ne pas exposer publiquement)
    private static final String STRIPE_SECRET_KEY = "sk_test_51QwBpAFlgJcRyS6REtYtP6gMrFKQs7g0e8WzHmBwaxlAA9ql96fmEqXVEBNFVtpT7fjf5IbNFAPm2KaxgVJ8PqXc00tjOL4j2t";

    public StripeService() {
        Stripe.apiKey = STRIPE_SECRET_KEY;
    }

    // Créer un PaymentIntent
    public String createPaymentIntent(int montant, String currency) throws StripeException {
        long amountInCents = montant * 100L; // Convertir en centimes

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amountInCents)
                .setCurrency(currency)
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .setAllowRedirects(PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER) // Désactiver les redirections
                                .build()
                )
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);
        return paymentIntent.getClientSecret();
    }

    // Confirmer le paiement
    public void confirmPayment(String paymentMethodId, long amount, String currency) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCurrency(currency)
                .setPaymentMethod(paymentMethodId)
                .setConfirm(true) // Confirmer le paiement immédiatement
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        if (!paymentIntent.getStatus().equals("succeeded")) {
            System.out.println("Le paiement n'a pas réussi. Statut : " + paymentIntent.getStatus());        }
    }

    // Récupérer l'intention de paiement
    public PaymentIntent getPaymentIntent(String paymentMethodId) throws StripeException {
        return PaymentIntent.retrieve(paymentMethodId);
    }
}
