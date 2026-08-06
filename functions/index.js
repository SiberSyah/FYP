const {onRequest} = require("firebase-functions/v2/https");
const {defineSecret} = require("firebase-functions/params");
const logger = require("firebase-functions/logger");
const Stripe = require("stripe");

const stripeSecretKey = defineSecret("STRIPE_SECRET_KEY");

exports.createPaymentIntent = onRequest(
    {
      cors: true,
      secrets: [stripeSecretKey],
    },
    async (req, res) => {
      try {
        if (req.method !== "POST") {
          return res.status(405).json({
            error: "Method not allowed. Use POST.",
          });
        }

        const stripe = new Stripe(stripeSecretKey.value());

        const {
          amount,
          currency = "myr",
        } = req.body || {};

        if (!amount) {
          return res.status(400).json({
            error: "Amount is required.",
          });
        }

        const numericAmount = Number(amount);

        if (!Number.isFinite(numericAmount) || numericAmount <= 0) {
          return res.status(400).json({
            error: "Invalid amount.",
          });
        }

        // Android sends RM amount.
        // Example: RM10.00 → 1000 sen.
        const amountInSen = Math.round(numericAmount * 100);

        const paymentIntent =
            await stripe.paymentIntents.create({
              amount: amountInSen,
              currency: currency.toLowerCase(),
              automatic_payment_methods: {
                enabled: true,
              },
            });

        logger.info("Stripe PaymentIntent created", {
          paymentIntentId: paymentIntent.id,
        });

        return res.status(200).json({
          success: true,
          paymentIntentId: paymentIntent.id,
          clientSecret: paymentIntent.client_secret,
        });
      } catch (error) {
        logger.error("Stripe PaymentIntent error", error);

        return res.status(500).json({
          success: false,
          error: error.message,
        });
      }
    },
);
