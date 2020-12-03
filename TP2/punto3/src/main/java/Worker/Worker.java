package Worker;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class Worker {
    private String id;
    private int peakLoad;
    private int currentLoad;
    private float percentageLoad;
    private WorkerState state;
    ArrayList<Service> services;


    public Worker(String id, int peakLoad){
        this.id= id;
        this.peakLoad=peakLoad;
        services = new ArrayList<>();
        this.currentLoad = 0;
        this.percentageLoad=0;
        this.state= WorkerState.IDLE;
    }


    public String getId() {
        return id;
    }

    public int getPeakLoad() {
        return peakLoad;
    }

    public int getCurrentLoad() {
        return currentLoad;
    }

    public float getPercentageLoad() {
        return percentageLoad;
    }

    public WorkerState getState() {
        return state;
    }

    public void addService(Service service){
        this.services.add(service);
    }

    public  ArrayList<Service> getServices(){
        return this.services;
    }

    private void  updatePercentageLoad(){
        this.percentageLoad = getCurrentLoad()/getPeakLoad();
    }

    private void  updateState(){
        if (getPercentageLoad() == 0) {
            this.setState(WorkerState.IDLE);
        } else if (getPercentageLoad() < 0.60) {
            this.setState(WorkerState.NORMAL);
        } else if (getPercentageLoad() < 0.80) {
            this.setState(WorkerState.ALERT);
        } else {
            this.setState(WorkerState.CRITICAL);
        }
    }

    public void setState(WorkerState state){
        this.state = state;
    }

    public Service findService( String name){
        Service result = null;
        int i = 0;
        boolean quit = false;
        while (!quit && (i<this.getServices().size())){
            if (this.services.get(i).getName().equals(name)){
                quit=true;
                result = this.services.get(i);
            }
            i++;
        }
        return result;
    }
}
