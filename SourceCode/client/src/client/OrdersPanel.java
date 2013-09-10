/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import entity.Order;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicListUI;
import rbac.Permission;
import rbac.Session;
import rmi.IOrderController;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import static javax.swing.JComponent.TOOL_TIP_TEXT_KEY;
import javax.swing.event.ListDataListener;
import lombok.Data;

/**
 *
 * @author Louis DeRossi
 */
public class OrdersPanel extends javax.swing.JPanel {
    final private Session session;
    private OrderTableModel tableModel;
    final private IOrderController orderController;

    /**
     * Creates new form mani
     */
    public OrdersPanel(Session session, IOrderController orderController) throws RemoteException {
        initComponents();
        this.session = session;
        this.orderController = orderController;
        this.tableModel = new OrderTableModel();
        
        this.setupTable();
        this.setupPermissions();
    }
    
    private void setupTable() throws RemoteException {
        List<Order> allOrders = orderController.getAllOrders(session.getSessionId());
        for (Order order : allOrders) {
            this.tableModel.add(order);
        }
        
        this.orderTable.setModel(tableModel);
        this.orderTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (! e.getValueIsAdjusting()) {
                    // Show the first selected order in the side bar
                    int[] selection = orderTable.getSelectedRows();
                    final int index = orderTable.convertColumnIndexToModel(selection[0]);
                    final Order selectedOrder = tableModel.get(index);
                    orderID.setText(selectedOrder.getOrderId());
                    orderFrom.setText(selectedOrder.getSender());
                    orderTo.setText(selectedOrder.getReceiver());

                    if(!selectedOrder.getStatus().equals("PENDING")){
                        orderFrom.setEnabled(false);
                        orderTo.setEnabled(false);
                        orderWeight.setEnabled(false);
                        orderCharge.setEnabled(false);
                        cancelOrderButton.setEnabled(false);
                    }
                    
                    // Update the status UI
                    DefaultComboBoxModel statuses = new DefaultComboBoxModel();
                    statuses.addElement("PENDING");
                    statuses.addElement("CONFIRMED");
                    statuses.addElement("DONE");
                    statuses.addElement("REJECTED");
                    status.setModel(statuses);
                    status.setSelectedItem(selectedOrder.getStatus());
                    
                    status.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                String s = (String) status.getSelectedItem();
                                orderController.updateOrderStatus(
                                        session.getSessionId(),
                                        selectedOrder.getOrderId(),
                                        s);
                                selectedOrder.setStatus(s);
                                tableModel.fireTableRowsUpdated(index, index);
                            } catch (RemoteException ex) {
                                Utils.showErrorDialog(null, "Communication error!");
                            }
                        }
                    });
                }
            }
            
        });
    }
    
    private void setupPermissions() {
        @Data
        class ComponentPermissionTuple {
            final Component component;
            final Permission permission;
        }
        
        // rp = required permissions ; to ease typing and reading
        List<ComponentPermissionTuple> rp = new LinkedList<>();
        rp.add(new ComponentPermissionTuple(this.createOrderButton, new Permission("create", "order")));
        rp.add(new ComponentPermissionTuple(this.cancelOrderButton, new Permission("remove", "order")));
        rp.add(new ComponentPermissionTuple(this.orderFrom, new Permission("update", "order")));
        rp.add(new ComponentPermissionTuple(this.orderCharge, new Permission("update", "order")));
        rp.add(new ComponentPermissionTuple(this.orderTo, new Permission("update", "order")));
        rp.add(new ComponentPermissionTuple(this.orderWeight, new Permission("update", "order")));
        rp.add(new ComponentPermissionTuple(this.orderDelivered, new Permission("update", "order")));
        rp.add(new ComponentPermissionTuple(this.orderCreate, new Permission("update", "order")));
        rp.add(new ComponentPermissionTuple(this.orderDueDate, new Permission("update", "order")));
        rp.add(new ComponentPermissionTuple(this.status, new Permission("update", "order.status")));
        List<Permission> allPermissions = session.getAllPermissions();
        
        for (ComponentPermissionTuple pair : rp) {
            if (!allPermissions.contains(pair.permission)) {
                pair.component.setEnabled(false);
            }
            
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        createOrderButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        orderTable = new javax.swing.JTable();
        status = new javax.swing.JComboBox();
        orderID = new javax.swing.JLabel();
        from = new javax.swing.JLabel();
        to = new javax.swing.JLabel();
        weight = new javax.swing.JLabel();
        status2 = new javax.swing.JLabel();
        charge = new javax.swing.JLabel();
        dueDate = new javax.swing.JLabel();
        createBy = new javax.swing.JLabel();
        deliveredBy = new javax.swing.JLabel();
        orderFrom = new javax.swing.JTextField();
        orderTo = new javax.swing.JTextField();
        orderWeight = new javax.swing.JTextField();
        orderCharge = new javax.swing.JTextField();
        orderDueDate = new javax.swing.JTextField();
        orderCreate = new javax.swing.JTextField();
        orderDelivered = new javax.swing.JTextField();
        cancelOrderButton = new javax.swing.JButton();
        searchBox = new javax.swing.JTextField();

        createOrderButton.setText("Create Order");
        createOrderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createOrderButtonActionPerformed(evt);
            }
        });

        orderTable.setAutoCreateRowSorter(true);
        orderTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Order ID", "Create At", "Due In", "Status"
            }
        ));
        orderTable.setAlignmentX(1.0F);
        jScrollPane1.setViewportView(orderTable);

        status.setModel(new javax.swing.DefaultComboBoxModel(new String[] { null }));
        status.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statusActionPerformed(evt);
            }
        });

        orderID.setText("Order");

        from.setText("From:");

        to.setText("To:");

        weight.setText("Weight:");

        status2.setText("Status");

        charge.setText("Charge:");

        dueDate.setText("Due date:");

        createBy.setText("Create By:");

        deliveredBy.setText("Delivered by:");

        orderDueDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orderDueDateActionPerformed(evt);
            }
        });

        orderCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orderCreateActionPerformed(evt);
            }
        });

        orderDelivered.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orderDeliveredActionPerformed(evt);
            }
        });

        cancelOrderButton.setText("Cancel Order");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(createOrderButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(searchBox, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(status2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(to, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(weight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(charge, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dueDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(createBy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(deliveredBy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(orderID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(from))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(status, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(orderFrom)
                            .addComponent(orderTo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                            .addComponent(orderWeight, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                            .addComponent(orderCharge, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                            .addComponent(orderDueDate, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                            .addComponent(orderCreate, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                            .addComponent(orderDelivered, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cancelOrderButton)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(createOrderButton)
                    .addComponent(searchBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(orderID, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(status, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(status2))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(from, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(orderFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(to, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(orderTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(weight, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(orderWeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(charge, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(orderCharge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dueDate, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(orderDueDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(createBy, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(orderCreate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(deliveredBy, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(orderDelivered, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addComponent(cancelOrderButton))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void statusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_statusActionPerformed

    private void orderDeliveredActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orderDeliveredActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_orderDeliveredActionPerformed

    private void orderDueDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orderDueDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_orderDueDateActionPerformed

    private void orderCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orderCreateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_orderCreateActionPerformed

    private void createOrderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createOrderButtonActionPerformed
        Order createdOrder = new CreateOrderDialog(null)
                .showDialog(session, orderController);
        if (createdOrder != null) {
            this.tableModel.add(createdOrder);
        }
    }//GEN-LAST:event_createOrderButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelOrderButton;
    private javax.swing.JLabel charge;
    private javax.swing.JLabel createBy;
    private javax.swing.JButton createOrderButton;
    private javax.swing.JLabel deliveredBy;
    private javax.swing.JLabel dueDate;
    private javax.swing.JLabel from;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField orderCharge;
    private javax.swing.JTextField orderCreate;
    private javax.swing.JTextField orderDelivered;
    private javax.swing.JTextField orderDueDate;
    private javax.swing.JTextField orderFrom;
    private javax.swing.JLabel orderID;
    private javax.swing.JTable orderTable;
    private javax.swing.JTextField orderTo;
    private javax.swing.JTextField orderWeight;
    private javax.swing.JTextField searchBox;
    private javax.swing.JComboBox status;
    private javax.swing.JLabel status2;
    private javax.swing.JLabel to;
    private javax.swing.JLabel weight;
    // End of variables declaration//GEN-END:variables
}
