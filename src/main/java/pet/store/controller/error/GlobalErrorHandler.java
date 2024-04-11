package pet.store.controller.error;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalErrorHandler {
	private enum LogStatus {
		STACK_TRACE, MESSAGE_ONLY
	}

	@Data
	private class ExceptionMessage {
		private String message;
		private String statusReason;
		private int statusCode;
		private String timestamp;
		private String uri;
	}

	// Catches Ex. Mess. NSEE and sets HTTP response code to 404 (Not Found) + some details
	@ExceptionHandler(NoSuchElementException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public ExceptionMessage handleNoSuchElementException(NoSuchElementException ex, WebRequest webRequest) {
		return buildExceptionMessage(ex, HttpStatus.NOT_FOUND, webRequest, LogStatus.MESSAGE_ONLY);
	}

	// Construct Ex. Mess. by extracting HTTP status code's reason phrase and value, timestamp, and URI
	private ExceptionMessage buildExceptionMessage(Exception ex, HttpStatus status, WebRequest webRequest,
			LogStatus logStatus) {
		String message = ex.toString();
		String statusReason = status.getReasonPhrase();
		int statusCode = status.value();
		String timestamp = ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME);
		String uri = null;

		if (webRequest instanceof ServletWebRequest swr) {
			uri = swr.getRequest().getRequestURI();
		}

		if (logStatus == LogStatus.MESSAGE_ONLY) {
			log.error("Exception: {}", message);
		} else {
			log.error("Exception: {}", ex);
		}

		
		// Make the exception message and return it to give feedback on provided parameters
		ExceptionMessage exMsg = new ExceptionMessage();

		exMsg.setMessage(message);
		exMsg.setStatusReason(statusReason);
		exMsg.setStatusCode(statusCode);
		exMsg.setTimestamp(timestamp);
		exMsg.setUri(uri);

		return exMsg;
	}
}