package part2_preparation;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.util.Random;
import java.util.regex.Pattern;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Student Number: ST10487456 Full Name: Ndaedzo Tshiovhe  Assignment Part 2
 */
public final class Welcome extends javax.swing.JFrame {

    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton btnSendMessage;
    private javax.swing.JButton btnComingSoon;
    private javax.swing.JButton btnQuit;

    /**
     * Creates new form Welcome
     */
    public Welcome() {
        setSize(500, 400);
        //  setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        // sHOW the message to the user when they click close (X)
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                JOptionPane.showMessageDialog(null, "Please use the 'Quit' button to exit the app.");
            }
        });

        setTitle("QuickChat");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        jLabel1 = new javax.swing.JLabel("Welcome to QuickChat");
      btnSendMessage =  new javax.swing.JButton("1. send message");
        btnComingSoon = new javax.swing.JButton("2. Show report");
        btnQuit = new javax.swing.JButton("3. Quit");

//        btnSendMessage.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent evt) {
//                JOptionPane.showMessageDialog(null, "Send Message clicked!");
//            }
//        });
//        btnSendMessage.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent evt) {
//                String input = JOptionPane.showInputDialog(null, "How many messages do you want to send?");
//
//                if (input != null) {
//                    try {
//                        int count = Integer.parseInt(input.trim());
//
//                        if (count <= 0) {
//                            JOptionPane.showMessageDialog(null, "Please enter a number greater than zero.");
//                            return;
//                        }
//
//                        List<String> messages = new ArrayList<>();
//                        for (int i = 1; i <= count; i++) {
//                            String message = JOptionPane.showInputDialog(null, "Enter message " + i + ":");
//                            if (message != null) {
//                                messages.add(message);
//                            } else {
//                                messages.add("[No Message Entered]");
//                            }
//                        }
//
//                        // Optional: Show all messages back to the user
//                        StringBuilder summary = new StringBuilder("You entered the following messages:\n");
//                        for (int i = 0; i < messages.size(); i++) {
//                            summary.append((i + 1) + ". " + messages.get(i) + "\n");
//                        }
//
//                        JOptionPane.showMessageDialog(null, summary.toString());
//
//                    } catch (NumberFormatException e) {
//                        JOptionPane.showMessageDialog(null, "Invalid input. Please enter a numeric value.");
//                    }
//                }
//            }
//        });

btnSendMessage.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent evt) {
        String input = JOptionPane.showInputDialog(null, "How many messages do you want to send?");
        if (input == null) return;

        int count;
        try {
            count = Integer.parseInt(input.trim());
            if (count <= 0) {
                JOptionPane.showMessageDialog(null, "Please enter a number greater than zero.");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a numeric value.");
            return;
        }

        List<Message> messagesToStore = new ArrayList<>();
        int messagesSent = 0;

        for (int i = 0; i < count; i++) {
            String recipient = JOptionPane.showInputDialog(null, "Enter recipient phone number (e.g., +2783123456):");
            if (recipient == null) return;

            if (!recipient.matches("^\\+\\d{10}$")) {
                JOptionPane.showMessageDialog(null, "Invalid phone number. Must be + followed by 10 digits.");
                i--; // retry
                continue;
            }

            String messageText = JOptionPane.showInputDialog(null, "Enter your message:");
            if (messageText == null) return;

            if (messageText.length() > 250) {
                JOptionPane.showMessageDialog(null, "Message exceeds 250 characters.");
                i--; // retry
                continue;
            }

            if (messageText.length() > 50) {
                JOptionPane.showMessageDialog(null, "Please enter a message of less than 50 characters.");
                i--; // retry
                continue;
            }

            // Generate 10-digit message ID
            String msgId = String.format("%010d", new Random().nextInt(1_000_000_000));

            // Generate hash
            String[] words = messageText.trim().split("\\s+");
            String firstWord = words[0];
            String lastWord = words[words.length - 1];
            String hash = (msgId.substring(0, 2) + ":" + (i + 1) + ":" + firstWord + lastWord).toUpperCase();

            Message msg = new Message(msgId, i + 1, recipient, messageText, hash);  // Store the correct count

            Object[] options = {"Send Message", "Disregard Message", "Store Message to Send Later"};
            int choice = JOptionPane.showOptionDialog(null, "Choose an action:",
                    "Message Options", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, options, options[0]);

            if (choice == 0) {
                // Show full message details in a single dialog
                JOptionPane.showMessageDialog(null,
                        "Message Sent:\n\n" +
                        "Message ID: " + msg.id + "\n" +
                        "Hash: " + msg.hash + "\n" +
                        "Recipient: " + msg.recipient + "\n" +
                        "Message: " + msg.content);

                messagesToStore.add(msg); // also store
                messagesSent++;
            } else if (choice == 1) {
                JOptionPane.showMessageDialog(null, "Message disregarded.");
                continue;
            } else if (choice == 2) {
                messagesToStore.add(msg);
                JOptionPane.showMessageDialog(null, "Message stored.");
            }
        }

        // Store to JSON if there are messages to store
        if (!messagesToStore.isEmpty()) {
            JSONArray jsonMessages = new JSONArray();
            for (Message m : messagesToStore) {
                JSONObject obj = new JSONObject();
                obj.put("message_id", m.id);
                obj.put("count", m.count);
                obj.put("recipient", m.recipient);
                obj.put("content", m.content);
                obj.put("hash", m.hash);
                jsonMessages.add(obj);
            }

            try (FileWriter file = new FileWriter("stored_messages.json")) {
                file.write(jsonMessages.toJSONString());
                file.flush();
                JOptionPane.showMessageDialog(null, "Stored messages written to stored_messages.json");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error saving JSON: " + e.getMessage());
            }
        }

        // Show total messages sent
        JOptionPane.showMessageDialog(null, "Total messages sent: " + messagesSent);
    }
});

