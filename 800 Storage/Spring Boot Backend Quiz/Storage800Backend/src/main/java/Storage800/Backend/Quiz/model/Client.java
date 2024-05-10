package Storage800.Backend.Quiz.model;

public class Client {
	
	private int id;
	private String firstname;
	private String lastname;
	private String mobile;
	
	
	public Client() {
		super();
	}


	public Client(int id, String firstname, String lastname, String mobile) {
		super();
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.mobile = mobile;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getFirstname() {
		return firstname;
	}


	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}


	public String getLastname() {
		return lastname;
	}


	public void setLastname(String lastname) {
		this.lastname = lastname;
	}


	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	
	
}
