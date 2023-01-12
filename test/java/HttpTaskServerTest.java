import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.*;
import ru.yandex.practicum.api.*;
import ru.yandex.practicum.managers.Managers;
import ru.yandex.practicum.tasks.Epic;
import ru.yandex.practicum.tasks.SubTask;
import ru.yandex.practicum.tasks.Task;
import ru.yandex.practicum.utils.GsonConfig;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.api.APIMessage.*;
import static ru.yandex.practicum.api.Endpoint.*;

public class HttpTaskServerTest {
	private final String uriHttpServer = "http://localhost:8080";
	static KVServer kvServer;
	static HttpTaskServer httpServer;
	TasksHandler handler;
	HttpRequest request;
	private final HttpClient client = HttpClient.newHttpClient();
	private HttpResponse<String> response;
	private final Gson gson = GsonConfig.getGsonTaskConfig();
	private String taskBody1;
	private String taskBody2;
	private String subtaskBody1;
	private String subtaskBody2;
	private String epicBody1;
	private String epicBody2;
	private Task task1;
	private Task task2;
	private Epic epic1;
	private Epic epic2;
	private SubTask subTask1;
	private SubTask subTask2;
	private SubTask subTask3;
	private SubTask subTask4;
	private ZonedDateTime startTime1;
	private int expectedID;


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
		sendRequestDELETE("/tasks/task");
		sendRequestDELETE("/tasks/subtask");
		sendRequestDELETE("/tasks/epic");
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
		startTime1 = ZonedDateTime.of(
				LocalDateTime.of(2030, 12, 12, 0, 0), ZoneId.systemDefault());
	}

	private int getActGenerID() {
		return httpServer.getGeneratedID() - 1;
	}

	private HttpResponse<String> sendRequestGET(String path) {
		HttpResponse<String> response = null;
		try {
			request = HttpRequest.newBuilder()
					.GET()
					.uri(URI.create(uriHttpServer + path))
					.headers(CONTENT_TYPE.getMessage(), APPLICATION_JSON.getMessage())
					.version(HttpClient.Version.HTTP_1_1)
					.build();

			response = client.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException ignored) {
		}
		return response;
	}

	private HttpResponse<String> sendRequestPOST(String body, String path) {
		HttpResponse<String> response = null;
		try {
			request = HttpRequest.newBuilder()
					.POST(HttpRequest.BodyPublishers.ofString(body))
					.uri(URI.create(uriHttpServer + path))
					.headers(CONTENT_TYPE.getMessage(), APPLICATION_JSON.getMessage())
					.version(HttpClient.Version.HTTP_1_1)
					.build();
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException ignored) {
		}
		return response;
	}

	private HttpResponse<String> sendRequestDELETE(String path) {
		HttpResponse<String> response = null;
		try {
			request = HttpRequest.newBuilder()
					.DELETE()
					.uri(URI.create(uriHttpServer + path))
					.headers(CONTENT_TYPE.getMessage(), APPLICATION_JSON.getMessage())
					.version(HttpClient.Version.HTTP_1_1)
					.build();
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException ignored) {
		}
		return response;
	}

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

