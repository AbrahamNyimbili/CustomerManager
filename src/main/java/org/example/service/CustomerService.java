package org.example.service;

import org.example.model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CustomerService {

    private final ObservableList<Customer> customers =
            FXCollections.observableArrayList();

    public ObservableList<Customer> getCustomers() {
        return customers;
    }

    public boolean save(String name, String province) {
        if (name == null || name.trim().isEmpty()) return false;
        if (province == null || province.trim().isEmpty()) return false;
        customers.add(new Customer(name.trim(), province.trim()));
        return true;
    }

    public boolean delete(Customer customer) {
        if (customer == null) return false;
        return customers.remove(customer);
    }
}
