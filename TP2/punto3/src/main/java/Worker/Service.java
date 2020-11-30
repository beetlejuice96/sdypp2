package Worker;

public interface Service {
    String getName();
    Object execute(Object[] list);//all it does is process the message
}
