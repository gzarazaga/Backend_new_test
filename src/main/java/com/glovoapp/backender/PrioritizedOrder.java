package com.glovoapp.backender;

public class PrioritizedOrder implements Comparable {
    private Order order;
    private Integer priority;

    public PrioritizedOrder(Order order, Integer priority) {
        this.setOrder(order);
        this.setPriority(priority);
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof PrioritizedOrder) {
            return Integer.compare(this.priority, ((PrioritizedOrder) o).priority);
        }
        return -1;
    }
}
