package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConduit {

    private final UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public DatabaseConduit(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public void save(UserRecord userRecord) {
        userRepository.save(userRecord);
    }


    public void handleTransaction(Transaction transaction) {

        Long senderId = transaction.getSenderId();
        Long recipientId = transaction.getRecipientId();
        float amount = transaction.getAmount();   // ✅ FIXED (float)

        var senderOpt = userRepository.findById(senderId);
        var recipientOpt = userRepository.findById(recipientId);

        // validation: users must exist
        if (senderOpt.isEmpty() || recipientOpt.isEmpty()) {
            return;
        }

        UserRecord sender = senderOpt.get();
        UserRecord recipient = recipientOpt.get();

        // validation: sufficient balance
        if (sender.getBalance().getAmount() < amount) {
            return;
        }

        // update balances
        sender.getBalance().setAmount(
                sender.getBalance().getAmount() - amount
        );
        recipient.getBalance().setAmount(
                recipient.getBalance().getAmount() + amount
        );

        // persist users
        userRepository.save(sender);
        userRepository.save(recipient);

        // persist transaction
        TransactionRecord record =
                new TransactionRecord(amount, sender, recipient);

        transactionRepository.save(record);
    }
}