//        btnSendMessage.addActionListener(new ActionListener() {
//    public void actionPerformed(ActionEvent evt) {
//        String input = JOptionPane.showInputDialog(null, "How many messages do you want to send?");
//        if (input == null) return;
//
//        int count;
//        try {
//            count = Integer.parseInt(input.trim());
//            if (count <= 0) {
//                JOptionPane.showMessageDialog(null, "Please enter a number greater than zero.");
//                return;
//            }
//        } catch (NumberFormatException e) {
//            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a numeric value.");
//            return;
//        }
//
//        List<Message> messagesToStore = new ArrayList<>();
//        int msgCounter = 1;
//
//        for (int i = 0; i < count; i++) {
//            JPanel panel = new JPanel(new GridLayout(0, 1));
//            JTextField recipientField = new JTextField();
//            JTextField messageField = new JTextField();
//
//            panel.add(new JLabel("Enter recipient phone number (e.g., +2783123456):"));
//            panel.add(recipientField);
//            panel.add(new JLabel("Enter message (<= 50 characters):"));
//            panel.add(messageField);
//
//            int result = JOptionPane.showConfirmDialog(null, panel, "Message " + msgCounter,
//                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
//            if (result != JOptionPane.OK_OPTION) return;
//
//            String recipient = recipientField.getText().trim();
//            String messageText = messageField.getText().trim();
//
//            if (!recipient.matches("^\\+\\d{10}$")) {
//                JOptionPane.showMessageDialog(null, "Invalid phone number. Must be + followed by 10 digits.");
//                i--; continue;
//            }
//
//            if (messageText.length() > 250) {
//                JOptionPane.showMessageDialog(null, "Message exceeds 250 characters.");
//                i--; continue;
//            }
//
//            if (messageText.length() > 50) {
//                JOptionPane.showMessageDialog(null, "Please enter a message of less than 50 characters.");
//                i--; continue;
//            }
//
//            String msgId = String.format("%010d", new Random().nextInt(1_000_000_000));
//            String[] words = messageText.split("\\s+");
//            String firstWord = words[0];
//            String lastWord = words[words.length - 1];
//            String hash = (msgId.substring(0, 2) + ":" + msgCounter + ":" + firstWord + lastWord).toUpperCase();
//
//            Message msg = new Message(msgId, msgCounter, recipient, messageText, hash);
//
//            Object[] options = {"Send Message", "Disregard Message", "Store Message to Send Later"};
//            int choice = JOptionPane.showOptionDialog(null,
//                    "ID: " + msgId + "\nHash: " + hash + "\nRecipient: " + recipient + "\nMessage: " + messageText,
//                    "Message Action", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
//                    null, options, options[0]);
//
//            if (choice == 0) {
//                JOptionPane.showMessageDialog(null, "Message sent: " + hash);
//            } else if (choice == 1) {
//                JOptionPane.showMessageDialog(null, "Message disregarded.");
//                continue;
//            } else if (choice == 2) {
//                messagesToStore.add(msg);
//                JOptionPane.showMessageDialog(null, "Message stored for later.");
//            }
//
//            msgCounter++;
//        }
//
//        // Save messages to JSON
//        if (!messagesToStore.isEmpty()) {
//            JSONArray jsonMessages = new JSONArray();
//            for (Message m : messagesToStore) {
//                JSONObject obj = new JSONObject();
//                obj.put("message_id", m.id);
//                obj.put("count", m.count);
//                obj.put("recipient", m.recipient);
//                obj.put("content", m.content);
//                obj.put("hash", m.hash);
//                jsonMessages.add(obj);
//            }
//
//            try (FileWriter file = new FileWriter("stored_messages.json")) {
//                file.write(jsonMessages.toJSONString());
//                file.flush();
//                JOptionPane.showMessageDialog(null, "Stored messages written to stored_messages.json");
//            } catch (IOException e) {
//                JOptionPane.showMessageDialog(null, "Error saving JSON: " + e.getMessage());
//            }
//        }
//    }
//});

