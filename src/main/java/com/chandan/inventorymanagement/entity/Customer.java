package com.chandan.inventorymanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

public class Customer {

    private long id;
    private String name;
    private String address;
    private String gmapslink;
}
