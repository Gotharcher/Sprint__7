package model;
//Оставлю класс, чтобы заложиться на расширение Джейсона. Похоже, он сам с расширением.
public class OrderResponseCarrier {
    private Order order;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
