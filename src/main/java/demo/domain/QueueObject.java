package demo.domain;

import java.io.Serializable;

public class QueueObject implements Serializable {
	
	private int id;
	private String name;
	
	
	public QueueObject(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "QueueObject [id=" + id + ", name=" + name + "]";
	}

    
	
		
	
}
