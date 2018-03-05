/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * server.java
 *
 * Created on Aug 5, 2014, 8:18:05 PM
 */
package xchatbox_v06;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.InetAddress;
import java.net.ServerSocket;
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
public class server extends javax.swing.JFrame {

    static ServerSocket serverSocket; // we need this line to open port on our
    // computer
    static Socket socket; // and this socket to accept remote connections
    static int client_user_list[] = new int[100]; // array "client_user_list" is unique id of client , and we set 100 clients to allow on server
    // every client have his own unique input and output stream, so we need
    // array to do job
    static ObjectInputStream in[] = new ObjectInputStream[client_user_list.length + 1]; // set input stream to 100 users (we don't use index_0 here, because 0 will destroy everything)
    static ObjectOutputStream out[] = new ObjectOutputStream[client_user_list.length + 1]; // set output stream to 100  users (we  don't use here index_0 too)
    static String username[] = new String[client_user_list.length + 1];
    static String sending_text; // string sending_text is for receiving and sending text
    static boolean signal; // signal is true when port is created
    static boolean start = true; // check the connection

    /** Creates new form server */
    public server() {
        initComponents();
    }

    // method where server receives message from one client and send it to all
    // other clients
    void send_messages_to_all(int cli, String por) // excluding the
    // sender_client
    {
        // String[] por2 = por.split("::");

        String[] por3 = por.split("<");
         System.out.println("por3_length = "+por3.length);

        // for showing emoticons
        String emo = null;
        int emo_value = 0;
        String[] emoticon = por3[0].split("~");
        int length_emo_array = emoticon.length;
        if (length_emo_array == 2) {
            emo_value = 1;
            // System.out.println(emo_value);
            // System.out.println(emoticon[0]);
            // System.out.println(emoticon[1]); //need to use for emoticons
        }

        int value = 0;
        
         String [] por4 = por3[1].split(",");
        
        if (por3.length < 3 && por3[1].equals("EVERYONE")) { // Handling Global
            // msg
            value = 1;
        } else if (por3.length < 3 && !por3[1].equals("EVERYONE") && por4.length == 1) { // Handling
            // 1to1
            // msg
            value = 0;
            if (emo_value == 1) {
                emo = find_emo(emoticon[1]);
            } else {
                emo = null;
            }
            indi_message(por3[1], por3[0], emoticon[0], emo); // name,msg
        } else if (por4.length > 1) { // Handling Group msg

            System.out.println("GROUP MSG TO = "+por3[0]);
           
            for (int i = 0; i < por4.length; i++) { // 0 = the msg
               System.out.println("POR4 : "+por4[i]);
            }
            
            
            if (emo_value == 1) {
                emo = find_emo(emoticon[1]);
            } else {
                emo = null;
            }

            for (int i = 0; i < por4.length; i++) { // 0 = the msg
               indi_message(por4[i], por3[0], emoticon[0], emo); //name,msg,emo,emo
            }
        }

        if (value == 1) {

            for (int i = 0; i < client_user_list.length; i++) // for loop trying to send message from server to all clients
            {
                if (client_user_list[i] != 0) // this line stop server to send messages to offline clients  (if"client_user_list[X] = 0" *ignoring)
                {
                    if (cli != i + 1) // stops the repetition to the sent client
                    {
                        try {

                            if (emo_value == 0) {
                                out[i + 1].writeObject(por3[0]);
                                out[i + 1].flush(); // clear - out
                            } else {
                                String emoticon_string = find_emo(emoticon[1]);
                                out[i + 1].writeObject(emoticon[0]
                                        + emoticon_string);
                                out[i + 1].flush(); // clear - out

                            }
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                }
            }
        } else {
            // indi_message(por2);
        }
    }

    // method for getting emoticons
    String find_emo(String e) {
        if (e.equals("D")) {
            return " >E-SMILE<";
        } else if (e.equals(")")) {
            return " >SMILE<";
        } else if (e.equals("(")) {
            return " >SAD<";
        } else if (e.equals("T_T")) {
            return " >CRYING<";
        } else if (e.equals("xD")) {
            return " >x_x<";
        } else if (e.equals("@")) {
            return " >ANGRY<";
        } else if (e.equals("-_-")) {
            return " >ANNOYED<";
        } else if (e.equals("bye")) {
            return " >GOOD BYE<";
        }

        return ">ERROR_EMOTICON<";
    }

    // method where Server send messages to all clients (EVERYONE _ GLOBAL _MESSAGE)
    void global_message(String por) {
        for (int i = 0; i < client_user_list.length; i++) // for loop trying to send message from server to all clients
        {
            if (client_user_list[i] != 0) // this line stop server to send messages to offline clients
            {
                try {
                    out[i + 1].writeObject(por);
                    out[i + 1].flush();
                } catch (Exception e) {
                }
            }
        }
    }

    // method where individual client to client chat
    void indi_message(String name, String msg, String emo0, String emo1) {
        String to_send = name;
        String the_msg = msg;
        int user_id_to_send = 0;

        /*
         * //The List of users for (int i = 0; i < client_user_list.length; i++)
         * { System.out.println("User " + i + " : " +
         * username[client_user_list[i]]); }
         */

        for (int i = 0; i < client_user_list.length; i++) {
            // String[] part2 = part[0].split(">");
            // to_send = part2[1];

            String user_name_check = username[client_user_list[i]];

            if (user_name_check.equals(to_send)) {
                user_id_to_send = i + 1;
                break;
            }
        }

        for (int i = 0; i < client_user_list.length; i++) {
            if (client_user_list[i] != 0) {
                if (client_user_list[i] == user_id_to_send) {
                    try {
                        if (emo1 == null) {
                            out[i + 1].writeObject(the_msg);
                            out[i + 1].flush();
                        } else {
                            out[i + 1].writeObject(emo0 + " " + emo1);
                            out[i + 1].flush();
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    // individually inform the users that they are connected to the server
    void connection_message(int id, String por) {
        try {
            out[id].writeObject(por);
            out[id].flush();
        } catch (Exception e) {
        }
    }

    // THREADS SECTION
    public class AcceptNewClients implements Runnable {

        Thread t;

        AcceptNewClients(String sss) {
            t = new Thread(this, sss);
            t.start();
        }

        // if socket is created wait for new clients to join channel
        public void run() {
            while (true) {
                try {
                    TA_Chat.append("Waiting for connection...\n");
                    TA_Chat.setCaretPosition(TA_Chat.getText().length());
                    try {
                        socket = serverSocket.accept(); // wait for someone to
                        // join the server
                    } catch (Exception ex) {
                        break;
                    }

                    TA_Chat.append("Received connection from : "
                            + socket.getInetAddress().getHostName() + "\n");
                    TA_Chat.setCaretPosition(TA_Chat.getText().length());

                    // for every new client
                    int client_id_catch = 0; // we need client_id_catch to catch
                    // client id
                    for (int i = 0; i < client_user_list.length; i++) {
                        if (client_user_list[i] == 0) // find first 0 or"empty slot" in array and set client_id number
                        {
                            client_user_list[i] = i + 1; // sets the client number indicating new client added
                            client_id_catch = i; // new client's id in the array
                            break; // on first 0 in array, break for-loop
                        }
                    }

                    // New client's output-input stream
                    out[client_user_list[client_id_catch]] = new ObjectOutputStream(
                            socket.getOutputStream());
                    out[client_user_list[client_id_catch]].flush();
                    in[client_user_list[client_id_catch]] = new ObjectInputStream(
                            socket.getInputStream());

                    TA_Chat.append("Client:[" + client_user_list[client_id_catch] + "] Successfully Logged!\n");  
                    //System.out.println(username[client_user_list[client_id_catch]]);
      
                    
                   

                    connection_message(client_user_list[client_id_catch],
                            "Connected Succesfully to the server !"); // send message to client[id] that is connected on server
                    username[client_user_list[client_id_catch]] = (String) in[client_user_list[client_id_catch]].readObject(); // receive username from client
                    global_message(username[client_user_list[client_id_catch]]
                            + " is online now."); // global message that this
                    // client has connected
                    
                    String insert_name = username[client_user_list[client_id_catch]];
                   // System.out.println(username[client_user_list[client_id_catch]]);
                    DBConnect connect = new DBConnect();        
                       connect.Insert(insert_name);     

                    ReceiveMsgs ob2 = new ReceiveMsgs(
                            client_user_list[client_id_catch],
                            "StartReceiveMsgs_"
                            + client_user_list[client_id_catch]); // making new thread for every new client
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // run this Thread only one time, so server can start sending
                // messages to clients when someone is connected, so we need
                // boolean start
                if (start == true) {
                    send_thread_msg(); // send message Thread
                    start = false;
                }
            }
        }
    }

    // part 3
    // Server to all client part 4
    public void send_thread_msg() {
        TF_sending_text.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    String sending_text2 = TF_sending_text.getText(); // sends a message
                    global_message("<Server>" + sending_text2); // globally send the message
                    TA_Chat.append("<Server>" + sending_text2 + "\n"); // appearence
                    TA_Chat.setCaretPosition(TA_Chat.getText().length());
                    TF_sending_text.setText(""); // default
                }
            }

            public void keyReleased(KeyEvent e) {
            }

            public void keyTyped(KeyEvent e) {
            }
        });
    }

    // receive message from one client and send to other clients
    public class ReceiveMsgs implements Runnable {

        int C_id;
        ReceiveMsgs ob1;
        Thread t;

        ReceiveMsgs(int C_id, String sss) {
            this.C_id = C_id; // Receive Client_id to C_id , so we can create
            // new thread with specific id
            t = new Thread(this, sss);
            t.start();
        }

        public void run() {
            do {
                try {
                    sending_text = (String) in[C_id].readObject(); // in : try to receive messages
                    TA_Chat.append("<Join_" + C_id + ">" + sending_text + "\n"); // print received message on server


                } catch (Exception e) {
                    sending_text = "bye_bye";
                }// server can't receive message from client, set sending_text
                // to "bye_bye", exit from do-while loop and then close
                // thread with client

                if (sending_text.equals("bye_bye")) // if sending_text = "bye_bye" , close thread and connection with offline client
                {
                    TA_Chat.append("<Join_" + C_id + "> <Now offline!>\n"); // print on server that client is offline					

                    send_messages_to_all(C_id, "<" + username[C_id]
                            + "> is now offline!"); // send to all that client
                    // is offline
                    // if says bye_bye then do this to disconnect this client
                    // completely
                    client_user_list[C_id - 1] = 0; // if someone leave or disconnect, clear that slot and set it to 0 in array, so other clients can join
                    username[C_id] = ""; // free nick name slot
                    try {
                        in[C_id].close(); // close input connection with client
                        out[C_id].close(); // close output connection with client
                    } catch (Exception ex) {
                    }
                } // else send message to all users
                else {
                    send_messages_to_all(C_id, sending_text); // send message to
                }																// all users
            } while (!sending_text.equals("bye_bye"));
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
        jLabel2 = new javax.swing.JLabel();
        TF_IP = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        TA_Chat = new javax.swing.JTextArea();
        TF_sending_text = new javax.swing.JTextField();
        B_StartServer = new javax.swing.JButton();
        B_Disconnect = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TF_Online = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        B_Refresh = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Port : ");

        TF_Port.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TF_Port.setText("5898");
        TF_Port.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TF_PortActionPerformed(evt);
            }
        });

        jLabel2.setText("IP : ");

        TF_IP.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TF_IP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TF_IPActionPerformed(evt);
            }
        });

        TA_Chat.setColumns(20);
        TA_Chat.setEditable(false);
        TA_Chat.setRows(5);
        jScrollPane1.setViewportView(TA_Chat);

        TF_sending_text.setFont(new java.awt.Font("Times New Roman", 0, 14));
        TF_sending_text.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        TF_sending_text.setAlignmentX(0.1F);
        TF_sending_text.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        B_StartServer.setText("Start Server");
        B_StartServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B_StartServerActionPerformed(evt);
            }
        });

        B_Disconnect.setText("Disconnect Server");
        B_Disconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B_DisconnectActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 10));
        jLabel3.setText("Created By XIIX");

        TF_Online.setColumns(20);
        TF_Online.setEditable(false);
        TF_Online.setRows(5);
        jScrollPane2.setViewportView(TF_Online);

        jLabel4.setText("Online Users");

        B_Refresh.setText("Refresh");
        B_Refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B_RefreshActionPerformed(evt);
            }
        });

        jLabel5.setText("Name");

        jLabel6.setText("Logged In");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(B_StartServer)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(B_Disconnect))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(TF_Port, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(TF_IP))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
                            .addComponent(TF_sending_text))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel4)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(49, 49, 49)
                                .addComponent(B_Refresh))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(149, 149, 149)
                        .addComponent(jLabel3)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(TF_Port, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(TF_IP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(B_StartServer)
                            .addComponent(B_Disconnect))
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5))
                        .addGap(3, 3, 3)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(TF_sending_text, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3))
                    .addComponent(B_Refresh))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void TF_PortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TF_PortActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_TF_PortActionPerformed

