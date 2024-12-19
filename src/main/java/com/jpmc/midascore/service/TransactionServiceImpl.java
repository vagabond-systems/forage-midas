package com.jpmc.midascore.service;
import com.jpmc.midascore.client.IncentiveClient;
import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.producer.KafkaProducer;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import jakarta.transaction.InvalidTransactionException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.jpmc.midascore.entity.UserRecord;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final KafkaProducer kafkaProducer;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final IncentiveClient incentiveClient;
    private final RestTemplate restTemplate;

    @Override
    public void processTransaction(Transaction transaction) {
        try {
            // Validate sender and receiver
            Optional<UserRecord> senderOpt = Optional.ofNullable(userRepository.findById(transaction.getSenderId()));
            Optional<UserRecord> receiverOpt = Optional
                    .ofNullable(userRepository.findById(transaction.getRecipientId()));

            if (senderOpt.toString().isEmpty() || senderOpt.toString().isBlank() || receiverOpt.toString().isEmpty()
                    || receiverOpt.toString().isBlank()) {
                log.error("Invalid sender or receiver for transaction: " + transaction);
                kafkaProducer.sendToDeadLetter(transaction.toString());
                throw new InvalidTransactionException("Invalid sender or receiver for transaction: " + transaction);
            }
            // Set sender and receiver
            UserRecord sender = senderOpt.orElse(null);

            UserRecord receiver = receiverOpt.orElse(null);

            // Validate Sender Balance
            if (sender.getBalance() < transaction.getAmount()) {
                log.atInfo().log("Insufficient balance for transaction: " + transaction);
                log.info("Insufficient balance for transaction: {}, " +
                                "SenderId: {}, \n" +
                                "Sender Balance: {}",
                        transaction, transaction.getSenderId(), sender.getBalance());
                kafkaProducer.sendToDeadLetter(transaction.toString());
                throw new InvalidTransactionException("Insufficient balance for transaction:" +
                        " " + transaction);
            }

            TransactionRecord transactionRecord = new TransactionRecord();
            // TODO - Call the Incentive Service/API
            Incentive incentive = incentiveClient.getIncentive(transaction);
            log.info("Incentive Service Response: " + incentive.toString());
            // log API Response from Incentive Service
            transactionRecord.setIncentive(incentive.getAmount());
            transaction.setIncentive(transactionRecord.getIncentive());
            if (incentive == null || incentive.getAmount() < 0) {
                log.error("Incentive Service is down. Cannot process transaction: " + transaction);
                kafkaProducer.sendToDeadLetter(transaction.toString());
                throw new InvalidTransactionException("Incentive Service is down. Cannot process transaction: " + transaction);
            }
            log.info("Incentive: " + incentive);
            log.info("Incentive correctly set in Transaction Object: " + transaction.getIncentive());
            // Record the Transaction
            transactionRecord.setSender(sender);
            transactionRecord.setRecipient(receiver);
            transactionRecord.setAmount(transaction.getAmount());
            transactionRecord.setIncentive(incentive.getAmount());
            transactionRepository.saveAndFlush(transactionRecord);
            log.info("Transaction Successfully Recorded: " + transactionRecord);
            log.info("Sender's Current Balance: " + sender.getBalance());
            log.info("Recipient's Current Balance: " + receiver.getBalance());
            // Adjust The Balances
            float senderNewBalance = roundToTwoDecimalPlaces(sender.getBalance() - transaction.getAmount());
            float recipientNewBalance =
                    roundToTwoDecimalPlaces(receiver.getBalance() + transaction.getAmount() + transaction.getIncentive());

            updateByBalanceId(sender.getId(), senderNewBalance);
            log.info("Sender Information: {}", sender);
            updateByBalanceId(receiver.getId(), recipientNewBalance);
            log.info("Recipient's Information: {}", receiver);


        } catch (InvalidTransactionException error) {
            log.error("Error processing transaction: " + transaction, error);
        }
    }

    @Override
    public void updateByBalanceId(long id, float balance) {
        userRepository.updateBalanceById(id, balance);
    }

    public static float roundToTwoDecimalPlaces(float value) {
        BigDecimal bd = new BigDecimal(Float.toString(value));
        bd = bd.setScale(2, RoundingMode.HALF_DOWN);
        return bd.floatValue();
    }

    @Override
    public float getUserBalanceByName(String name) {
        UserRecord userRecord = userRepository.findByName(name);
        if (userRecord == null) {
            log.error("User not found: " + name);
            return -1;
        }
        return userRecord.getBalance();
    }

}