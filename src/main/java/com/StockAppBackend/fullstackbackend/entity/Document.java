package com.StockAppBackend.fullstackbackend.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="document")
public class Document {

    @Id
    @Column(name = "iddocument")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number")
    private String number;

    @Column(name = "date")
    private Date date;

    @Column(name = "delivery")
    private int  delivery;

    @Column(name = "status")
    private String status;

    @Column(name = "type")
    private String type;

    @Column(name = "coefficient")
    private double  coefficient;

    @Column(name = "summ")
    private double  summ;

    @Column(name = "amount")
    private int  amount;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_storehouse")
    private Storehouse storehouse;

    @ManyToOne
    @JoinColumn(name = "id_provider")
    private Supplier supplier;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "document")
    private Set<DocumentInfo> documentInfo = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "document")
    private Set<Income> income = new HashSet<>();


    public Document(){

    }

    public Document(Long id, String number, Date date,int delivery, String status, String type, double coefficient, User user, Supplier supplier, Storehouse storehouse) {
        this.id = id;
        this.number = number;
        this.date = date;
        this.status = status;
        this.type = type;
        this.coefficient = coefficient;
        this.user = user;
        this.supplier = supplier;
        this.storehouse = storehouse;
        this.delivery = delivery;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public double getSumm() {
        return summ;
    }

    public void setSumm(double summ) {
        this.summ = summ;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Storehouse getStorehouse() {
        return storehouse;
    }

    public void setStorehouse(Storehouse storehouse) {
        this.storehouse = storehouse;
    }

    public int getDelivery() {
        return delivery;
    }

    public void setDelivery(int delivery) {
        this.delivery = delivery;
    }
}
