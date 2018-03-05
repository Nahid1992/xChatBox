/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * client.java
 *
 * Created on Aug 5, 2014, 7:54:43 PM
 */
package xchatbox_v06;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author Nahid
 */
public class client extends javax.swing.JFrame {

    static Socket joinSocket;
    static ObjectOutputStream out;
    static ObjectInputStream in;
    static String message;
    static String ip;
    static boolean signal = false;
    static boolean key = true;
    static String cur_name;

    /** Creates new form client */
    public client() {
        initComponents();
    }

    // connect to server
    class Cthread implements Runnable {

        Cthread ob1;
        Thread t;

        Cthread() {
            t = new Thread(this, "RunServer");
            t.start();
        }

        public void run() {
            try {
                try {
                    out = new ObjectOutputStream(joinSocket.getOutputStream());
                    out.flush();
                    in = new ObjectInputStream(joinSocket.getInputStream());

                    message = (String) in.readObject(); // receives a message
                    // from

                    TA_Chat.append("<Server>" + message + "\n"); // receives a
                    // message

                    TA_Chat.setCaretPosition(TA_Chat.getText().length());
                    Send_message(TF_Nick.getText());
                    B_Connect.setEnabled(false);
                    B_Disconnect.setEnabled(true);
                } catch (Exception e) {
                }

                Receive_Message primaj1 = new Receive_Message();
                if (key == true) {
                    post_msg();
                    key = false;
                }

            } catch (Exception ex) {
            }
        }
    }

