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

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

import static ru.yandex.practicum.api.APIMessage.*;
import static ru.yandex.practicum.api.Endpoint.*;
import static ru.yandex.practicum.api.RequestMethod.*;

public class HttpTaskServerTest {
	static KVServer kvServer;
	static HttpTaskServer httpServer;
	private final String uriHttpServer = "http://localhost:8080";
	private final HttpClient client = HttpClient.newHttpClient();
	private final Gson gson = GsonConfig.getGsonTaskConfig();
	TasksHandler handler;
	HttpRequest request;
	private HttpResponse<String> response;
	private Task task1;
	private Task task2;
	private Epic epic1;
	private Epic epic2;
	private SubTask subTask1;
	private SubTask subTask2;
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
		clearAllTasks();
		task1 = new Task("Таск1", "-", ZonedDateTime.of(LocalDateTime.of(
				2022, 1, 1, 0, 0), ZoneId.systemDefault()), 1);
		task2 = new Task("Таск2", "-");
		epic1 = new Epic("Эпик1", "-");
		epic2 = new Epic("Эпик2", "-");
		subTask1 = new SubTask("Саб1", "-", 1, ZonedDateTime.of(LocalDateTime.of(
				2022, 2, 2, 0, 0), ZoneId.systemDefault()), 1);
		subTask2 = new SubTask("Саб2", "-", 1, ZonedDateTime.of(LocalDateTime.of(
				2022, 2, 2, 1, 0), ZoneId.systemDefault()), 1);
		startTime1 = ZonedDateTime.of(
				LocalDateTime.of(2030, 12, 12, 0, 0), ZoneId.systemDefault());
	}

	private void clearAllTasks() {
		sendRequestDELETE("/tasks/task");
		sendRequestDELETE("/tasks/subtask");
		sendRequestDELETE("/tasks/epic");
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
		} catch (IOException | InterruptedException ignored) {}
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
		} catch (IOException | InterruptedException ignored) {}
		return response;
	}

	@Test
	public void shouldReturnEndpointExcludeCreateUpdateWhenSendTrueRequest() {
		handler = new TasksHandler(Managers.getDefault());
		assertEquals(GET_ALL_TASKS, handler.getEndpoint(URI.create("/tasks/task"), GET));
		assertEquals(GET_ALL_SUBTASKS, handler.getEndpoint(URI.create("/tasks/subtask"), GET));
		assertEquals(GET_ALL_EPICS, handler.getEndpoint(URI.create("/tasks/epic"), GET));
		assertEquals(GET_TASK_BY_ID, handler.getEndpoint(URI.create("/tasks/task?id="), GET));
		assertEquals(GET_SUBTASK_BY_ID, handler.getEndpoint(URI.create("/tasks/subtask?id="), GET));
		assertEquals(GET_EPIC_BY_ID, handler.getEndpoint(URI.create("/tasks/epic?id="), GET));
		assertEquals(GET_ALL_SUBTASKS_BY_EPIC, handler.getEndpoint(URI.create("/tasks/subtask/epic?id="), GET));
		assertEquals(GET_PRIORITIZED_TASKS, handler.getEndpoint(URI.create("/tasks"), GET));
		assertEquals(GET_HISTORY, handler.getEndpoint(URI.create("/tasks/history"), GET));
		assertEquals(DELETE_ALL_TASKS, handler.getEndpoint(URI.create("/tasks/task"), DELETE));
		assertEquals(DELETE_ALL_SUBTASKS, handler.getEndpoint(URI.create("/tasks/subtask"), DELETE));
		assertEquals(DELETE_ALL_EPICS, handler.getEndpoint(URI.create("/tasks/epic"), DELETE));
		assertEquals(DELETE_TASK_BY_ID, handler.getEndpoint(URI.create("/tasks/task?id="), DELETE));
		assertEquals(DELETE_SUBTASK_BY_ID, handler.getEndpoint(URI.create("/tasks/subtask?id="), DELETE));
		assertEquals(DELETE_EPIC_BY_ID, handler.getEndpoint(URI.create("/tasks/epic?id="), DELETE));
	}

	@Test
	public void shouldReturnSameTaskAfterCreated() {
		response = sendRequestPOST(gson.toJson(task1), "/tasks/task");
		Task responceTask = gson.fromJson(response.body(), Task.class);
		expectedID = getActGenerID();
		assertEquals(expectedID, responceTask.getID());
		responceTask.setID(0);
		assertEquals(task1, responceTask);
	}

	@Test
	public void shouldReturnSameEpicAfterCreated() {
		response = sendRequestPOST(gson.toJson(epic1), "/tasks/epic");
		Epic responceTask = gson.fromJson(response.body(), Epic.class);
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
		SubTask responceTask = gson.fromJson(response.body(), SubTask.class);
		expectedID = getActGenerID();
		assertEquals(expectedID, responceTask.getID());
		responceTask.setID(0);
		assertEquals(subTask1, responceTask);
	}

	@Test
	public void shouldReturnUpdatedTaskWithNewFieldsAndSameID() {
		response = sendRequestPOST(gson.toJson(task1), "/tasks/task");
		Task responseTask1 = gson.fromJson(response.body(), Task.class);
		responseTask1.setName("newName");
		responseTask1.setStartTime(startTime1);
		response = sendRequestPOST(gson.toJson(responseTask1), "/tasks/task?id="
				+ responseTask1.getID());
		Task responseTask2 = gson.fromJson(response.body(), Task.class);
		assertEquals(responseTask1.getID(), responseTask2.getID());
		assertEquals(responseTask1, responseTask2);
	}

	@Test
	public void shouldReturnUpdatedEpicWithNewFieldsAndSameID() {
		response = sendRequestPOST(gson.toJson(epic1), "/tasks/epic");
		Epic responseTask1 = gson.fromJson(response.body(), Epic.class);
		responseTask1.setName("newName");
		response = sendRequestPOST(gson.toJson(responseTask1), "/tasks/epic?id="
				+ responseTask1.getID());
		Epic responseTask2 = gson.fromJson(response.body(), Epic.class);
		assertEquals(responseTask1.getID(), responseTask2.getID());
		assertEquals(responseTask1, responseTask2);
	}

	@Test
	public void shouldReturnUpdatedSubtasksWithNewFieldsAndSameID() {
		response = sendRequestPOST(gson.toJson(epic1), "/tasks/epic");
		Epic responseEpic = gson.fromJson(response.body(), Epic.class);
		int epicID = responseEpic.getID();
		subTask1.setParentEpicID(epicID);
		response = sendRequestPOST(gson.toJson(subTask1), "/tasks/subtask");
		SubTask responseSubtask1 = gson.fromJson(response.body(), SubTask.class);
		int responseSubtask1ID = responseSubtask1.getID();
		subTask1.setName("newName");
		subTask1.setID(responseSubtask1ID);
		response = sendRequestPOST(gson.toJson(subTask1), "/tasks/subtask?id="
				+ responseSubtask1.getID());
		SubTask responseSubtask2 = gson.fromJson(response.body(), SubTask.class);
		assertEquals(responseSubtask1.getID(), responseSubtask2.getID());
		assertEquals(responseSubtask1, responseSubtask2);
	}

	@Test
	public void shouldReturnCreatedTaskIfCallGetByID() {
		response = sendRequestPOST(gson.toJson(task1), "/tasks/task");
		Task expectedTask = gson.fromJson(response.body(), Task.class);
		int idToFind = expectedTask.getID();
		response = sendRequestGET("/tasks/task?id="
				+ idToFind);
		Task actualTask = gson.fromJson(response.body(), Task.class);
		assertEquals(expectedTask, actualTask);
	}

	@Test
	public void shouldReturnCreatedEpicIfCallGetByID() {
		response = sendRequestPOST(gson.toJson(epic1), "/tasks/epic");
		Epic expectedEpic = gson.fromJson(response.body(), Epic.class);
		int idToFind = expectedEpic.getID();
		response = sendRequestGET("/tasks/epic?id="
				+ idToFind);
		Epic actualTask = gson.fromJson(response.body(), Epic.class);
		assertEquals(expectedEpic, actualTask);
	}

	@Test
	public void shouldReturnCreatedSubtaskIfCallGetByID() {
		response = sendRequestPOST(gson.toJson(epic1), "/tasks/epic");
		Epic epic = gson.fromJson(response.body(), Epic.class);
		int epicID = epic.getID();
		subTask1.setParentEpicID(epicID);
		response = sendRequestPOST(gson.toJson(subTask1), "/tasks/subtask");
		SubTask expectedSubtask = gson.fromJson(response.body(), SubTask.class);
		int idToFind = expectedSubtask.getID();
		response = sendRequestGET("/tasks/subtask?id="
				+ idToFind);
		SubTask actualSubtask = gson.fromJson(response.body(), SubTask.class);
		assertEquals(expectedSubtask, actualSubtask);
	}

	@Test
	public void shouldReturnSuccessBodyMessageIfDeletedTask() {
		response = sendRequestPOST(gson.toJson(task1), "/tasks/task");
		Task deletingTask = gson.fromJson(response.body(), Task.class);
		response = sendRequestDELETE("/tasks/task?id=" + deletingTask.getID());
		assertEquals("\"" + APIMessage.DELETE_ACCEPT.getMessage() + "\"", response.body());
	}

	@Test
	public void shouldReturnSuccessBodyMessageIfDeletedSubtask() {
		response = sendRequestPOST(gson.toJson(epic1), "/tasks/epic");
		Epic epic = gson.fromJson(response.body(), Epic.class);
		int epicID = epic.getID();
		subTask1.setParentEpicID(epicID);
		response = sendRequestPOST(gson.toJson(subTask1), "/tasks/subtask");
		SubTask deletingSubtask = gson.fromJson(response.body(), SubTask.class);
		response = sendRequestDELETE("/tasks/subtask?id=" + deletingSubtask.getID());
		assertEquals("\"" + APIMessage.DELETE_ACCEPT.getMessage() + "\"", response.body());
	}

	@Test
	public void shouldReturnSuccessBodyMessageIfDeletedEpic() {
		response = sendRequestPOST(gson.toJson(epic1), "/tasks/epic");
		Epic deletingEpic = gson.fromJson(response.body(), Epic.class);
		response = sendRequestDELETE("/tasks/epic?id=" + deletingEpic.getID());
		assertEquals("\"" + APIMessage.DELETE_ACCEPT.getMessage() + "\"", response.body());
	}

	@Test
	public void shouldReturnSameListOfTaskWhenGetAllTasks() {
		response = sendRequestPOST(gson.toJson(task1), "/tasks/task");
		Task expectedTask1 = gson.fromJson(response.body(), Task.class);
		response = sendRequestPOST(gson.toJson(task2), "/tasks/task");
		Task expectedTask2 = gson.fromJson(response.body(), Task.class);
		Task[] expectedArray = new Task[]{expectedTask1, expectedTask2};
		response = sendRequestGET("/tasks/task");
		Type type = new TypeToken<Task[]>() {}.getType();
		Task[] actualArray = gson.fromJson(response.body(), type);
		assertArrayEquals(expectedArray, actualArray);
	}

	@Test
	public void shouldReturnSameListOfEpicWhenGetAllEpics() {
		response = sendRequestPOST(gson.toJson(epic1), "/tasks/epic");
		Epic expectedTask1 = gson.fromJson(response.body(), Epic.class);
		response = sendRequestPOST(gson.toJson(epic2), "/tasks/epic");
		Epic expectedTask2 = gson.fromJson(response.body(), Epic.class);
		Epic[] expectedArray = new Epic[]{expectedTask1, expectedTask2};
		response = sendRequestGET("/tasks/epic");
		Type type = new TypeToken<Epic[]>() {}.getType();
		Epic[] actualArray = gson.fromJson(response.body(), type);
		assertArrayEquals(expectedArray, actualArray);
	}

	@Test
	public void shouldReturnSameListOfSubtaskWhenGetAllSubtasks() {
		response = sendRequestPOST(gson.toJson(epic1), "/tasks/epic");
		Epic epic = gson.fromJson(response.body(), Epic.class);
		int epicID = epic.getID();
		subTask1.setParentEpicID(epicID);
		subTask2.setParentEpicID(epicID);
		response = sendRequestPOST(gson.toJson(subTask1), "/tasks/subtask");
		SubTask expectedTask1 = gson.fromJson(response.body(), SubTask.class);
		response = sendRequestPOST(gson.toJson(subTask2), "/tasks/subtask");
		SubTask expectedTask2 = gson.fromJson(response.body(), SubTask.class);
		SubTask[] expectedArray = new SubTask[]{expectedTask1, expectedTask2};
		response = sendRequestGET("/tasks/subtask");
		Type type = new TypeToken<SubTask[]>() {}.getType();
		SubTask[] actualArray = gson.fromJson(response.body(), type);
		assertArrayEquals(expectedArray, actualArray);
	}

	@Test
	public void shouldReturnPrioritizedList() {
		response = sendRequestPOST(gson.toJson(task1), "/tasks/task");
		Task expectedTask1 = gson.fromJson(response.body(), Task.class);
		response = sendRequestPOST(gson.toJson(task2), "/tasks/task");
		Task expectedTask2 = gson.fromJson(response.body(), Task.class);
		response = sendRequestPOST(gson.toJson(epic1), "/tasks/epic");
		Epic epic = gson.fromJson(response.body(), Epic.class);
		int epicID = epic.getID();
		subTask1.setParentEpicID(epicID);
		subTask2.setParentEpicID(epicID);
		response = sendRequestPOST(gson.toJson(subTask1), "/tasks/subtask");
		SubTask expectedSubtask1 = gson.fromJson(response.body(), SubTask.class);
		response = sendRequestPOST(gson.toJson(subTask2), "/tasks/subtask");
		SubTask expectedSubtask2 = gson.fromJson(response.body(), SubTask.class);
		response = sendRequestGET("/tasks");
		int[] expectedArray = new int[] {
				expectedTask1.getID(), expectedSubtask1.getID(), expectedSubtask2.getID(), expectedTask2.getID()};
		Type type = new TypeToken<Task[]>() {}.getType();
		Task[] responseArray = gson.fromJson(response.body(), type);
		int[] actualArray = Arrays.stream(responseArray).mapToInt(Task::getID).toArray();
		assertArrayEquals(expectedArray, actualArray);
	}

	@Test
	public void shouldReturnHistoryList() {
		response = sendRequestPOST(gson.toJson(task1), "/tasks/task");
		Task expectedTask1 = gson.fromJson(response.body(), Task.class);
		response = sendRequestPOST(gson.toJson(epic1), "/tasks/epic");
		Epic expectedEpic1 = gson.fromJson(response.body(), Epic.class);
		subTask1.setParentEpicID(expectedEpic1.getID());
		response = sendRequestPOST(gson.toJson(subTask1), "/tasks/subtask");
		SubTask expectedSubtask1 = gson.fromJson(response.body(), SubTask.class);
		sendRequestGET("/tasks/task?id=" + expectedTask1.getID());
		sendRequestGET("/tasks/subtask?id=" + expectedSubtask1.getID());
		sendRequestGET("/tasks/epic?id=" + expectedEpic1.getID());
		response = sendRequestGET("/tasks/history");
		int[] expectedArray = new int[] {
				expectedTask1.getID(), expectedSubtask1.getID(), expectedEpic1.getID()};
		Type type = new TypeToken<Task[]>() {}.getType();
		Task[] responseArray = gson.fromJson(response.body(), type);
		int[] actualArray = Arrays.stream(responseArray).mapToInt(Task::getID).toArray();
		assertArrayEquals(expectedArray, actualArray);
	}

	@Test
	public void shouldLoadSameTasksWhenCreateNewHttpTaskServer() {
		response = sendRequestPOST(gson.toJson(task1), "/tasks/task");
		Task expectedTask1 = gson.fromJson(response.body(), Task.class);
		response = sendRequestPOST(gson.toJson(task2), "/tasks/task");
		Task expectedTask2 = gson.fromJson(response.body(), Task.class);
		Task[] expectedArray = new Task[]{expectedTask1, expectedTask2};
		response = sendRequestGET("/tasks/task");
		httpServer.stop();
		httpServer = Servers.getHttpTaskServer();
		httpServer.start();
		System.out.println();
		Type type = new TypeToken<Task[]>() {}.getType();
		Task[] actualArray = gson.fromJson(response.body(), type);
		assertArrayEquals(expectedArray, actualArray);
	}
}