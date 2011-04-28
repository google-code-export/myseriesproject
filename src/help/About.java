/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * About.java
 *
 * Created on 3 Δεκ 2008, 7:04:40 μμ
 */
package help;

import java.awt.Cursor;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import myComponents.MyUsefulFunctions;
import myComponents.myGUI.MyDraggable;
import myseries.MySeries;
import tools.DesktopSupport;
import tools.Skin;
import tools.MySeriesLogger;

/**
 * About
 * @author lordovol
 */
public class About extends MyDraggable {

  private static final long serialVersionUID = 543546475L;
  /**
   * MySeries main form
   */
  private MySeries m;
  /**
   * The System properties String
   */
  private String propertyString;

  /** Creates new form About
   * @param m
   */
  public About(MySeries m) {
    this.m = m;
    MySeriesLogger.logger.log(Level.INFO, "Initializing components");
    initComponents();
    MySeriesLogger.logger.log(Level.FINE, "Components initialized");
    getProperties();
    setLocationRelativeTo(m);
    setVisible(true);
  }

  /**
   * Gets the system properties
   */
  private void getProperties() {
    MySeriesLogger.logger.log(Level.INFO, "Getting system properties");
    Properties props = System.getProperties();
    // Enumerate all system properties
    Enumeration<?> enumer = props.propertyNames();
    propertyString = "<table width=\"100\" cellspacing=\"0\" cellpadding=\"0\">";
    for (; enumer.hasMoreElements();) {
      String propName = (String) enumer.nextElement();
      // Get property value
      String propValue = (String) props.get(propName);
      propertyString += "<tr valign=\"top\"><td><b>" + propName + "</b>:</td><td> " + propValue.replaceAll(";", "; ") + "</td></tr>";
    }
    propertyString += "</table>";
    propertiesEditor.setText(propertyString);
    propertiesEditor.setCaretPosition(0);
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        main_panel = new javax.swing.JPanel();
        bt_cancel = new buttons.Button();
        inner_panel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textArea_contact = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        label_version = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        propertiesEditor = new javax.swing.JEditorPane();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        main_panel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        main_panel.setOpaque(false);

        bt_cancel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        bt_cancel.setText("");
        bt_cancel.setFocusable(false);
        bt_cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_cancelActionPerformed(evt);
            }
        });

        inner_panel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logo.png"))); // NOI18N

        jPanel2.setOpaque(false);

        jLabel5.setFont(jLabel5.getFont().deriveFont(jLabel5.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel5.setText(m.date);

        jLabel2.setFont(jLabel2.getFont().deriveFont(jLabel2.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel2.setText("Version :");

        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane1.setHorizontalScrollBar(null);
        jScrollPane1.setWheelScrollingEnabled(false);

        textArea_contact.setColumns(20);
        textArea_contact.setEditable(false);
        textArea_contact.setFont(textArea_contact.getFont().deriveFont(textArea_contact.getFont().getSize()-1f));
        textArea_contact.setRows(5);
        textArea_contact.setText("For any comments or suggestions\nmail me at lordovol@hotmail.com");
        textArea_contact.setBorder(null);
        textArea_contact.setOpaque(false);
        textArea_contact.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                textArea_contactMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                textArea_contactMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                textArea_contactMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(textArea_contact);

        jLabel3.setFont(jLabel3.getFont().deriveFont(jLabel3.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel3.setText("Contact :");

        label_version.setFont(label_version.getFont().deriveFont(label_version.getFont().getStyle() | java.awt.Font.BOLD));
        label_version.setText(m.version);

        jLabel4.setFont(jLabel4.getFont().deriveFont(jLabel4.getFont().getStyle() | java.awt.Font.BOLD));
        jLabel4.setText("Date:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label_version, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE))
                        .addContainerGap())
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 367, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(label_version, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        propertiesEditor.setContentType("text/html");
        propertiesEditor.setEditable(false);
        propertiesEditor.setFont(propertiesEditor.getFont());
        propertiesEditor.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        propertiesEditor.setOpaque(false);
        jScrollPane2.setViewportView(propertiesEditor);

        javax.swing.GroupLayout inner_panelLayout = new javax.swing.GroupLayout(inner_panel);
        inner_panel.setLayout(inner_panelLayout);
        inner_panelLayout.setHorizontalGroup(
            inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, inner_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 629, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, inner_panelLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        inner_panelLayout.setVerticalGroup(
            inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inner_panelLayout.createSequentialGroup()
                .addGroup(inner_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(inner_panelLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jLabel1))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 332, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel6.setFont(jLabel6.getFont().deriveFont(jLabel6.getFont().getStyle() | java.awt.Font.BOLD, jLabel6.getFont().getSize()+2));
        jLabel6.setForeground(Skin.getTitleColor());
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("About MySeries");

        javax.swing.GroupLayout main_panelLayout = new javax.swing.GroupLayout(main_panel);
        main_panel.setLayout(main_panelLayout);
        main_panelLayout.setHorizontalGroup(
            main_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(main_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(main_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(main_panelLayout.createSequentialGroup()
                        .addComponent(inner_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, main_panelLayout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 625, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bt_cancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        main_panelLayout.setVerticalGroup(
            main_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(main_panelLayout.createSequentialGroup()
                .addGroup(main_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bt_cancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(main_panelLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jLabel6)))
                .addGap(7, 7, 7)
                .addComponent(inner_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(main_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(main_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

  private void textArea_contactMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_textArea_contactMouseEntered
    if (DesktopSupport.isMailSupport()) {
      setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}//GEN-LAST:event_textArea_contactMouseEntered

  private void textArea_contactMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_textArea_contactMouseExited
    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
  }//GEN-LAST:event_textArea_contactMouseExited

  private void textArea_contactMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_textArea_contactMouseReleased
    if (!DesktopSupport.isMailSupport()) {
      MySeriesLogger.logger.log(Level.WARNING, "Emailing is not supported by the OS");
    }
    try {
      MySeriesLogger.logger.log(Level.INFO, "Opening email application");

      DesktopSupport.getDesktop().mail(
          new URI("mailto:lordovol@hotmail.com?subject=MySeries%20v" + MySeries.version));
      MySeriesLogger.logger.log(Level.FINE, "Email application opened");
    } catch (URISyntaxException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "There's an error interface the email subject", ex);
    } catch (IOException ex) {
      MySeriesLogger.logger.log(Level.SEVERE, "I/O Error", ex);
    }

  }//GEN-LAST:event_textArea_contactMouseReleased

  private void bt_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_cancelActionPerformed
    MySeriesLogger.logger.log(Level.INFO, "Closing window");
    dispose();
    MySeries.glassPane.deactivate();
  }//GEN-LAST:event_bt_cancelActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private buttons.Button bt_cancel;
    private javax.swing.JPanel inner_panel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel label_version;
    private javax.swing.JPanel main_panel;
    private javax.swing.JEditorPane propertiesEditor;
    private javax.swing.JTextArea textArea_contact;
    // End of variables declaration//GEN-END:variables
}