private void B_StartServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B_StartServerActionPerformed
// TODO add your handling code here:
    try {
        // get the typed ip
        InetAddress thisIp = InetAddress.getLocalHost(); 
        String ip = thisIp.getHostAddress().toString(); 
        TF_IP.setText(ip); // editis the field
    } catch (Exception ex) {
        TF_IP.setText("Offline!");
    }

    int p = 0;
    try {
        String port = TF_Port.getText(); // gets the port
        // and sets in
        // port val. as
        // string.
        p = Integer.parseInt(port); // parse into int
        TA_Chat.append("Creating a connection on port " + p
                + "...\n"); // command from the server
        TA_Chat.setCaretPosition(TA_Chat.getText().length()); 
        serverSocket = new ServerSocket(p); 
        
        signal = true; // could successfully set the port
    } catch (Exception ex) {
        TA_Chat.append("Error: Port " + p
                + " Busy or Couldn't Connect !\n");
        TA_Chat.setCaretPosition(TA_Chat.getText().length());
        signal = false; // error
    }

    if (signal == true) {
        AcceptNewClients ob1 = new AcceptNewClients(
                "RunServer"); // calls with RunServer
        B_StartServer.setEnabled(false); 
        B_Disconnect.setEnabled(true); 
    }
}//GEN-LAST:event_B_StartServerActionPerformed

