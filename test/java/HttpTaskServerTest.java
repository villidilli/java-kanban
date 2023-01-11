import com.google.gson.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.api.*;
import ru.yandex.practicum.managers.Managers;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;
import ru.yandex.practicum.utils.GsonConfig;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.api.APIMessage.*;
import static ru.yandex.practicum.api.Endpoint.*;

public class HttpTaskServerTest {
	private final URI uriHttpServer = URI.create("http://localhost:8080");
	static KVServer kvServer;
	static HttpTaskServer httpServer;
	TasksHandler handler;
	HttpRequest request;
	private final HttpClient client = HttpClient.newHttpClient();
	private HttpResponse<String> response;
	private final Gson gson = GsonConfig.getGsonTaskConfig();
	private Task task1;
	private Task task2;
	private Epic epic1;
	private Epic epic2;
	private SubTask subTask1;
	private SubTask subTask2;
	private SubTask subTask3;
	private SubTask subTask4;

	private HttpResponse<String> sendRequestGET() {
		HttpResponse<String> response = null;
		try {
			request = HttpRequest.newBuilder()
					.GET()
					.uri(uriHttpServer)
					.headers(CONTENT_TYPE.getMessage(), APPLICATION_JSON.getMessage())
					.version(HttpClient.Version.HTTP_1_1)
					.build();

			response = client.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException ignored) {
		}
		return response;
	}

	private HttpResponse<String> sendRequestPOST(String body) {
		HttpResponse<String> response = null;
		try {
			request = HttpRequest.newBuilder()
					.POST(HttpRequest.BodyPublishers.ofString(body))
					.uri(uriHttpServer)
					.headers(CONTENT_TYPE.getMessage(), APPLICATION_JSON.getMessage())
					.version(HttpClient.Version.HTTP_1_1)
					.build();

			response = client.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException ignored) {
		}
		return response;
	}

	private HttpResponse<String> sendRequestDELETE() {
		HttpResponse<String> response = null;
		try {
			request = HttpRequest.newBuilder()
					.DELETE()
					.uri(uriHttpServer)
					.headers(CONTENT_TYPE.getMessage(), APPLICATION_JSON.getMessage())
					.version(HttpClient.Version.HTTP_1_1)
					.build();
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException ignored) {
		}
		return response;
	}

	@BeforeAll
	public static void beforeAll() {
		kvServer = Servers.getKVServer();
		kvServer.start();
		httpServer = Servers.getHttpTaskServer();
		httpServer.start();
	}

	@AfterAll
	public static void afterAll() {
		kvServer.stop();
		httpServer.stop();
	}

	@BeforeEach
	public void beforeEach() {
		task1 = new Task("Таск1", "-", ZonedDateTime.of(LocalDateTime.of(
				2022, 1, 1, 0, 0), ZoneId.systemDefault()), 1);
		task2 = new Task("Таск2", "-");
		epic1 = new Epic("Эпик1", "-");
		epic2 = new Epic("Эпик2", "-");
		subTask1 = new SubTask("Саб1", "-", 1, ZonedDateTime.of(LocalDateTime.of(
				2022, 2, 2, 0, 0), ZoneId.systemDefault()), 1);
		subTask2 = new SubTask("Саб2", "-", 1, ZonedDateTime.of(LocalDateTime.of(
				2022, 2, 2, 1, 0), ZoneId.systemDefault()), 1);
		subTask3 = new SubTask("Саб3", "-", 1);
		subTask4 = new SubTask("Саб4", "-", 1, ZonedDateTime.of(LocalDateTime.of(
				2022, 1, 1, 0, 0), ZoneId.systemDefault()), 1);
	}

	@Test
	public void shouldReturnTrueWhenServerCreated() {
		boolean isCreate = false;
		try (Socket socket = new Socket()) {
			socket.connect(new InetSocketAddress("localhost", 8080), 5);
			isCreate = true;
		} catch (IOException ignored) {
		}
		assertTrue(isCreate);
	}

//	@Test
//	public void shouldReturnStatusForServerState() {
//		assertEquals(HTTP_TASK_SERVER_CREATED.getMessage(), httpServer.getStatusServer().getMessage());
//		httpServer.start();
//		assertEquals(HTTP_TASK_SERVER_STARTED, httpServer.getStatusServer());
//		httpServer.stop();
//		assertEquals(HTTP_TASK_SERVER_STOPPED, httpServer.getStatusServer());
//	}

	@Test
	public void shouldReturnEndpointExcludeCreateUpdateWhenSendTrueRequest() {
		handler = new TasksHandler(Managers.getDefault());
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
	}

//	@Test
//	public void shouldReturnEndpointCreateOrUpdateTaskWhenSendTrueRequest() {
//		TasksHandler handler = new TasksHandler(Managers.getDefault());
//		handler.body = JsonParser.parseString(bodyWithID);
//		assertEquals(UPDATE_TASK, handler.getEndpoint(URI.create("/tasks/task"), RequestMethod.POST));
//		handler.body = JsonParser.parseString(bodyWithoutID);
//		assertEquals(CREATE_TASK, handler.getEndpoint(URI.create("/tasks/task"), RequestMethod.POST));
//	}
//
//	@Test
//	public void shouldReturnEndpointCreateOrUpdateSubtaskWhenSendTrueRequest() {
//		handler = new TasksHandler(Managers.getDefault());
//		handler.body = JsonParser.parseString(bodyWithID);
//		assertEquals(UPDATE_SUBTASK, handler.getEndpoint(URI.create("/tasks/subtask"), RequestMethod.POST));
//		handler.body = JsonParser.parseString(bodyWithoutID);
//		assertEquals(CREATE_SUBTASK, handler.getEndpoint(URI.create("/tasks/subtask"), RequestMethod.POST));
//	}
//
//	@Test
//	public void shouldReturnEndpointCreateOrUpdateEpicWhenSendTrueRequest() {
//		handler = new TasksHandler(Managers.getDefault());
//		handler.body = JsonParser.parseString(bodyWithID);
//		assertEquals(UPDATE_EPIC, handler.getEndpoint(URI.create("/tasks/epic"), RequestMethod.POST));
//		handler.body = JsonParser.parseString(bodyWithoutID);
//		assertEquals(CREATE_EPIC, handler.getEndpoint(URI.create("/tasks/epic"), RequestMethod.POST));
//	}

	@Test
	public void shouldReturnUnknownEndpointWhenEndpointNotFind() {
		handler = new TasksHandler(Managers.getDefault());
		Endpoint endpoint = handler.getEndpoint(URI.create("/tasks/anyPathParts"), RequestMethod.GET);
		assertEquals(UNKNOWN, endpoint);
	}

	@Test
	public void shouldReturnSameTaskAfterCreated() {
		Task expectedTask = task1;
		sendRequestPOST(gson.toJson(task1));
		response = sendRequestGET();
		JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
		Task responceTask = gson.fromJson(jsonObject, Task.class);
		responceTask.setID(0);
		assertEquals(expectedTask, responceTask);
	}


}