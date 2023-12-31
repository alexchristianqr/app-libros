package Formularios;

import java.awt.Image;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author alex
 */
public class FormLibro extends javax.swing.JFrame {

    // Declaración de clases
    Libro libro;
    ArregloLibros arregloLibros;
    DefaultTableModel modelo;
    String[] cabecera = {"N°", "Codigo", "Nombre de libro", "Tipo", "Clase", "Año", "Num. Pag", "Costo"};
    String[][] data = {};

    // Variables globales
    int num = 0;

    /**
     * Creates new form FormLibro
     */
    public FormLibro() {
        initComponents();

        modelo = new DefaultTableModel(data, cabecera);
        tblLibros.setModel(modelo);
        arregloLibros = new ArregloLibros();
        cargar();
        actualizarTabla();
        resumen();
        txtCodigo.requestFocus();
    }

    //////////////////////////
    /* Mantenimiento MODELO */
    //////////////////////////
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
        } catch (IOException | ClassNotFoundException ex) {
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

        if (posicionLibro == -1) {
            mensaje("El Codigo no existe");
        } else {
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

    }

    public void consultar() {
        String codigo = txtCodigo.getText().toUpperCase();
        int posicionLibro = arregloLibros.buscarLibro(codigo);

        if (posicionLibro == -1) {
            mensaje("El Codigo no existe");
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
            lblIcono.setIcon(libro.getPortada());

        }
    }

    public void eliminar() {
        consultar();

        String codigo = txtCodigo.getText().toUpperCase();
        int posicionLibro = arregloLibros.buscarLibro(codigo);

        if (posicionLibro != -1) {
            int respuesta = JOptionPane.showConfirmDialog(this, "¿Esta seguro de eliminar este registro?", "Responder",
                    0);
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

    /////////////////////////
    /* Mantenimiento TABLA */
    /////////////////////////
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

    public void insertarTabla(int num, String codigo, String nombre, String tipo, String clase, int anio, int numPagina,
            double costo) {
        String formatoCosto;

        DecimalFormat df = new DecimalFormat("####.00");
        formatoCosto = df.format(costo);
        Object[] fila = {num, codigo, nombre, tipo, clase, valueToString(anio), valueToString(numPagina),
            formatoCosto};
        modelo.addRow(fila);
    }

    ///////////////////////
    /* Funciones ORDENAR */
    ///////////////////////
    public void ordenarPorCodigo() {
        int tamano = arregloLibros.totalLibros();
        int menor;
        Libro aux;

        for (int p = 0; p < (tamano - 1); p++) {
            menor = p;
            for (int q = (p + 1); q < tamano; q++) {
                String codigoActual = arregloLibros.obtenerLibro(q).getCodigo();
                String codigoMenor = arregloLibros.obtenerLibro(menor).getCodigo();

                if (codigoActual.compareToIgnoreCase(codigoMenor) < 0) {
                    menor = q;
                }
            }

            aux = arregloLibros.obtenerLibro(p);
            arregloLibros.reemplazarLibro(p, arregloLibros.obtenerLibro(menor));
            arregloLibros.reemplazarLibro(menor, aux);
        }

        actualizarTabla();
    }

    public void ordenarPorNombre() {
        int tamano = arregloLibros.totalLibros();
        Libro aux;

        for (int q = 0; q < (tamano - 1); q++) {
            for (int p = 0; p < (tamano - 1) - q; p++) {

                int posActual = p;
                int posSiguiente = posActual + 1;

                String xActual = arregloLibros.obtenerLibro(posActual).getNombre();
                String xSiguiente = arregloLibros.obtenerLibro(posSiguiente).getNombre();

                if (xActual.compareToIgnoreCase(xSiguiente) > 0) {
                    aux = arregloLibros.obtenerLibro(posActual);
                    arregloLibros.reemplazarLibro(posActual, arregloLibros.obtenerLibro(posSiguiente));
                    arregloLibros.reemplazarLibro(posSiguiente, aux);
                }
            }
        }

        actualizarTabla();
    }

    public void ordenarPorTipo() {
        int tamano = arregloLibros.totalLibros();
        int j;
        Libro aux;

        for (int i = 1; i < tamano; i++) {

            j = i;

            while (j != 0) {

                int posActual = j;
                int posAnterior = j - 1;

                String xActual = arregloLibros.obtenerLibro(posActual).getTipo();
                String xAnterior = arregloLibros.obtenerLibro(posAnterior).getTipo();

                if (xActual.compareToIgnoreCase(xAnterior) < 0) {

                    aux = arregloLibros.obtenerLibro(posActual);
                    arregloLibros.reemplazarLibro(posActual, arregloLibros.obtenerLibro(posAnterior));
                    arregloLibros.reemplazarLibro(posAnterior, aux);

                } else {
                    j = 1;
                }

                j = j - 1;
            }
        }

        actualizarTabla();
    }

    public void ordenarPorClase() {
        int tamano = arregloLibros.totalLibros();
        Libro aux;

        for (int q = 0; q < (tamano - 1); q++) {
            for (int p = 0; p < (tamano - 1) - q; p++) {

                int posActual = p;
                int posSiguiente = posActual + 1;

                String xActual = arregloLibros.obtenerLibro(posActual).getClase();
                String xSiguiente = arregloLibros.obtenerLibro(posSiguiente).getClase();

                if (xActual.compareToIgnoreCase(xSiguiente) > 0) {
                    aux = arregloLibros.obtenerLibro(posActual);
                    arregloLibros.reemplazarLibro(posActual, arregloLibros.obtenerLibro(posSiguiente));
                    arregloLibros.reemplazarLibro(posSiguiente, aux);
                }
            }
        }

        actualizarTabla();
    }

    public void ordenarPorAnio() {
        int tamano = arregloLibros.totalLibros();
        int j;
        Libro aux;

        for (int i = 1; i < tamano; i++) {

            j = i;

            while (j != 0) {

                int posActual = j;
                int posAnterior = posActual - 1;

                int xActual = arregloLibros.obtenerLibro(posActual).getAnio();
                int xAnterior = arregloLibros.obtenerLibro(posAnterior).getAnio();

                if (xActual < xAnterior) {

                    aux = arregloLibros.obtenerLibro(posActual);
                    arregloLibros.reemplazarLibro(posActual, arregloLibros.obtenerLibro(posAnterior));
                    arregloLibros.reemplazarLibro(posAnterior, aux);

                } else {
                    j = 1;
                }

                j = j - 1;
            }
        }

        actualizarTabla();
    }

    public void ordenarPorNumPag() {
        int tamano = arregloLibros.totalLibros();
        int menor;
        Libro aux;

        for (int p = 0; p < (tamano - 1); p++) {

            menor = p;

            for (int q = (p + 1); q < tamano; q++) {
                int xSiguiente = arregloLibros.obtenerLibro(q).getNumPagina();
                int xActual = arregloLibros.obtenerLibro(menor).getNumPagina();

                if (xSiguiente < xActual) {
                    menor = q;
                }
            }

            aux = arregloLibros.obtenerLibro(p);
            arregloLibros.reemplazarLibro(p, arregloLibros.obtenerLibro(menor));
            arregloLibros.reemplazarLibro(menor, aux);
        }

        actualizarTabla();
    }

    public void ordenarPorCosto() {
        int tamano = arregloLibros.totalLibros();
        int menor;
        Libro aux;

        for (int p = 0; p < (tamano - 1); p++) {

            menor = p;

            for (int q = (p + 1); q < tamano; q++) {
                double xSiguiente = arregloLibros.obtenerLibro(q).getCosto();
                double xActual = arregloLibros.obtenerLibro(menor).getCosto();

                if (xSiguiente < xActual) {
                    menor = q;
                }
            }

            aux = arregloLibros.obtenerLibro(p);
            arregloLibros.reemplazarLibro(p, arregloLibros.obtenerLibro(menor));
            arregloLibros.reemplazarLibro(menor, aux);
        }

        actualizarTabla();
    }

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
        cbxOrdenar = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txt01 = new javax.swing.JTextField();
        txt02 = new javax.swing.JTextField();
        txt03 = new javax.swing.JTextField();
        txt04 = new javax.swing.JTextField();
        btnSalir = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txt05 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Detalles del Libro"));

        jLabel1.setText("Codigo:");

        jLabel2.setText("Nombre:");

        jLabel3.setText("Año edición:");

        jLabel4.setText("Clase:");

        txtCodigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodigoKeyPressed(evt);
            }
        });

        jLabel5.setText("Tipo editorial:");

        cbxTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- SELECCIONAR --", "PROGRAMACION", "GESTION", "SISTEMAS" }));

        cbxClase.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- SELECCIONAR --", "A", "B", "C" }));

        jLabel6.setText("Costo:");

        jLabel7.setText("Nro pag.:");

        btnBuscarPortada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/picture.png"))); // NOI18N
        btnBuscarPortada.setText("Portada");
        btnBuscarPortada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarPortadaActionPerformed(evt);
            }
        });

        btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/save.png"))); // NOI18N
        btnGrabar.setText("Grabar");
        btnGrabar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGrabarActionPerformed(evt);
            }
        });

        btnConsultar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/search.png"))); // NOI18N
        btnConsultar.setText("Consultar");
        btnConsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConsultarActionPerformed(evt);
            }
        });

        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/edit.png"))); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/error.png"))); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        lblIcono.setBackground(new java.awt.Color(204, 204, 204));
        lblIcono.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblIcono.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/folder.png"))); // NOI18N

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

        cbxOrdenar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ninguno", "Codigo", "Nombre", "Tipo editorial", "Clase", "Año edición", "Numero de páginas", "Costo" }));
        cbxOrdenar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxOrdenarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 718, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbxOrdenar, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(cbxOrdenar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Información del Libro"));

        jLabel9.setText("Nombre del Libro con el Año  de edición mas reciente.");
        jLabel9.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/exit.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        jLabel10.setText("Nombre de la editorial que tiene el libro con el menor numero de paginas.");

        jLabel11.setText("Numero de libros que supera el costo de 100 PEN que sean de la clase A y sean de Gestion");

        jLabel12.setText("Nombre del libro que tenga el mayor costo y a que editorial pertenece.");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txt05)
                    .addComponent(txt03)
                    .addComponent(txt04, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSalir, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(txt02, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt01, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt02, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt03, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt04, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt05, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtCodigoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == evt.VK_ENTER) {
            txtCodigo.requestFocus();
        }
    }//GEN-LAST:event_txtCodigoKeyPressed

    private void btnBuscarPortadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarPortadaActionPerformed
        // TODO add your handling code here:

        JFileChooser jfc = new JFileChooser();
        int option = jfc.showOpenDialog(this);

        if (option == JFileChooser.APPROVE_OPTION) {
            String file = jfc.getSelectedFile().getPath();

            lblIcono.setIcon(new ImageIcon(file));

            ImageIcon icon = new ImageIcon(file);
            Image img = icon.getImage();
            Image newImage = img.getScaledInstance(140, 170, java.awt.Image.SCALE_SMOOTH);

            ImageIcon newIcon = new ImageIcon(newImage);
            lblIcono.setIcon(newIcon);
            lblIcono.setSize(140, 170);
        }
    }//GEN-LAST:event_btnBuscarPortadaActionPerformed

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed
        // TODO add your handling code here:

        if (txtCodigo.getText().isEmpty()) {
            mensaje("El campo [codigo] no puede estar vacio");
            return;
        }
        String codigo = txtCodigo.getText().toUpperCase();

        if (txtNombre.getText().isEmpty()) {
            mensaje("El campo [nombre] no puede estar vacio");
            return;
        }
        String nombre = txtNombre.getText().toUpperCase();

        String tipo = cbxTipo.getSelectedItem().toString();
        String clase = cbxClase.getSelectedItem().toString();

        if (txtAnioEdicion.getText().isEmpty()) {
            mensaje("El campo [anio] no puede estar vacio");
            return;
        }
        int anio = Integer.parseInt(txtAnioEdicion.getText());

        if (txtNumPaginas.getText().isEmpty()) {
            mensaje("El campo [num_pag] no puede estar vacio");
            return;
        }
        int numPagina = Integer.parseInt(txtNumPaginas.getText());

        if (txtCosto.getText().isEmpty()) {
            mensaje("El campo [costo] no puede estar vacio");
            return;
        }
        double costo = Double.parseDouble(txtCosto.getText());

        if (lblIcono.getIcon().toString().isEmpty()) {
            mensaje("El campo [icono] no puede estar vacio");
            return;
        }
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

    private void btnConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsultarActionPerformed
        // TODO add your handling code here:

        consultar();
    }//GEN-LAST:event_btnConsultarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        // TODO add your handling code here:

        modificar();
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:

        eliminar();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void cbxOrdenarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxOrdenarActionPerformed
        // TODO add your handling code here:

        System.out.println("El indice es: " + cbxOrdenar.getSelectedIndex());

        switch (cbxOrdenar.getSelectedIndex()) {
            case 0:
                cargar();
                actualizarTabla();
                break;
            case 1:
                ordenarPorCodigo();
                break;
            case 2:
                ordenarPorNombre();
                break;
            case 3:
                ordenarPorTipo();
                break;
            case 4:
                ordenarPorClase();
                break;
            case 5:
                ordenarPorAnio();
                break;
            case 6:
                ordenarPorNumPag();
                break;
            case 7:
                ordenarPorCosto();
                break;
        }
    }//GEN-LAST:event_cbxOrdenarActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        // TODO add your handling code here:

        dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

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
            java.util.logging.Logger.getLogger(FormLibro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormLibro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormLibro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormLibro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormLibro().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarPortada;
    private javax.swing.JButton btnConsultar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGrabar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> cbxClase;
    private javax.swing.JComboBox<String> cbxOrdenar;
    private javax.swing.JComboBox<String> cbxTipo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
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
