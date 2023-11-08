/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Formularios;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author alex
 */
public class FormularioLibro extends javax.swing.JFrame {

    // Declaración de clases
    Libro libro;
    ArregloLibros arregloLibros;
    DefaultTableModel modelo;
    String[] cabecera = {"N°", "Codigo", "Nombre de libro", "Tipo", "Clase", "Año", "Num. Pag", "Costo"};
    String[][] data = {};

    // Variables globales
    int num = 0;

    /**
     * Creates new form FormularioLibro
     */
    public FormularioLibro() {
        initComponents();

        modelo = new DefaultTableModel(data, cabecera);
        tblLibros.setModel(modelo);
        arregloLibros = new ArregloLibros();
        cargar();
        actualizarTabla();
        resumen();
        txtCodigo.requestFocus();
    }

    /* Mantenimiento MODELO */
    public int seleccionarTipo(String tipo) {
        if (tipo.equalsIgnoreCase("PROGRAMACION")) {
            return 1;
        } else if (tipo.equalsIgnoreCase("GESTION")) {
            return 2;
        } else if (tipo.equalsIgnoreCase("SISTEMAS")) {
            return 3;
        } else {
            return 0;
        }
    }

    public int seleccionarClase(String clase) {
        if (clase.equalsIgnoreCase("A")) {
            return 1;
        } else if (clase.equalsIgnoreCase("B")) {
            return 2;
        } else if (clase.equalsIgnoreCase("C")) {
            return 3;
        } else {
            return 0;
        }
    }

    public String valueToString(Object any) {
        return String.valueOf(any);
    }

    public void mensaje(String texto) {
        JOptionPane.showMessageDialog(this, texto);
    }

