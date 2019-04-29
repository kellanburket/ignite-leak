package co.mira.ignite;

import org.apache.ignite.cache.query.annotations.QueryTextField;

import java.io.Serializable;

public class JavaValue implements Serializable {
	JavaValue(String theta) {
		this.theta = theta;
	}

	@QueryTextField
	private String theta;
}