//btnSendMessage.addActionListener(new ActionListener() {
//    public void actionPerformed(ActionEvent evt) {
//        String input = JOptionPane.showInputDialog(null, "How many messages do you want to send?");
//        if (input == null) return;
//
//        int count;
//        try {
//            count = Integer.parseInt(input.trim());
//            if (count <= 0) {
//                JOptionPane.showMessageDialog(null, "Please enter a number greater than zero.");
//                return;
//            }
//        } catch (NumberFormatException e) {
//            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a numeric value.");
//            return;
//        }
//
//        List<Message> messagesToStore = new ArrayList<>();
//        int msgCounter = 1;
//        int messagesSent = 0;
//
//        for (int i = 0; i < count; i++) {
//            String recipient = JOptionPane.showInputDialog(null, "Enter recipient phone number (e.g., +2783123456):");
//            if (recipient == null) return;
//
//            if (!recipient.matches("^\\+\\d{10}$")) {
//                JOptionPane.showMessageDialog(null, "Invalid phone number. Must be + followed by 10 digits.");
//                i--; // retry
//                continue;
//            }
//
//            String messageText = JOptionPane.showInputDialog(null, "Enter your message:");
//            if (messageText == null) return;
//
//            if (messageText.length() > 250) {
//                JOptionPane.showMessageDialog(null, "Message exceeds 250 characters.");
//                i--; // retry
//                continue;
//            }
//
//            if (messageText.length() > 50) {
//                JOptionPane.showMessageDialog(null, "Please enter a message of less than 50 characters.");
//                i--; // retry
//                continue;
//            }
//
//            // Generate 10-digit message ID
//            String msgId = String.format("%010d", new Random().nextInt(1_000_000_000));
//
//            // Generate hash
//            String[] words = messageText.trim().split("\\s+");
//            String firstWord = words[0];
//            String lastWord = words[words.length - 1];
//            String hash = (msgId.substring(0, 2) + ":" + msgCounter + ":" + firstWord + lastWord).toUpperCase();
//
//            Message msg = new Message(msgId, msgCounter, recipient, messageText, hash);
//
//            Object[] options = {"Send Message", "Disregard Message", "Store Message to Send Later"};
//            int choice = JOptionPane.showOptionDialog(null, "Choose an action:",
//                    "Message Options", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
//                    null, options, options[0]);
//
//            if (choice == 0) {
//                // Show full message details
//                JOptionPane.showMessageDialog(null,
//                        "Message Sent:\n\n" +
//                        "Message ID: " + msg.id + "\n" +
//                        "Hash: " + msg.hash + "\n" +
//                        "Recipient: " + msg.recipient + "\n" +
//                        "Message: " + msg.content);
//
//                messagesToStore.add(msg); // also store
//                messagesSent++;
//            } else if (choice == 1) {
//                JOptionPane.showMessageDialog(null, "Message disregarded.");
//                continue;
//            } else if (choice == 2) {
//                messagesToStore.add(msg);
//                JOptionPane.showMessageDialog(null, "Message stored.");
//            }
//
//            msgCounter++;
//        }
//
//        // Store to JSON
//        if (!messagesToStore.isEmpty()) {
//            JSONArray jsonMessages = new JSONArray();
//            for (Message m : messagesToStore) {
//                JSONObject obj = new JSONObject();
//                obj.put("message_id", m.id);
//                obj.put("count", m.count);
//                obj.put("recipient", m.recipient);
//                obj.put("content", m.content);
//                obj.put("hash", m.hash);
//                jsonMessages.add(obj);
//            }
//
//            try (FileWriter file = new FileWriter("stored_messages.json")) {
//                file.write(jsonMessages.toJSONString());
//                file.flush();
//                JOptionPane.showMessageDialog(null, "Stored messages written to stored_messages.json");
//            } catch (IOException e) {
//                JOptionPane.showMessageDialog(null, "Error saving JSON: " + e.getMessage());
//            }
//        }
//
//        // Show total messages sent
//        JOptionPane.showMessageDialog(null, "Total messages sent: " + messagesSent);
//    }
//});

        btnComingSoon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                JOptionPane.showMessageDialog(null, "Coming Soon");
            }
        });

        btnQuit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                dispose();
            }
        });

        setupLayout();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setText("Welcome to QuickChat");

        jButton1.setText("send message");

        jButton2.setText("show report");

        jButton3.setText("Quit");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(290, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(240, 240, 240))
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 645, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(64, 64, 64)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addGap(18, 18, 18)
                .addComponent(jButton3)
                .addContainerGap(109, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Welcome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Welcome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Welcome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Welcome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Welcome().setVisible(true);
            }
        });
    }

    public void setupLayout() {
        // Layout
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jLabel1)
                        .addComponent(btnSendMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnComingSoon, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnQuit, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGap(20)
                        .addComponent(jLabel1)
                        .addGap(20)
                        .addComponent(btnSendMessage)
                        .addGap(10)
                        .addComponent(btnComingSoon)
                        .addGap(10)
                        .addComponent(btnQuit)
                        .addGap(20)
        );

        pack();
        setLocationRelativeTo(null); // Center the window

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
}
