/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smarttmtweb.libauth.utils;


import java.text.Normalizer;
import java.util.Map;

/**
 * @author Usuario
 */
public class LeeCadena {

    public static String leer(String cadena, String separador, String argumento) {
        int indiceInicial, indiceFinal;
        String tokenCadena;
        try {
            if (!((cadena == null ? "" : cadena).isEmpty() && (separador == null ? "" : separador).isEmpty() && (argumento == null ? "" : argumento).isEmpty())) {
                indiceInicial = cadena.indexOf(argumento);
                tokenCadena = cadena.substring(indiceInicial);
                indiceFinal = (tokenCadena.indexOf(separador) <= 0 ? tokenCadena.length() : tokenCadena.indexOf(separador));
                tokenCadena = tokenCadena.substring(0, indiceFinal);
                return tokenCadena.substring(argumento.length() + 1);
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    public static String funLeerItems(String cadena, String separador, String argumento) {
        int indiceInicial, indiceFinal;
        String tokenCadena;
        try {
            if (!((cadena == null ? "" : cadena).isEmpty() && (separador == null ? "" : separador).isEmpty() && (argumento == null ? "" : argumento).isEmpty())) {
                indiceInicial = cadena.indexOf(argumento);
                tokenCadena = cadena.substring(indiceInicial);
                indiceFinal = (tokenCadena.indexOf(separador) <= 0 ? tokenCadena.length() : tokenCadena.indexOf(separador));
                tokenCadena = tokenCadena.substring(0, indiceFinal);
                return tokenCadena.substring(argumento.length() + 1);
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    public static String funLeerItems(String cadena, String argumento) {
        int indiceInicial, indiceFinal;
        String tokenCadena;
        try {
            if (cadena != null && !cadena.isEmpty() && argumento != null && !argumento.isEmpty()) {
                indiceInicial = cadena.indexOf(argumento);
                tokenCadena = cadena.substring(indiceInicial);
                indiceFinal = (tokenCadena.indexOf("|") <= 0 ? tokenCadena.length() : tokenCadena.indexOf("|"));
                tokenCadena = tokenCadena.substring(0, indiceFinal);
                return tokenCadena.substring(argumento.length() + 1);
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    public static String armaCadenaItems(Map<String, String> mapRegistros) throws Exception {
        String cadena;
        if (!mapRegistros.isEmpty()) {
            cadena = mapRegistros.toString();
            cadena = cadena.replace("=", ":");
            cadena = cadena.replace(",", "|");
            cadena = cadena.replace("{", "");
            cadena = cadena.replace("}", "");
            return cadena;
        }
        return null;
    }

    public static String armaIdBeanMetodo(String idBeanMetodo){
        return "#{".concat(idBeanMetodo).concat("()}");
    }

    public static String eliminarPuntos(String cadena) {
        String archivo = cadena;

        String nombrearchivo = archivo.substring(0, archivo.lastIndexOf('.'));

        String tipoarchivo = archivo.substring(archivo.lastIndexOf('.'), archivo.length());

        String nombreArchivoModificado = nombrearchivo.replace(".", "");

        archivo = nombreArchivoModificado + tipoarchivo;

        return archivo;
    }

    public static String eliminarCaracteresEspeciales(String cadena) {
        String archivo = cadena;
        archivo = archivo.replace("@", "");
        archivo = archivo.replace("=", "");
        archivo = archivo.replace(",", "");
        archivo = archivo.replace("\"", "");
        archivo = archivo.replace("/", "");
        archivo = archivo.replace("&", "");
        archivo = archivo.replace("%", "");
        archivo = archivo.replace("$", "");
        archivo = archivo.replace("#", "");
        archivo = archivo.replace("'", "");
        archivo = archivo.replace("*", "");
        archivo = archivo.replace("^", "");
        archivo = archivo.replace("?", "");
        archivo = archivo.replace("¿", "");
        archivo = archivo.replace("!", "");
        archivo = archivo.replace("+", "");
        String noNormalizada = archivo;
        String cadenanormalizada = Normalizer.normalize(noNormalizada, Normalizer.Form.NFD);
        archivo = cadenanormalizada.replaceAll("[^\\p{ASCII}]", ""); //Le quita las tildes a todos los valores que lleguen.
        archivo = eliminarPuntos(archivo);
        return archivo;
    }

    /*public static void main(String args[]) {
        System.out.print("RETORNO -> " + LeeCadena.leer("NUMERSERVI:" + "A64" + ";" + "MENSAERRO:" + "ERROR XXXXX", ";", "MENSAERRO"));
    }*/
}