private void TF_IPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TF_IPActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_TF_IPActionPerformed

private void B_DisconnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B_DisconnectActionPerformed
// TODO add your handling code here:
    
    DBConnect connect = new DBConnect();
    connect.DeleteAll();
    
    TA_Chat.setCaretPosition(TA_Chat.getText().length());
    try {
        serverSocket.close(); // closes the socket if
        // disconnect button pressed
    } catch (Exception ex) {
    }

    for (int i = 0; i < in.length; i++) {
        try // closes all the connections
        {
            in[i].close();
            out[i].close();
        } catch (Exception ex) {
        }
    }
    TA_Chat.append("Server is Disconnected !\n");
    B_StartServer.setEnabled(true);
    B_Disconnect.setEnabled(false);
}//GEN-LAST:event_B_DisconnectActionPerformed

private void B_RefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B_RefreshActionPerformed
// TODO add your handling code here:

    TF_Online.setText("");
    
    DBConnect connect = new DBConnect();
    
    String[] array = connect.getData();
    String [] array2 = connect.getTime();
    int check = 0;   
    
    for (int i = 0; i < array.length; i++) {
        if (array[i] != null) {
            check = 1;
            TF_Online.append(array[i]+"      "+array2[i] + "\n");
            TF_Online.setCaretPosition(TF_Online.getText().length());
        }
    }
     
    if(check == 0 ){
        TF_Online.append(" ~ No Online Users Found ~" + "\n");
            TF_Online.setCaretPosition(TF_Online.getText().length());
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
            java.util.logging.Logger.getLogger(server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(server.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new server().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton B_Disconnect;
    private javax.swing.JButton B_Refresh;
    private javax.swing.JButton B_StartServer;
    private javax.swing.JTextArea TA_Chat;
    private javax.swing.JTextField TF_IP;
    private javax.swing.JTextArea TF_Online;
    private javax.swing.JTextField TF_Port;
    private javax.swing.JTextField TF_sending_text;
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
