package org.hydroid.beowulf.tool;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mfdev.utility.SpringContextBuilder;

public class ListContextBuilder {
	private static final Logger log = Logger.getLogger(ListContextBuilder.class);
	private final SpringContextBuilder builder;
	
	@Inject
	private ListContextBuilder(SpringContextBuilder builder) {
		this.builder = builder;
		builder.add("test-singly-linked-list.xml");
		builder.add("test-repoman.xml");
		builder.add("test-space.xml");
	}
	
	public ClassPathXmlApplicationContext build() {
		return builder.build();
	}
}