    public void post_msg() {
        TF_Message.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    String message2 = TF_Message.getText(); // sends a message
                    
                    if(!message2.equals("")){
                   // System.out.println("lol");                    
                    
                    TA_Chat.append(TF_Nick.getText() + ">" + message2 + "\n");
                    TA_Chat.setCaretPosition(TA_Chat.getText().length());
                    Send_message(TF_Nick.getText() + ">" + message2
                            + "<"+TF_To.getText()); // sends a message to the server
                    // (OUT)
                    TF_Message.setText(""); // makes it blank as default
                    }
                }
            }

            public void keyReleased(KeyEvent e) {
            }

            public void keyTyped(KeyEvent e) {
            }
        });
    }

    // Receive messages
    class Receive_Message implements Runnable {

        Receive_Message ob1;
        Thread t;

        Receive_Message() {
            t = new Thread(this, "Receive_Message");
            t.start();
        }

        public void run() {
            String online_users;
            try {
                do {
                    try {
                        message = (String) in.readObject(); // receives the message, in = msg from server's out

                        TA_Chat.append(message + "\n");

                    } catch (Exception ex) {
                        TA_Chat.append("Disconnected, Closing Down.\n");
                        TA_Chat.setCaretPosition(TA_Chat.getText().length());
                        
                        DBConnect connect = new DBConnect();
                        connect.Delete(cur_name);
                        
                        message = "cao";
                        try {
                            in.close();
                            out.close();
                            joinSocket.close();
                        } catch (Exception ex2) {
                        }

                        B_Connect.setEnabled(true);
                        B_Disconnect.setEnabled(false);
                    }
                } while (!message.equalsIgnoreCase("cao"));
            } catch (Exception ex) {
            }

        }
    }

    // send messages
    void Send_message(String por) {
        try {
            out.writeObject(por);
            out.flush();
        } catch (Exception e) {
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        TF_Port = new javax.swing.JTextField();
        B_Connect = new javax.swing.JButton();
        B_Disconnect = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        TF_IP = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        TF_Nick = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        TA_Chat = new javax.swing.JTextArea();
        TF_Message = new javax.swing.JTextField();
        B_Send = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        TF_To = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TA_Online = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        B_Refresh = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Port : ");

        TF_Port.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TF_Port.setText("5898");
        TF_Port.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TF_PortActionPerformed(evt);
            }
        });

        B_Connect.setText("Connect");
        B_Connect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B_ConnectActionPerformed(evt);
            }
        });

        B_Disconnect.setText("Disconnect");
        B_Disconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B_DisconnectActionPerformed(evt);
            }
        });

        jLabel2.setText("IP : ");

        TF_IP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TF_IPActionPerformed(evt);
            }
        });

        jLabel3.setText("Username : ");

        TF_Nick.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TF_Nick.setText("Nickname");

        TA_Chat.setColumns(20);
        TA_Chat.setEditable(false);
        TA_Chat.setRows(5);
        jScrollPane1.setViewportView(TA_Chat);

        TF_Message.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TF_MessageActionPerformed(evt);
            }
        });

        B_Send.setText("Send");
        B_Send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B_SendActionPerformed(evt);
            }
        });

        jLabel4.setText("To : ");

        TF_To.setText("EVERYONE");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 10));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Created By XIIX");

        TA_Online.setColumns(20);
        TA_Online.setEditable(false);
        TA_Online.setRows(5);
        jScrollPane2.setViewportView(TA_Online);

        jLabel6.setText("Online Users");

        B_Refresh.setText("Refresh");
        B_Refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B_RefreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(B_Send)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(TF_Port, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(B_Connect))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(TF_Nick)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel4)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(TF_To)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(B_Disconnect)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(TF_IP, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(TF_Message))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(76, 76, 76)
                                .addComponent(jLabel6))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(67, 67, 67)
                                .addComponent(B_Refresh))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(176, 176, 176)
                        .addComponent(jLabel5)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(TF_Port, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(B_Connect)
                    .addComponent(B_Disconnect)
                    .addComponent(jLabel2)
                    .addComponent(TF_IP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(TF_Nick, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TF_To, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(TF_Message, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(B_Send, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                        .addComponent(jLabel5))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(B_Refresh)
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void TF_PortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TF_PortActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_TF_PortActionPerformed

private void TF_IPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TF_IPActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_TF_IPActionPerformed

private void TF_MessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TF_MessageActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_TF_MessageActionPerformed

private void B_ConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B_ConnectActionPerformed
// TODO add your handling code here:
    int p = 0;
    
    try {
        String port = TF_Port.getText();
        p = Integer.parseInt(port);
        ip = TF_IP.getText();
        joinSocket = new Socket(ip, p); // created the socket
        signal = true; // Started
        TA_Chat.append("Trying to Connect IP:" + ip + " port:" + p
                + "\n");
        TA_Chat.setCaretPosition(TA_Chat.getText().length());
        
       // System.out.println(TF_Nick.getText());
         cur_name = TF_Nick.getText();
        

    } catch (Exception ex) {
        TA_Chat.append("Error: Server is unavailable!\n");
        TA_Chat.setCaretPosition(TA_Chat.getText().length());
        signal = false; // Couldn't connect
    }

    if (signal == true) {
        Cthread ob1 = new Cthread();
        B_Connect.setEnabled(false);
        B_Disconnect.setEnabled(true);
        TF_Nick.setEditable(false);
    }
}//GEN-LAST:event_B_ConnectActionPerformed

private void B_DisconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B_DisconnectActionPerformed
// TODO add your handling code here:

    TA_Chat.append("Client is Disconnected!\n");
    TA_Chat.setCaretPosition(TA_Chat.getText().length());
    DBConnect connect = new DBConnect();
    connect.Delete(cur_name);
    
    try {
        joinSocket.close();
    } catch (Exception ex) {
    }
    try {
        in.close();
    } catch (Exception ex) {
    }
    try {
        out.close();
    } catch (Exception ex) {
    }
    B_Connect.setEnabled(true);
    B_Disconnect.setEnabled(false);
    TF_Nick.setEditable(true);
}//GEN-LAST:event_B_DisconnectActionPerformed

private void B_SendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B_SendActionPerformed
// TODO add your handling code here:
	
	if(signal == true){
		
	
    String message2 = TF_Message.getText(); // sends a message
    
    if(!message2.equals("")){
    TA_Chat.append(TF_Nick.getText() + ">" + message2 + "\n");
    TA_Chat.setCaretPosition(TA_Chat.getText().length());
    
   // System.out.println(TF_Nick.getText());
    
    Send_message(TF_Nick.getText() + ">" + message2
            + "<"+TF_To.getText()); // sends a message to the server
    // (OUT)
    TF_Message.setText(""); // makes it blank as default
    }
	}else {
		TA_Chat.append("~ Not Connected ~" + "\n");
	    TA_Chat.setCaretPosition(TA_Chat.getText().length());
	}
}//GEN-LAST:event_B_SendActionPerformed

private void B_RefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B_RefreshActionPerformed
// TODO add your handling code here:   
    
    TA_Online.setText("");    
       
    DBConnect connect = new DBConnect();
    String[] array = connect.getData();
    int check = 0;

    for (int i = 0; i < array.length; i++) {
        if(array[i] != null && !array[i].equals(cur_name) ){
            check = 1;
           TA_Online.append(array[i]+"\n");
          TA_Online.setCaretPosition(TA_Online.getText().length());
        }
    }
    
    if(check == 0){
         TA_Online.append(" ~ No Other Online Users Found ~"+"\n");
          TA_Online.setCaretPosition(TA_Online.getText().length());
    }
    
}//GEN-LAST:event_B_RefreshActionPerformed

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
            java.util.logging.Logger.getLogger(client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new client().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton B_Connect;
    private javax.swing.JButton B_Disconnect;
    private javax.swing.JButton B_Refresh;
    private javax.swing.JButton B_Send;
    private javax.swing.JTextArea TA_Chat;
    private javax.swing.JTextArea TA_Online;
    private javax.swing.JTextField TF_IP;
    private javax.swing.JTextField TF_Message;
    private javax.swing.JTextField TF_Nick;
    private javax.swing.JTextField TF_Port;
    private javax.swing.JTextField TF_To;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
