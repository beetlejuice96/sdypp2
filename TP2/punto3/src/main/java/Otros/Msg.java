package Otros;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Msg implements Serializable {

    private static final long serialVersionUID = 1L;
    private String functionName;
    public Map<String, String> parametros;

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

    private String getFunctionName(){
        return this.functionName;
    }


}
