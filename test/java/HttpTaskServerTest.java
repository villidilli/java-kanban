import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.api.*;
import ru.yandex.practicum.managers.Managers;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.api.APIMessage.*;
import static ru.yandex.practicum.api.Endpoint.*;

public class HttpTaskServerTest {
	@BeforeAll
	public static void beforeAll() {
		Servers.getKVServer().start();
	}

	@Test
	public void shouldReturnTrueWhenServerCreated() {
		Servers.getHttpTaskServer();
		boolean isCreate = false;
		try (Socket socket = new Socket()) {
			socket.connect(new InetSocketAddress("localhost", 8080), 5);
			isCreate = true;
		} catch (IOException e) {
		}
		assertTrue(isCreate);
	}

	@Test
	public void shouldReturnStatusForServerState() {
		HttpTaskServer server = Servers.getHttpTaskServer();
		assertEquals(HTTP_TASK_SERVER_CREATED.getMessage(), server.getStatusServer().getMessage());
		server.start();
		assertEquals(HTTP_TASK_SERVER_STARTED, server.getStatusServer());
		server.stop();
		assertEquals(HTTP_TASK_SERVER_STOPPED, server.getStatusServer());
	}

	@Test
	public void shouldReturnEndpointExcludeCreateUpdateWhenSendTrueRequest() {
		TasksHandler handler = new TasksHandler(Managers.getDefault());
		assertEquals(GET_ALL_TASKS, handler.getEndpoint(URI.create("/tasks/task"), RequestMethod.GET));
		assertEquals(GET_ALL_SUBTASKS, handler.getEndpoint(URI.create("/tasks/subtask"), RequestMethod.GET));
		assertEquals(GET_ALL_EPICS, handler.getEndpoint(URI.create("/tasks/epic"), RequestMethod.GET));
		assertEquals(GET_TASK_BY_ID, handler.getEndpoint(URI.create("/tasks/task?id="), RequestMethod.GET));
		assertEquals(GET_SUBTASK_BY_ID, handler.getEndpoint(URI.create("/tasks/subtask?id="), RequestMethod.GET));
		assertEquals(GET_EPIC_BY_ID, handler.getEndpoint(URI.create("/tasks/epic?id="), RequestMethod.GET));
		assertEquals(GET_ALL_SUBTASKS_BY_EPIC, handler.getEndpoint(URI.create("/tasks/subtask/epic?id="),
																							RequestMethod.GET));
		assertEquals(GET_PRIORITIZED_TASKS, handler.getEndpoint(URI.create("/tasks"), RequestMethod.GET));
		assertEquals(GET_HISTORY, handler.getEndpoint(URI.create("/tasks/history"), RequestMethod.GET));
		assertEquals(DELETE_ALL_TASKS, handler.getEndpoint(URI.create("/tasks/task"), RequestMethod.DELETE));
		assertEquals(DELETE_ALL_SUBTASKS, handler.getEndpoint(URI.create("/tasks/subtask"), RequestMethod.DELETE));
		assertEquals(DELETE_ALL_EPICS, handler.getEndpoint(URI.create("/tasks/epic"), RequestMethod.DELETE));
		assertEquals(DELETE_TASK_BY_ID, handler.getEndpoint(URI.create("/tasks/task?id="), RequestMethod.DELETE));
		assertEquals(DELETE_SUBTASK_BY_ID, handler.getEndpoint(URI.create("/tasks/subtask?id="), RequestMethod.DELETE));
		assertEquals(DELETE_EPIC_BY_ID, handler.getEndpoint(URI.create("/tasks/epic?id="), RequestMethod.DELETE));
		assertEquals(UNKNOWN, handler.getEndpoint(URI.create("/tasks/yandex"), RequestMethod.DELETE));
		assertEquals(UNKNOWN, handler.getEndpoint(URI.create("/tasks/yandex"), RequestMethod.POST));
		assertEquals(UNKNOWN, handler.getEndpoint(URI.create("/tasks/yandex"), RequestMethod.GET));
	}

	@Test
	public void shouldReturnEndpointCreateOrUpdateTaskWhenSendTrueRequest() {
		final String bodyWithID = "{\n" +
				"\t\"id\" : 1,\n" +
				"\t\"name\" : \"Task\"\n" +
				"}";
		final String bodyWithoutID = "{\n" +
				"\t\"name\" : \"Task\"\n" +
				"}";
		TasksHandler handler = new TasksHandler(Managers.getDefault());
		handler.body = JsonParser.parseString(bodyWithID);
		assertEquals(UPDATE_TASK, handler.getEndpoint(URI.create("/tasks/task"), RequestMethod.POST));
		handler.body = JsonParser.parseString(bodyWithoutID);
		assertEquals(CREATE_TASK, handler.getEndpoint(URI.create("/tasks/task"), RequestMethod.POST));
	}

	@Test
	public void shouldReturnEndpointCreateOrUpdateSubtaskWhenSendTrueRequest() {
		final String bodyWithID = "{\n" +
				"\t\"id\" : 1,\n" +
				"\t\"name\" : \"Subtask\"\n" +
				"}";
		final String bodyWithoutID = "{\n" +
				"\t\"name\" : \"Subtask\"\n" +
				"}";
		TasksHandler handler = new TasksHandler(Managers.getDefault());
		handler.body = JsonParser.parseString(bodyWithID);
		assertEquals(UPDATE_SUBTASK, handler.getEndpoint(URI.create("/tasks/subtask"), RequestMethod.POST));
		handler.body = JsonParser.parseString(bodyWithoutID);
		assertEquals(CREATE_SUBTASK, handler.getEndpoint(URI.create("/tasks/subtask"), RequestMethod.POST));
	}

	@Test
	public void shouldReturnEndpointCreateOrUpdateEpicWhenSendTrueRequest() {
		final String bodyWithID = "{\n" +
				"\t\"id\" : 1,\n" +
				"\t\"name\" : \"Epic\"\n" +
				"}";
		final String bodyWithoutID = "{\n" +
				"\t\"name\" : \"Epic\"\n" +
				"}";
		TasksHandler handler = new TasksHandler(Managers.getDefault());
		handler.body = JsonParser.parseString(bodyWithID);
		assertEquals(UPDATE_EPIC, handler.getEndpoint(URI.create("/tasks/epic"), RequestMethod.POST));
		handler.body = JsonParser.parseString(bodyWithoutID);
		assertEquals(CREATE_EPIC, handler.getEndpoint(URI.create("/tasks/epic"), RequestMethod.POST));
	}

	@Test
	public void shouldReturnEndpointCreateOrUpdateEpicWhenSendIncorrectRequest() {
		final String bodyNull = "";

		TasksHandler handler = new TasksHandler(Managers.getDefault());
		handler.body = JsonParser.parseString(bodyNull);
		assertEquals(UNKNOWN, handler.getEndpoint(URI.create("/tasks/epic"), RequestMethod.GET));

	}

}