//	@Test
//	public void shouldReturnUnknownEndpointWhenEndpointNotFind() {
//		handler = new TasksHandler(Managers.getDefault());
//		Endpoint endpoint = handler.getEndpoint(URI.create("/tasks/anyPathParts"), RequestMethod.GET);
//		assertEquals(UNKNOWN, endpoint);
//	}
//


	@Test
	public void shouldReturnSameTaskAfterCreated() {
		response = sendRequestPOST(gson.toJson(task1), "/tasks/task");
		JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
		Task responceTask = gson.fromJson(jsonObject, Task.class);
		expectedID = getActGenerID();
		assertEquals(expectedID, responceTask.getID());
		responceTask.setID(0);
		assertEquals(task1, responceTask);
	}

	@Test
	public void shouldReturnSameEpicAfterCreated() {
		response = sendRequestPOST(gson.toJson(epic1), "/tasks/epic");
		JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
		Epic responceTask = gson.fromJson(jsonObject, Epic.class);
		expectedID = getActGenerID();
		assertEquals(expectedID, responceTask.getID());
		responceTask.setID(0);
		assertEquals(epic1, responceTask);
	}

	@Test
	public void shouldReturnSameSubtaskAfterCreated() {
		response = sendRequestPOST(gson.toJson(epic1), "/tasks/epic");
		int epic1ID = getActGenerID();
		subTask1.setParentEpicID(epic1ID);
		response = sendRequestPOST(gson.toJson(subTask1), "/tasks/subtask");
		JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
		SubTask responceTask = gson.fromJson(jsonObject, SubTask.class);
		expectedID = getActGenerID();
		assertEquals(expectedID, responceTask.getID());
		responceTask.setID(0);
		assertEquals(subTask1, responceTask);
	}

	@Test
	public void shouldReturnUpdatedTaskWithNewFieldsAndSameID() {
		response = sendRequestPOST(gson.toJson(task1), "/tasks/task");
		JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
		Task responseTask1 = gson.fromJson(jsonObject, Task.class);
		responseTask1.setName("newName");
		responseTask1.setStartTime(startTime1);
		response = sendRequestPOST(gson.toJson(responseTask1), "/tasks/task?id="
																+ String.valueOf(responseTask1.getID()));
		jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
		Task responseTask2 = gson.fromJson(jsonObject, Task.class);
		assertEquals(responseTask1.getID(), responseTask2.getID());
		assertEquals(responseTask1, responseTask2);
	}

	@Test
	public void shouldReturnUpdatedEpicWithNewFieldsAndSameID() {
		response = sendRequestPOST(gson.toJson(epic1), "/tasks/epic");
		JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
		Epic responseTask1 = gson.fromJson(jsonObject, Epic.class);
		responseTask1.setName("newName");
		response = sendRequestPOST(gson.toJson(responseTask1), "/tasks/epic?id="
																	+ String.valueOf(responseTask1.getID()));
		jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
		Epic responseTask2 = gson.fromJson(jsonObject, Epic.class);
		assertEquals(responseTask1.getID(), responseTask2.getID());
		assertEquals(responseTask1, responseTask2);
	}

	@Test
	public void shouldReturnUpdatedSubtasksWithNewFieldsAndSameID() {
		response = sendRequestPOST(gson.toJson(epic1), "/tasks/epic");
		JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
		Epic responseEpic = gson.fromJson(jsonObject, Epic.class);
		int epicID = responseEpic.getID();
		subTask1.setParentEpicID(epicID);
		response = sendRequestPOST(gson.toJson(subTask1), "/tasks/subtask");
		jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
		SubTask responseSubtask1 = gson.fromJson(jsonObject, SubTask.class);
		int responseSubtask1ID = responseSubtask1.getID();
		subTask1.setName("newName");
		subTask1.setID(responseSubtask1ID);
		response = sendRequestPOST(gson.toJson(subTask1), "/tasks/subtask?id="
															+ String.valueOf(responseSubtask1.getID()));
		jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
		SubTask responseSubtask2 = gson.fromJson(jsonObject, SubTask.class);
		assertEquals(responseSubtask1.getID(), responseSubtask2.getID());
		assertEquals(responseSubtask1, responseSubtask2);
	}

	@Test
	public void shouldReturnCreatedTaskIfCallGetByID() {
		response = sendRequestPOST(gson.toJson(task1), "/tasks/task");
		JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
		Task expectedTask = gson.fromJson(jsonObject, Task.class);
		int idToFind = expectedTask.getID();
		response = sendRequestGET("/tasks/task?id="
									+ String.valueOf(idToFind));
		jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
		Task actualTask = gson.fromJson(jsonObject, Task.class);
		assertEquals(expectedTask, actualTask);
	}

	@Test
	public void shouldReturnCreatedEpicIfCallGetByID() {
		response = sendRequestPOST(gson.toJson(epic1), "/tasks/epic");
		JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
		Epic expectedEpic = gson.fromJson(jsonObject, Epic.class);
		int idToFind = expectedEpic.getID();
		response = sendRequestGET("/tasks/epic?id="
				+ String.valueOf(idToFind));
		jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
		Epic actualTask = gson.fromJson(jsonObject, Epic.class);
		assertEquals(expectedEpic, actualTask);
	}

	@Test
	public void shouldReturnCreatedSubtaskIfCallGetByID() {
		response = sendRequestPOST(gson.toJson(epic1), "/tasks/epic");
		JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
		Epic epic = gson.fromJson(jsonObject, Epic.class);
		int epicID = epic.getID();
		subTask1.setParentEpicID(epicID);
		response = sendRequestPOST(gson.toJson(subTask1), "/tasks/subtask");
		jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
		SubTask expectedSubtask = gson.fromJson(jsonObject, SubTask.class);
		int idToFind = expectedSubtask.getID();
		response = sendRequestGET("/tasks/subtask?id="
				+ String.valueOf(idToFind));
		jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
		SubTask actualSubtask = gson.fromJson(jsonObject, SubTask.class);
		assertEquals(expectedSubtask, actualSubtask);
	}

	@Test
	public void shouldReturnSuccessBodyMessageIfDeletedTask() {
		response = sendRequestPOST(gson.toJson(task1), "/tasks/task");
		JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
		Task deletingTask = gson.fromJson(jsonObject, Task.class);
		response = sendRequestDELETE("/tasks/task?id=" + deletingTask.getID());
		assertEquals("\"" + APIMessage.DELETE_ACCEPT.getMessage() + "\"", response.body());
	}

	@Test
	public void shouldReturnSuccessBodyMessageIfDeletedSubtask() {
		response = sendRequestPOST(gson.toJson(epic1), "/tasks/epic");
		JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
		Epic epic = gson.fromJson(jsonObject, Epic.class);
		int epicID = epic.getID();
		subTask1.setParentEpicID(epicID);
		response = sendRequestPOST(gson.toJson(subTask1), "/tasks/subtask");
		jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
		SubTask deletingSubtask = gson.fromJson(jsonObject, SubTask.class);
		response = sendRequestDELETE("/tasks/subtask?id=" + deletingSubtask.getID());
		assertEquals("\"" + APIMessage.DELETE_ACCEPT.getMessage() + "\"", response.body());
	}

	@Test
	public void shouldReturnSuccessBodyMessageIfDeletedEpic() {
		response = sendRequestPOST(gson.toJson(epic1), "/tasks/epic");
		JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
		Epic deletingEpic = gson.fromJson(jsonObject, Epic.class);
		response = sendRequestDELETE("/tasks/epic?id=" + deletingEpic.getID());
		assertEquals("\"" + APIMessage.DELETE_ACCEPT.getMessage() + "\"", response.body());
	}

	@Test
	public void shouldListOfTaskWhenGetAllTasks() {
		response = sendRequestPOST(gson.toJson(task1), "/tasks/task");
		JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
		Task task = gson.fromJson(jsonObject, Task.class);
		List<Task> expectedList = List.of(task);
		response = sendRequestGET("/tasks/task");
		jsonObject
		Type type = new TypeToken<List<Task>>(){}.getType();
		List<Task> list = gson.fromJson()
	}


}