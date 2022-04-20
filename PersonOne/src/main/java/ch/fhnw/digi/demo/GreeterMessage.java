package ch.fhnw.digi.demo;

public class GreeterMessage {

	private String name;
	private String value;
	private String status;

	public GreeterMessage() {

	}
	public GreeterMessage(String name, String value, String status) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
