package Otros;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Msg implements Serializable {

    private static final long serialVersionUID = 1L;
    private String functionName;
    public Map<String, String> parametros;
    private String resultado;

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }



    public Msg( String functionName){
        super();
        this.functionName=functionName;
        this.parametros= new HashMap<String, String>();
    }

    public void putParam(String key, String value){
        this.parametros.put(key,value);
    }

    public void deleteParam(String key){
        this.parametros.remove(key);
    }

    public String getFunctionName(){
        return this.functionName;
    }


}
