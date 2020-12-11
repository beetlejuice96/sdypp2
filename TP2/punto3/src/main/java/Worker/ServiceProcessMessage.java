package Worker;

public class ServiceProcessMessage implements Service {
    private String name;

    public ServiceProcessMessage(String name){
        this.name= name;
    }

    public String getName() {
        return this.name;
    }


    //procesa la lista de parametros que se manda en el mensaje desde el cliente
    public Object execute(Object[] list) {
        String resultado = "";
        for (int i=0;i<list.length;i++){
            resultado += list[i].toString();
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Se proceso el mensaje"+ resultado;
    }
}
