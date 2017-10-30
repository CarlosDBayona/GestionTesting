/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;
import DAO.AdministrativoDAO;
import Datos.Administrativo;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author james
 */

public class adminControlador extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // reading the user input
        String id = request.getParameter("idAdmin");
        String nombre = request.getParameter("nombreAdmin");
        String contrasena= request.getParameter("contrasenaAdmin");
        String tipopermiso= request.getParameter("tipoPermiso");
      
        
        
        
        //Se debe incluir validaciones - Lo recuerda: Gestion de Excepciones.
        AdministrativoDAO dao = new AdministrativoDAO();
        Administrativo admin = new Administrativo();
        
        admin.setIdadministrativo(id);
        admin.setNombreadmin(nombre);
        admin.setContrasena(contrasena);
        admin.setTipopermiso(tipopermiso);

        try {
            dao.create(admin);
        } catch (Exception ex) {
            Logger.getLogger(adminControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Listando la informacion  
        List<Administrativo> admins =  dao.findAdministrativoEntities();
        request.setAttribute("admins", admins);
       
       
        //Redireccionando la informacion
        RequestDispatcher redireccion = request.getRequestDispatcher("persona.jsp");
        redireccion.forward(request, response);
        
        
        }
}
