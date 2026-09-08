const {onRequest} = require("firebase-functions/v2/https");
const {defineSecret} = require("firebase-functions/params");
const logger = require("firebase-functions/logger");

const Stripe = require("stripe");
const nodemailer = require("nodemailer");

const {initializeApp} = require("firebase-admin/app");

initializeApp();


// ===============================
// SECRETS
// ===============================

const stripeSecretKey = defineSecret("STRIPE_SECRET_KEY");

const gmailPassword = defineSecret("GMAIL_APP_PASSWORD");


// ===============================
// STRIPE PAYMENT
// ===============================

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


        const stripe = new Stripe(
            stripeSecretKey.value(),
        );


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


        if (
          !Number.isFinite(numericAmount) ||
          numericAmount <= 0
        ) {
          return res.status(400).json({
            error: "Invalid amount.",
          });
        }


        // RM to sen

        const amountInSen =
            Math.round(
                numericAmount * 100,
            );


        const paymentIntent =
            await stripe.paymentIntents.create({

              amount: amountInSen,

              currency:
                currency.toLowerCase(),


              automatic_payment_methods: {

                enabled: true,

              },

            });


        logger.info(
            "Stripe PaymentIntent created",
            {
              paymentIntentId:
                paymentIntent.id,
            },
        );


        return res.status(200).json({

          success: true,

          paymentIntentId:
            paymentIntent.id,

          clientSecret:
            paymentIntent.client_secret,

        });
      } catch (error) {
        logger.error(
            "Stripe PaymentIntent error",
            error,
        );


        return res.status(500).json({

          success: false,

          error: error.message,

        });
      }
    },
);


// ===============================
// SEND OTP EMAIL
// ===============================

exports.sendOTPEmail = onRequest(

    {

      cors: true,

      secrets: [
        gmailPassword,
      ],

    },


    async (req, res)=>{
      try {
        const {

          email,

          otp,


        } = req.body;


        if (!email || !otp) {
          return res.status(400).json({

            error:
            "Email and OTP required",

          });
        }


        const transporter =
            nodemailer.createTransport({

              service: "gmail",


              auth: {


                user:
                "encikaircond.service@gmail.com",


                pass:
                gmailPassword.value(),


              },


            });


        await transporter.sendMail({


          from:
          "Encik Aircond <encikaircond.service@gmail.com>",


          to: email,


          subject:
          "Encik Aircond Password Reset OTP",


          html: `


          <h2>Encik Aircond</h2>


          <p>Your password reset OTP is:</p>


          <h1>${otp}</h1>


          <p>This code will expire in 10 minutes.</p>


          `,


        });


        logger.info(
            "OTP email sent",
            {
              email: email,
            },
        );


        return res.status(200).json({

          success: true,

          message:
          "OTP sent successfully",

        });
      } catch (error) {
        logger.error(
            "OTP email error",
            error,
        );


        return res.status(500).json({

          success: false,

          error: error.message,

        });
      }
    },

);
