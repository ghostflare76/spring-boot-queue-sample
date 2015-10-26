package demo;

import java.io.IOException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.Marker;
import org.slf4j.helpers.BasicMarkerFactory;

public class LogbackAppenderTest {
private static final Logger LOG = LoggerFactory.getLogger(LogbackAppenderTest.class);
	
	@Test
	public void logSimple() throws InterruptedException {
		
		
			LOG.warn("Test the logger 1.");
			
	
		
		
		Thread.sleep(1000); // 非同期にlogをappendしてるのでちょっと待つ...
	}
	
	@Test
	public void logMdc() throws InterruptedException {
		MDC.put("req.requestURI", "/hello/world.js");
		LOG.info("Test the logger 1.");
		LOG.info("Test the logger 2.");
		
		Thread.sleep(1000); // 非同期にlogをappendしてるのでちょっと待つ...
	}
	
	@Test
	public void logMarker() throws InterruptedException {
		Marker sendEmailMarker = new BasicMarkerFactory().getMarker("SEND_EMAIL");
		LOG.info(sendEmailMarker, "Test the marker 1.");
		LOG.info(sendEmailMarker, "Test the marker 2.");
		
		Thread.sleep(1000); // 非同期にlogをappendしてるのでちょっと待つ...
	}
	
	@Test
	public void logNestedMarker() throws InterruptedException {
		Marker notifyMarker = new BasicMarkerFactory().getMarker("NOTIFY");
		Marker sendEmailMarker = new BasicMarkerFactory().getMarker("SEND_EMAIL");
		sendEmailMarker.add(notifyMarker);
		LOG.info(sendEmailMarker, "Test the nested marker 1.");
		LOG.info(sendEmailMarker, "Test the nested marker 2.");
		
		Thread.sleep(1000); // 非同期にlogをappendしてるのでちょっと待つ...
	}
	
	@Test
	public void logThrowable() throws InterruptedException {
		LOG.info("Without Exception.");
		LOG.error("Test the checked Exception.", new IOException("Connection something"));
		LOG.warn("Test the unchecked Exception.", new IllegalStateException("Oh your state"));
		
		Thread.sleep(1000); // 非同期にlogをappendしてるのでちょっと待つ...
	}
}
