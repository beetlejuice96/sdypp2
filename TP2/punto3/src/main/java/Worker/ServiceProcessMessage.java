package Worker;

public class ServiceProcessMessage implements Service {
    private String name;

    public ServiceProcessMessage(String name){
        this.name= name;
    }

    public String getName() {
        return null;
    }

    public Object execute(Object[] list) {
        String resultado = "";
        for (int i=0;i<list.length;i++){
            resultado += list[i].toString();
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Se proceso el mensaje"+ resultado;
    }
}