    public void cargar() {
        try {
            FileInputStream fis = new FileInputStream("Libros.bin");
            ObjectInputStream ois = new ObjectInputStream(fis);

            if (ois != null) {
                arregloLibros = (ArregloLibros) ois.readObject();
                ois.close();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar el archivo binario..." + ex);
        }
    }

    public void grabar() {
        try {
            FileOutputStream fos = new FileOutputStream("Libros.bin");
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            if (oos != null) {
                oos.writeObject(arregloLibros);
                oos.close();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error al grabar archivo serializado..." + ex);
        }
    }

    public void resumen() {
        String sA = "", sB = "", sC = "", sD = "";
        int mayor = -99, menor = 999999, sE = 0;
        double mayorCosto = -99;
        int n = arregloLibros.totalLibros();

        for (int i = 0; i < n; i++) {
            String codigo = arregloLibros.obtenerLibro(i).getCodigo();
            String nombre = arregloLibros.obtenerLibro(i).getNombre();
            String tipo = arregloLibros.obtenerLibro(i).getTipo();
            String clase = arregloLibros.obtenerLibro(i).getClase();
            int anio = arregloLibros.obtenerLibro(i).getAnio();
            int numPagina = arregloLibros.obtenerLibro(i).getNumPagina();
            double costo = arregloLibros.obtenerLibro(i).getCosto();

            if (anio > mayor) {
                mayor = anio;
                sA = nombre;
            }

            if (numPagina < menor) {
                menor = numPagina;
                sB = tipo;
            }

            if (costo > 100 && tipo.equalsIgnoreCase("GESTION") && clase.equalsIgnoreCase("A")) {
                sE++;
            }

            if (costo > mayorCosto) {
                mayorCosto = costo;
                sC = nombre;
                sD = tipo;

            }
        }

        txt01.setText(sA);
        txt02.setText(sB);
        txt04.setText(sC);
        txt05.setText(sD);
        txt03.setText(valueToString(sE));
    }

    public void modificar() {
        String codigo = txtCodigo.getText().toUpperCase();
        int posicionLibro = arregloLibros.buscarLibro(codigo);

        String nombre = txtNombre.getText().toUpperCase();
        String tipo = cbxTipo.getSelectedItem().toString();
        String clase = cbxClase.getSelectedItem().toString();
        int anio = Integer.parseInt(txtAnioEdicion.getText());
        int numPagina = Integer.parseInt(txtNumPaginas.getText());
        double costo = Double.parseDouble(txtCosto.getText());
        Icon icono = lblIcono.getIcon();

        libro = new Libro(codigo, nombre, tipo, clase, anio, numPagina, costo, icono);

        if (posicionLibro == -1) {
            arregloLibros.agregarLibro(libro);
        } else {
            arregloLibros.reemplazarLibro(posicionLibro, libro);
            limpiar();
            grabar();
            actualizarTabla();
            resumen();
            txtCodigo.requestFocus();
        }
    }

    public void consultar() {
        String codigo = txtCodigo.getText().toUpperCase();
        int posicionLibro = arregloLibros.buscarLibro(codigo);

        if (posicionLibro == -1) {
            mensaje("Codigo no existe");
            limpiar();
        } else {
            libro = arregloLibros.obtenerLibro(posicionLibro);

            String nombre = libro.getNombre();
            String tipo = libro.getTipo();
            String clase = libro.getClase();
            int anio = libro.getAnio();
            int numPagina = libro.getNumPagina();
            double costo = libro.getCosto();

            txtNombre.setText(nombre);
            cbxTipo.setSelectedIndex(seleccionarTipo(tipo));
            cbxClase.setSelectedIndex(seleccionarClase(clase));
            txtAnioEdicion.setText(valueToString(anio));
            txtNumPaginas.setText(valueToString(numPagina));
            txtCosto.setText(valueToString(costo));
            // lblIcono.setText(libro.obtenerPortada());

        }
    }

    public void eliminar() {
        consultar();

        String codigo = txtCodigo.getText().toUpperCase();
        int posicionLibro = arregloLibros.buscarLibro(codigo);

        if (posicionLibro != -1) {
            int respuesta = JOptionPane.showConfirmDialog(this, "¿Esta seguro de eliminar este registro?", "Responder", 0);
            if (respuesta == 0) {
                // arregloLibros.eliminarLibros();
                arregloLibros.eliminarLibro(codigo);
                limpiar();
                grabar();
                actualizarTabla();
                resumen();
                txtCodigo.requestFocus();
            }
        }
    }

    public void insertar() {

    }

    public void limpiar() {
        txtCodigo.setText("");
        txtNombre.setText("");
        cbxTipo.setSelectedIndex(0);
        cbxClase.setSelectedIndex(0);
        txtAnioEdicion.setText("");
        txtNumPaginas.setText("");
        txtCosto.setText("");
        lblIcono.setIcon(null);

        txtCodigo.requestFocus(true);
    }

    /* -- */

 /* Mantenimiento TABLA */
    public void actualizarTabla() {

        vaciarTabla();

        int n = arregloLibros.totalLibros();

        for (int i = 0; i < n; i++) {
            String codigo = arregloLibros.obtenerLibro(i).getCodigo();
            String nombre = arregloLibros.obtenerLibro(i).getNombre();
            String tipo = arregloLibros.obtenerLibro(i).getTipo();
            String clase = arregloLibros.obtenerLibro(i).getClase();
            int anio = arregloLibros.obtenerLibro(i).getAnio();
            int numPagina = arregloLibros.obtenerLibro(i).getNumPagina();
            double costo = arregloLibros.obtenerLibro(i).getCosto();

            insertarTabla(i + 1, codigo, nombre, tipo, clase, anio, numPagina, costo);
        }
    }

    public void vaciarTabla() {
        int filas = tblLibros.getRowCount();

        for (int i = 0; i < filas; i++) {
            modelo.removeRow(0);
        }
    }

    public void insertarTabla(int num, String codigo, String nombre, String tipo, String clase, int anio, int numPagina, double costo) {
        String formatoCosto;

        DecimalFormat df = new DecimalFormat("####.00");
        formatoCosto = df.format(costo);
        Object[] fila = {num, codigo, nombre, tipo, clase, valueToString(anio), valueToString(numPagina), formatoCosto};
        modelo.addRow(fila);
    }

    /* -- */
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cbxTipo = new javax.swing.JComboBox<>();
        txtNombre = new javax.swing.JTextField();
        txtAnioEdicion = new javax.swing.JTextField();
        cbxClase = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtCosto = new javax.swing.JTextField();
        txtNumPaginas = new javax.swing.JTextField();
        btnBuscarPortada = new javax.swing.JButton();
        btnGrabar = new javax.swing.JButton();
        btnConsultar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        lblIcono = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblLibros = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txt01 = new javax.swing.JTextField();
        txt02 = new javax.swing.JTextField();
        txt03 = new javax.swing.JTextField();
        txt04 = new javax.swing.JTextField();
        txt05 = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Libro");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Detalles del Libro"));

