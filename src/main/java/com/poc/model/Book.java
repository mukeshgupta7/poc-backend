package com.poc.model;

import javax.persistence.*;

@Entity
@Table(name = "book")
public class Book {
	
	@ManyToOne
    @JoinColumn(name="uid", referencedColumnName="name", nullable=true, insertable=false, updatable=false)
    private User user;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "author")
	private String author;
	
	@Column(name = "price")
	private String price;
	
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	@Column(name = "uid")
	private String uid;
	
	@Column(name = "picByte", length = 100000)
	private byte[] picByte;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	
	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public byte[] getPicByte() {
		return picByte;
	}

	public void setPicByte(byte[] picByte) {
		this.picByte = picByte;
	}
}