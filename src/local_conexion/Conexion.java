/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package local_conexion;

import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author 
 */
public class Conexion {
    
    static Connection cn;
    static Statement st;
    static ResultSet rs;
    
    //CONEXION
    public void conectar(){
    
        try{
            String url = "jdbc:oracle:thin:@localhost:1521:XE";
           //CONECTAR
            cn = DriverManager.getConnection(url,"bancoDaniel","admin");
            st = cn.createStatement();
            
        
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, "Error 1 " + ex, "Error2", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //CREAR CUENTA
    public void CrearCuenta(String rut, String nom, String ape, String dir, String tel, String ema, int monto){
        CallableStatement procedimiento = null;
        String crear = "{call CREAR_CUENTA(?,?,?,?,?,?,?)}";
        
        try{
            
        conectar();
        procedimiento = cn.prepareCall(crear);
        procedimiento.setString(1, rut);
        procedimiento.setString(2, nom);
        procedimiento.setString(3, ape);
        procedimiento.setString(4, dir);
        procedimiento.setString(5, tel);
        procedimiento.setString(6, ema);
        procedimiento.setInt(7, monto);
        procedimiento.executeUpdate();
        JOptionPane.showMessageDialog(null, 
                        "Gracias " + nom + " " + ape + ", tu cuenta se ha creado con exito.","Exito",JOptionPane.INFORMATION_MESSAGE);
        cn.close();
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    
    //EDITAR DATOS CLIENTES
    public void Modificar(String rut, String nom, String ape, String dir, String tel, String ema){
        CallableStatement procedimiento = null;
        String editar = "{call MODIFICAR_DATOS_CLIENTE(?,?,?,?,?,?)}";
        
        try{
            
        conectar();
        procedimiento = cn.prepareCall(editar);
        procedimiento.setString(1, rut);
        procedimiento.setString(2, nom);
        procedimiento.setString(3, ape);
        procedimiento.setString(4, dir);
        procedimiento.setString(5, tel);
        procedimiento.setString(6, ema);
        procedimiento.executeUpdate();
        JOptionPane.showMessageDialog(null, 
                        "El registro se modifico con exito","Editar",JOptionPane.INFORMATION_MESSAGE);
        cn.close();
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    
    }
    
    
    //ELIMINAR CUENTA CLIENTE
    public void Eliminar(String numero){
    
        CallableStatement procedimiento = null;
        String eliminar = "{call BD_MANTENCION.ELIMINAR_CUENTA(?)}";
        
        try{
            
        conectar();
        procedimiento = cn.prepareCall(eliminar);
        procedimiento.setString(1, numero);
        
        procedimiento.executeUpdate();
        JOptionPane.showMessageDialog(null, 
                        "El registro se elimino con exito","Editar",JOptionPane.INFORMATION_MESSAGE);
        cn.close();
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        
    }

    
    //REALIZAR DEPOSITO
     public void Deposito(String numero, int monto){
    
        CallableStatement procedimiento = null;
        String deposito = "{call BD_MANTENCION.REALIZAR_DEPOSITO(?,?)}";
        
        try{
            
        conectar();
        procedimiento = cn.prepareCall(deposito);
        procedimiento.setString(1, numero);
        procedimiento.setInt(2, monto);
        
        procedimiento.executeUpdate();
        JOptionPane.showMessageDialog(null, 
                        "El deposito se realizo con exito","Editar",JOptionPane.INFORMATION_MESSAGE);
        cn.close();
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
     
     //REALIZAR GIRO
     public void Giro(String numero, int monto){
    
        CallableStatement procedimiento = null;
        String giro = "{call REALIZAR_GIRO(?,?)}";
        
        try{
            
        conectar();
        procedimiento = cn.prepareCall(giro);
        procedimiento.setString(1, numero);
        procedimiento.setInt(2, monto);
        
        procedimiento.executeUpdate();
        JOptionPane.showMessageDialog(null, 
                        "El giro se realizo con exito","Editar",JOptionPane.INFORMATION_MESSAGE);
        cn.close();
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
     
     //REALIZAR TRASPASO
     public void Traspaso(String numeroO, String numeroD, int monto){
    
        CallableStatement procedimiento = null;
        
        String giro = "{call REALIZAR_GIRO(?,?)}";
        String deposito = "{call BD_MANTENCION.REALIZAR_DEPOSITO(?,?)}";
        try{
            
        conectar();
        procedimiento = cn.prepareCall(giro);
        procedimiento.setString(1, numeroO);
        procedimiento.setInt(2, monto);
        procedimiento.executeUpdate();
        
        procedimiento = cn.prepareCall(deposito);
        procedimiento.setString(1, numeroD);
        procedimiento.setInt(2, monto);
        procedimiento.executeUpdate();
        JOptionPane.showMessageDialog(null, 
                        "El traspaso se realizo con exito","Editar",JOptionPane.INFORMATION_MESSAGE);
        cn.close();
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
          
     
    //CARGAR DATOS DE LA TABLA CLIENTE EN FORMULARIO DE CLIENTE
    public void CargarDatos(JTable tabla){
          
    try
    {
        conectar();
        rs=st.executeQuery("SELECT * FROM cliente");
        DefaultTableModel dtm=new DefaultTableModel();
        dtm.addColumn("Rut cliente");
        dtm.addColumn("Nombre");
        dtm.addColumn("Apellido");
        dtm.addColumn("Direccion");
        dtm.addColumn("Telefono");
        dtm.addColumn("email");

        tabla.setModel(dtm);
     while(rs.next())
     {
         Object vector[]=new Object[6];
         for(int i=0;i<6;i++)
             vector[i]=rs.getObject(i+1);
          dtm.addRow(vector);
     }
    }

    catch(SQLException ex)
    {
     ex.getMessage();
    }
   }
     
    //BUSCAR POR NOMBRE
    public void BuscarDatos(JTable tabla, String nombre){
          
    try
    {
        conectar();
        rs=st.executeQuery("SELECT * FROM cliente where nombre ='"+nombre+"'");
        DefaultTableModel dtm=new DefaultTableModel();
        dtm.addColumn("Rut cliente");
        dtm.addColumn("Nombre");
        dtm.addColumn("Apellido");
        dtm.addColumn("Direccion");
        dtm.addColumn("Telefono");
        dtm.addColumn("email");

        tabla.setModel(dtm);
     while(rs.next())
     {
         Object vector[]=new Object[6];
         for(int i=0;i<6;i++)
             vector[i]=rs.getObject(i+1);
          dtm.addRow(vector);
     }
    }

    catch(SQLException ex)
    {
     ex.getMessage();
    }
   }
    
    //CARTOLA
    public void Cartola(JTable tabla, String numero){
          
    try
    {
        conectar();
        rs=st.executeQuery("SELECT * FROM saldo_ant where num_cuenta ='"+numero+"' order by fecha_transaccion desc");
        DefaultTableModel dtm=new DefaultTableModel();
        dtm.addColumn("Codigo");
        dtm.addColumn("Numero cuenta");
        dtm.addColumn("Transaccion");
        dtm.addColumn("Monto");
        dtm.addColumn("Saldo inicial");
        dtm.addColumn("Saldo final");
        dtm.addColumn("Fecha");

        tabla.setModel(dtm);
     while(rs.next())
     {
         Object vector[]=new Object[7];
         for(int i=0;i<7;i++)
             vector[i]=rs.getObject(i+1);
          dtm.addRow(vector);
     }
    }

    catch(SQLException ex)
    {
     ex.getMessage();
    }
   }
    
       //CARGAR DATOS DE LA TABLA CLIENTE EN FORMULARIO DE CLIENTE
    public void CargarCliente(JTable tabla){
          
    try
    {
        conectar();
        rs=st.executeQuery("SELECT * FROM cuenta_clt");
        DefaultTableModel dtm=new DefaultTableModel();
        dtm.addColumn("Numero cuenta");
        dtm.addColumn("Rut cliente");
        dtm.addColumn("Monto");


        tabla.setModel(dtm);
     while(rs.next())
     {
         Object vector[]=new Object[3];
         for(int i=0;i<3;i++)
             vector[i]=rs.getObject(i+1);
          dtm.addRow(vector);
     }
    }

    catch(SQLException ex)
    {
     ex.getMessage();
    }
   }
}
