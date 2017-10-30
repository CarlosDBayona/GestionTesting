package Testing;

import DAO.AdministrativoDAO;
import Datos.Administrativo;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author CARLOS
 */
public class Testing {
    public static void main(String[] args) throws Exception {
        Administrativo admin=new Administrativo("1019111412", "Carlos", "Contraseña");
        AdministrativoDAO Admin=new AdministrativoDAO();
        Admin.create(admin);
    }
}
