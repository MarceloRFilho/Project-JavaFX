package model.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Seller implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String name;
	private String email;
	private Date bDate;
	private Double salary;
	
	private Department department;
	
	public Seller() {
	}

	public Seller(Integer id, String name, String email, Date bDate, Double salary, Department department) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.bDate = bDate;
		this.salary = salary;
		this.department = department;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getbDate() {
		return bDate;
	}

	public void setbDate(Date bDate) {
		this.bDate = bDate;
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Seller other = (Seller) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Seller [id=" + id + ", name=" + name + ", email=" + email + ", bDate=" + bDate + ", salary=" + salary
				+ ", department=" + department + "]";
	}
	
	
}