        jLabel1.setText("Codigo:");

        jLabel2.setText("Nombre:");

        jLabel3.setText("Año edición:");

        jLabel4.setText("Clase:");

        jLabel5.setText("Tipo editorial:");

        cbxTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- SELECCIONAR --", "PROGRAMACION", "GESTION", "SISTEMAS" }));

        cbxClase.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- SELECCIONAR --", "A", "B", "C" }));

        jLabel6.setText("Costo:");

        jLabel7.setText("Nro pag.:");

        btnBuscarPortada.setText("Portada");

        btnGrabar.setText("Grabar");
        btnGrabar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGrabarActionPerformed(evt);
            }
        });

        btnConsultar.setText("Consultar");
        btnConsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConsultarActionPerformed(evt);
            }
        });

        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        lblIcono.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbxTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtNombre)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cbxClase, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtAnioEdicion, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtCosto, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)
                            .addComponent(txtNumPaginas))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscarPortada, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnGrabar, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnConsultar, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblIcono, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(cbxTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(txtAnioEdicion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6)
                                    .addComponent(txtCosto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cbxClase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel7)
                                    .addComponent(txtNumPaginas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(btnBuscarPortada, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnGrabar)
                            .addComponent(btnConsultar)
                            .addComponent(btnModificar)
                            .addComponent(btnEliminar))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(lblIcono, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Relación de Libros"));

        tblLibros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblLibros);

        jLabel8.setText("Ordenar por:");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Información del Libro"));

        jLabel9.setText("Nombre del Libro con el Año de edición mas reciente.");

        txt01.setText("jTextField6");

        txt02.setText("jTextField1");

        txt03.setText("jTextField2");

        txt04.setText("jTextField3");

        txt05.setText("jTextField4");

        jButton6.setText("jButton6");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 234, Short.MAX_VALUE)
                    .addComponent(txt01, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txt02)
                    .addComponent(txt03)
                    .addComponent(txt04)
                    .addComponent(txt05)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jButton6)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txt02, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txt03, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txt04, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txt05, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed
        // TODO add your handling code here:

        String codigo = txtCodigo.getText();
        String nombre = txtNombre.getText();
        String tipo = cbxTipo.getSelectedItem().toString();
        String clase = cbxClase.getSelectedItem().toString();
        int anio = Integer.parseInt(txtAnioEdicion.getText());
        int numPagina = Integer.parseInt(txtNumPaginas.getText());
        double costo = Double.parseDouble(txtCosto.getText());
        Icon icono = lblIcono.getIcon();

        libro = new Libro(codigo, nombre, tipo, clase, anio, numPagina, costo, icono);

        if (arregloLibros.buscarLibro(libro.getCodigo()) != -1) {
            mensaje("Codigo repetido");
        } else {
            arregloLibros.agregarLibro(libro);
            insertarTabla(num, codigo, nombre, tipo, clase, anio, numPagina, costo);
            limpiar();
            grabar();
            actualizarTabla();
            resumen();
        }
    }//GEN-LAST:event_btnGrabarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        // TODO add your handling code here:
        modificar();
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsultarActionPerformed
        // TODO add your handling code here:
        consultar();
    }//GEN-LAST:event_btnConsultarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
        eliminar();
    }//GEN-LAST:event_btnEliminarActionPerformed

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
            java.util.logging.Logger.getLogger(FormularioLibro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormularioLibro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormularioLibro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormularioLibro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormularioLibro().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarPortada;
    private javax.swing.JButton btnConsultar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGrabar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JComboBox<String> cbxClase;
    private javax.swing.JComboBox<String> cbxTipo;
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblIcono;
    private javax.swing.JTable tblLibros;
    private javax.swing.JTextField txt01;
    private javax.swing.JTextField txt02;
    private javax.swing.JTextField txt03;
    private javax.swing.JTextField txt04;
    private javax.swing.JTextField txt05;
    private javax.swing.JTextField txtAnioEdicion;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtCosto;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtNumPaginas;
    // End of variables declaration//GEN-END:variables
}
